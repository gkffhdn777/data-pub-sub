package com.coding.kko.func;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class FromIterable<T> extends Mono<T> {

	final Iterable<T> iterable;

	FromIterable(final Iterable<T> value) {
		this.iterable = Objects.requireNonNull(value, "value");
	}

	@Override
	public void subscribe(final Flow.Subscriber<? super T> subscriber) {
		final AtomicBoolean canceled = new AtomicBoolean(Boolean.FALSE);
		final AtomicLong bufferControlSize = new AtomicLong(0);

		subscriber.onSubscribe(new Flow.Subscription() {
			final Iterator<T> iter = iterable.iterator();

			@Override
			public void request(final long n) {
				try {
					if (Long.MAX_VALUE == n) {
						iterable.forEach(subscriber::onNext);
						subscriber.onComplete();
						return;
					}

					bufferControlSize.set(n);

					if (canceled.get()) {
						subscriber.onComplete();
						return;
					}

					while (bufferControlSize.getAndDecrement() > 0) {
						if (this.iter.hasNext()) {
							subscriber.onNext(this.iter.next());
						} else {
							subscriber.onComplete();
							break;
						}
					}

				} catch (final Exception e) {
					subscriber.onError(e);
				}
			}

			@Override
			public void cancel() {
				canceled.set(Boolean.TRUE);
			}
		});
	}
}
