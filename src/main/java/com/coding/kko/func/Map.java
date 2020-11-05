package com.coding.kko.func;

import static java.util.concurrent.Flow.Subscriber;
import static java.util.concurrent.Flow.Subscription;

import java.util.function.Function;

public class Map<T, R> extends Mono<R> {
	final Mono<T> mono;

	final Function<? super T, ? extends R> mapper;

	public Map(final Mono<T> mono, final Function<? super T, ? extends R> mapper) {
		this.mono = mono;
		this.mapper = mapper;
	}

	@Override
	public void subscribe(final Subscriber<? super R> subscriber) {
		this.mono.subscribe(new Subscriber<>() {
			@Override
			public void onSubscribe(final Subscription subscription) {
				subscriber.onSubscribe(subscription);
			}

			@Override
			public void onNext(final T item) {
				try {
					subscriber.onNext(mapper.apply(item));
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
