package com.coding.kko.func;

import java.util.concurrent.Flow;

public interface CoreSubscriber<T> extends Flow.Subscriber<T> {
	@Override
	void onSubscribe(Flow.Subscription s);
}
