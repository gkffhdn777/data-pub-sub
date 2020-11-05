package com.coding.kko.func;

import static java.util.concurrent.Flow.Subscriber;
import static java.util.concurrent.Flow.Subscription;

import com.coding.kko.consumer.Partition;

public class Queue<T> extends Mono<T> {
	final Mono<T> mono;

	final Partition<? super T> queue;

	public Queue(final Mono<T> mono, final Partition<? super T> queue) {
		this.mono = mono;
		this.queue = queue;
	}

	@Override
	public void subscribe(final Subscriber<? super T> subscriber) {
		this.mono.subscribe(new Subscriber<>() {
			@Override
			public void onSubscribe(final Subscription subscription) {
				subscriber.onSubscribe(subscription);
			}

			@Override
			public void onNext(final T item) {
				try {
					queue.add(item);
					subscriber.onNext(item);
				} catch (Throwable e) {
					onError(e);
				}
			}

			@Override
			public void onError(final Throwable throwable) {
				subscriber.onError(throwable);
			}

			@Override
			public void onComplete() {
				subscriber.onComplete();
			}
		});
	}
}
