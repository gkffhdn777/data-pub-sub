package com.coding.kko.file;

import java.util.List;
import java.util.Objects;

public final class WordsFile {

	private final String fileName;

	private final String directoryPath;

	private final List<String> words;

	private final OutputFile<WordsFile> outputFile;

	WordsFile(
			final String fileName,
			final List<String> words,
			final String directoryPath,
			final OutputFile<WordsFile> outputFile) {
		this.fileName = fileName;
		this.words = words;
		this.directoryPath = directoryPath;
		this.outputFile = outputFile;
	}

	void writer() {
		this.outputFile.writeFile(this);
	}

	public String getFileName() {
		return fileName;
	}

	public List<String> getWords() {
		return words;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WordsFile wordsFile = (WordsFile) o;
		return Objects.equals(fileName, wordsFile.fileName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileName);
	}

	@Override
	public String toString() {
		return "writerFile{" +
				"fileName='" + fileName + '\'' +
				", words=" + words +
				'}';
	}

}
