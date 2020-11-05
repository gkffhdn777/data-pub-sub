package com.coding.kko.infra.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Objects;

import com.coding.kko.file.Cache;
import com.coding.kko.file.OutputFile;
import com.coding.kko.file.WordsFile;

public final class OutputWordFile implements OutputFile<WordsFile> {

	private final Cache<String> snapshot;

	public OutputWordFile(final Cache<String> snapshot) {
		Objects.requireNonNull(snapshot, "Snapshot cannot be null.");
		this.snapshot = snapshot;
	}

	public void writeFile(final WordsFile wordsFile) {
		if (wordsFile == null) {
			return;
		}
		var path = Paths.get(wordsFile.getDirectoryPath() + "/" + wordsFile.getFileName() + ".txt");
		try (var fw = new FileWriter(path.toFile(), true);
			 var bw = new BufferedWriter(fw);
			 var out = new PrintWriter(bw)) {
			this.printFile(wordsFile, out);
		} catch (IOException e) {
			throw new WriteFileException(e.getMessage(), e);
		}
	}

	private void printFile(final WordsFile wordsFile, final PrintWriter out) {
		wordsFile.getWords().stream()
				.filter(this.snapshot::doesNotContain)
				.forEach(word -> {
					out.write(word + ",");
					this.snapshot.add(word);
				});
	}

	private static final class WriteFileException extends RuntimeException {
		public WriteFileException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
