package com.coding.kko.func;

import java.util.function.Function;

import com.coding.kko.consumer.Partition;

public abstract class Mono<T> implements CorePublisher<T> {

	public static <T> Mono<T> fromIterable(final Iterable<T> iterable) {
		return new FromIterable<>(iterable);
	}

	public final <R> Mono<R> map(final Function<? super T, ? extends R> mapper) {
		return new Map<>(this, mapper);
	}

	public final Mono<T> addQueue(final Partition<? super T> queue) {
		return new Queue<>(this, queue);
	}
}
