package com.coding.kko.infra.cache;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.coding.kko.file.Cache;

public class Snapshot<T> implements Cache<T> {

	private final Set<T> words;

	public Snapshot(final ConcurrentHashMap.KeySetView<T, Boolean> words) {
		this.words = words;
	}

	public Snapshot() {
		this(ConcurrentHashMap.newKeySet());
	}

	public void add(final T t) {
		Objects.requireNonNull(t, "Snapshot data cannot be null.");
		this.words.add(t);
	}

	public boolean doesNotContain(final T t) {
		Objects.requireNonNull(t, "cannot be null.");
		return !this.words.contains(t);
	}
}
