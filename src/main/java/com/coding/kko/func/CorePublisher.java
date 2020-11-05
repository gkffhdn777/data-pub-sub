package com.coding.kko.func;

import java.util.concurrent.Flow;

public interface CorePublisher<T> extends Flow.Publisher<T> {
	@Override
	void subscribe(Flow.Subscriber<? super T> subscriber);
}
