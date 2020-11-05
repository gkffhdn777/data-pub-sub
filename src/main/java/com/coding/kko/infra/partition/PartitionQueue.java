package com.coding.kko.infra.partition;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.coding.kko.consumer.Partition;
import com.coding.kko.utils.KeyGenerator;

public class PartitionQueue<V> implements Partition<V> {

	private final Map<Integer, Queue<V>> partition;

	private final int partitionSize;

	public PartitionQueue(final int partitionSize) {
		if (partitionSize <= 0) {
			throw new IllegalArgumentException("PartitionSize Must be greater than zero");
		}
		Map<Integer, Queue<V>> map = new ConcurrentHashMap<>(partitionSize);
		for (int i = 0; i < partitionSize; i++) {
			map.put(i, new ConcurrentLinkedQueue<>());
		}
		this.partitionSize = partitionSize;
		this.partition = Collections.unmodifiableMap(map);
	}

	public Queue<V> findByWord(final V key) {
		Objects.requireNonNull(key, "PartitionQueue key cannot be null.");
		return this.partition.get(KeyGenerator.getKey(key, partitionSize));
	}

	public void add(final V v) {
		Objects.requireNonNull(v, "Queue Data cannot be null.");
		findByWord(v).add(v);
	}

	public Queue<V> findByKey(final int partitionKey) {
		if (partitionKey < 0) {
			throw new IllegalArgumentException("PartitionKey Size must be strictly positive");
		}
		return this.partition.get(partitionKey);
	}
}
