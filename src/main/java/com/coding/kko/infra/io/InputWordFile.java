package com.coding.kko.infra.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.coding.kko.producer.InputFile;

public final class InputWordFile implements InputFile<String> {

	private static final String STATIC_PATH = "/static/";

	private final Predicate<String> validWord;

	public InputWordFile(final Predicate<String> validWord) {
		this.validWord = Objects.requireNonNullElseGet(validWord, () -> (x) -> true);
	}

	public List<String> readFile(final String fileName) {
		if (!fileName.contains(".txt")) {
			throw new IllegalArgumentException("The file can only be (.txt)");
		}
		var path = Paths.get(InputWordFile.class.getResource(STATIC_PATH).getPath() + fileName);

		try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
			return this.readAllLines(reader);
		} catch (IOException e) {
			throw new ReadFileException(e.getMessage(), e);
		}
	}

	private List<String> readAllLines(final BufferedReader reader) {
		return reader.lines()
				.filter(Objects::nonNull)
				.filter(word -> !word.isEmpty())
				.filter(this.validWord)
				.map(word -> word.replaceAll(" ", ""))
				.collect(Collectors.toUnmodifiableList());
	}

	private static final class ReadFileException extends RuntimeException {
		public ReadFileException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
