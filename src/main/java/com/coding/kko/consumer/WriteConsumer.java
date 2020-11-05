package com.coding.kko.consumer;

import static java.util.concurrent.Flow.Subscription;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import com.coding.kko.file.FileService;
import com.coding.kko.func.CoreSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WriteConsumer implements CoreSubscriber<String> {

	private static final Logger logger = LoggerFactory.getLogger(WriteConsumer.class);

	private final Partition<String> partitionQueue;

	private final int partitionSize;

	private final FileService fileService;

	private final long backpressureSize;

	private final AtomicLong bufferControlSize;

	private final AtomicBoolean isComplete = new AtomicBoolean(false);

	private Subscription subscription;

	public WriteConsumer(
			final Partition<String> partitionQueue,
			final int partitionSize,
			final FileService fileService,
			final long backpressureSize) {
		Objects.requireNonNull(partitionQueue, "PartitionQueue cannot be null.");
		Objects.requireNonNull(fileService, "fileService cannot be null.");
		this.partitionQueue = partitionQueue;
		this.partitionSize = partitionSize;
		this.fileService = fileService;
		this.backpressureSize = (backpressureSize <= 0) ? Long.MAX_VALUE : backpressureSize;
		this.bufferControlSize = new AtomicLong(this.backpressureSize);
	}

	@Override
	public void onSubscribe(final Subscription subscription) {
		this.subscription = subscription;
		subscription.request(this.backpressureSize);
	}

	@Override
	public void onNext(final String word) {
		try {
			if (this.backpressureSize != Long.MAX_VALUE) {
				if (this.bufferControlSize.getAndDecrement() <= 1) {
					this.bufferControlSize.set(this.backpressureSize);
					this.consumeQueue(this.partitionSize);
					this.subscription.request(this.backpressureSize);
				}
			}
		} catch (Exception e) {
			onError(e);
		}
	}

	@Override
	public void onError(final Throwable throwable) {
		throw new WriteConsumerException(throwable.getMessage(), throwable);
	}


	@Override
	public void onComplete() {
		try {
			this.isComplete.set(Boolean.TRUE);
			if (this.backpressureSize == Long.MAX_VALUE || this.isComplete.get()) {
				this.consumeQueue(this.partitionSize);
				this.isComplete.set(Boolean.FALSE);
			}
		} catch (Exception e) {
			onError(e);
		}
	}

	private void consumeQueue(final int partitionSize) {
		IntStream.range(0, partitionSize)
				.mapToObj(this.partitionQueue::findByKey)
				.filter(x -> !x.isEmpty())
				.parallel()
				.forEach(this.fileService::doWrite);
	}

	private static final class WriteConsumerException extends RuntimeException {
		public WriteConsumerException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}