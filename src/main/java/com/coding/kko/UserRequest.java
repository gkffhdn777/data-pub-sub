package com.coding.kko;

import java.util.Objects;

public final class UserRequest {

	private final String fileName;

	private final String directoryPath;

	private final int partition;

	public UserRequest(final String fileName, final String directoryPath, final int partition) {
		Objects.requireNonNull(fileName, "FileName cannot be null.");
		Objects.requireNonNull(directoryPath, "DirectoryPath cannot be null.");
		this.fileName = fileName;
		this.directoryPath = directoryPath;
		this.partition = verifyPartitionSize(partition);
	}

	private int verifyPartitionSize(final int partition) {
		if ((partition >= 1) && (partition <= 28)) {
			return partition;
		} else {
			throw new IllegalArgumentException("Partition size : (1 < N < 28)");
		}
	}

	public String getFileName() {
		return fileName;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public int getPartition() {
		return partition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserRequest that = (UserRequest) o;
		return Objects.equals(fileName, that.fileName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileName);
	}
}
