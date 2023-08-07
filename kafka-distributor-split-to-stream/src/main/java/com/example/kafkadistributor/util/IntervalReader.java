package com.example.kafkadistributor.util;
import java.nio.file.Path;
import java.util.List;

public interface IntervalReader<T> {
     List<? extends T> read(Path path, int leftIndex, int rightIndex);

}
