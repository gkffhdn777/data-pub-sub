package com.coding.kko.producer;

import java.util.Objects;

import com.coding.kko.func.Mono;

public final class ReadProducer {

	private final InputFile<String> inputFile;

	public ReadProducer(final InputFile<String> inputFile) {
		this.inputFile = inputFile;
	}

	public Mono<String> sendWord(final String fileName) {
		Objects.requireNonNull(fileName, "FileName cannot be null.");
		return Mono.fromIterable(this.inputFile.readFile(fileName));
	}
}
