package com.coding.kko.file;

public interface Cache<T> {
	void add(final T t);

	boolean doesNotContain(final T t);
}
