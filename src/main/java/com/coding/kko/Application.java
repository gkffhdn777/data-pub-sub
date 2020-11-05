package com.coding.kko;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		final var request = userRequest();
		CodingTaskApplication.run(request);
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
