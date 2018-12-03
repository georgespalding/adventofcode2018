package com.github.georgespalding.adventofcode;

import static java.nio.file.Files.lines;
import static java.util.stream.LongStream.iterate;

import java.util.HashSet;
import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

public class DayOne {

   private static final long[] data = readData();

   static LongStream streamData(long iteration) {
      System.out.println("iteration: " + iteration);
      return LongStream.of(data);
   }

   static long[] readData() {
      try {
         return Util.streamResource("1.lst")
            .mapToLong(Integer::parseInt).toArray();
      } catch (Exception e) {
         throw new RuntimeException("data read error:", e);
      }
   }

   public static void main(String[] args) {
      final HashSet<Long> seenFrequencies = new HashSet<>();
      final AtomicLong currentFrequency = new AtomicLong(0);
      seenFrequencies.add(currentFrequency.get());

      final long start = System.currentTimeMillis();

      final long frequency = streamData(0).sum();

      final long mid = System.currentTimeMillis();

      final OptionalLong repeat = iterate(0, i -> i + 1)
         .flatMap(DayOne::streamData)
         //.peek(frequencyChange-> System.out.println(frequencyChange+": "+(currentFrequency.get()+frequencyChange)))
         .map(currentFrequency::addAndGet)
         .filter(lastFrequency -> !seenFrequencies.add(lastFrequency))
         .findFirst();

      final long end = System.currentTimeMillis();

      System.out.printf("Ans1: %s (%d ms)\n",  frequency, mid-start);
      System.out.printf("Ans2: %s (%d ms)\n",  repeat, end-mid);
      System.out.printf("Total (%d ms)\n", end-start);
   }
}
