package com.coding.kko.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public final class FileService {

	private static final String NUMBER_KEY_NAME = "number";

	private static final String NUMBER_KEY_REGEX = "[0-9].*";

	private final String directoryPath;

	private final OutputFile<WordsFile> outputFile;

	public FileService(final OutputFile<WordsFile> outputFile, final String directoryPath) {
		this.directoryPath = directoryPath;
		this.outputFile = outputFile;
	}

	public void doWrite(final Queue<String> queue) {
		Objects.requireNonNull(queue, "Queue cannot be null.");
		this.groupBy(queue).forEach((key, value) -> new WordsFile(key, value, this.directoryPath, this.outputFile).writer());
	}

	private Map<String, List<String>> groupBy(final Queue<String> words) {
		final Map<String, List<String>> map = new HashMap<>();
		for (String word : words) {
			var key = Character.toString(word.charAt(0)).toLowerCase();
			if (key.matches(NUMBER_KEY_REGEX)) {
				key = NUMBER_KEY_NAME;
			}
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<>());
			}
			map.get(key).add(words.poll());
		}
		return unmodifiableCollections(map);
	}

	private Map<String, List<String>> unmodifiableCollections(final Map<String, List<String>> map) {
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			entry.setValue(Collections.unmodifiableList(entry.getValue()));
		}
		return Collections.unmodifiableMap(map);
	}
}
