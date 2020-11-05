package com.coding.kko;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.coding.kko.consumer.Partition;
import com.coding.kko.consumer.WriteConsumer;
import com.coding.kko.file.Cache;
import com.coding.kko.file.FileService;
import com.coding.kko.infra.cache.Snapshot;
import com.coding.kko.infra.io.InputWordFile;
import com.coding.kko.infra.io.OutputWordFile;
import com.coding.kko.infra.partition.PartitionQueue;
import com.coding.kko.producer.ReadProducer;

public final class CodingTaskApplication {

	private CodingTaskApplication() {}

	private static final Cache<String> SNAPSHOT = new Snapshot<>();

	public static void run(final UserRequest request) {

		final Partition<String> partition = new PartitionQueue<>(request.getPartition());
		final Predicate<String> validWord = (word) -> Pattern.matches("([a-zA-Z0-9]{0})\\w+.*", word);
		final long backpressureSize = 10;

		new ReadProducer(new InputWordFile(validWord))
				.sendWord(request.getFileName())
				.addQueue(partition)
				.subscribe(new WriteConsumer(
						partition,
						request.getPartition(),
						new FileService(new OutputWordFile(SNAPSHOT), request.getDirectoryPath()),
						backpressureSize));
	}
}
