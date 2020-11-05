package com.coding.kko;

import java.util.Scanner;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private static final Cache<String> SNAPSHOT = new Snapshot<>();

	public static void main(String[] args) {
		final var request = userRequest();
		final Predicate<String> valid = (validWord) -> Pattern.matches("([a-zA-Z0-9]{0})\\w+.*", validWord);
		applicationStart(valid, request, 5);
	}

	private static void applicationStart(
			final Predicate<String> validWord,
			final UserRequest request,
			final long backpressureSize) {

		final Partition<String> partition = new PartitionQueue<>(request.getPartition());

		new ReadProducer(new InputWordFile(validWord))
				.sendWord(request.getFileName())
				.addQueue(partition)
				.subscribe(
						new WriteConsumer(
								partition,
								request.getPartition(),
								new FileService(new OutputWordFile(SNAPSHOT), request.getDirectoryPath()),
								backpressureSize));
	}

	private static UserRequest userRequest() {
		Scanner scanner = new Scanner(System.in);
		logger.info("a. 처리해야 할 입력 파일명");
		String fileName = scanner.nextLine();
		logger.info("b. 결과 파일들을 저장 할 디렉토리 경로");
		String directoryPath = scanner.nextLine();
		logger.info("c. 병렬 처리를 위한 파티션 수 N (1 < N < 28)");
		int partition = scanner.nextInt();
		return new UserRequest(fileName, directoryPath, partition);
	}
}
