package com.example.kafkadistributer.util.progressprinter;
public interface ProgressPrinter {

   void printProgress(long startTime, long total, long current);
}
