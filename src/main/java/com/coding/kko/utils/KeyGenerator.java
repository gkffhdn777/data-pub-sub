package com.coding.kko.utils;

public final class KeyGenerator {

	private KeyGenerator() {
	}

	public static <T> Integer getKey(final T key, final int partition) {
		if (key instanceof String) {
			return Math.abs(((String) key).toLowerCase().hashCode() % partition);
		}
		return Math.abs(key.hashCode() % partition);
	}
}
