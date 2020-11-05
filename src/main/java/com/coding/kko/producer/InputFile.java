package com.coding.kko.producer;

import java.util.List;

public interface InputFile<T> {
	List<T> readFile(final T file);
}
