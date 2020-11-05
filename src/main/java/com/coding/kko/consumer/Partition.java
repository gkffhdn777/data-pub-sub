package com.coding.kko.consumer;

import java.util.Queue;

public interface Partition<T> {
	void add(T v);

	Queue<T> findByKey(int key);
}
