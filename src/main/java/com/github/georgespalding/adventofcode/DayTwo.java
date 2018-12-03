package com.github.georgespalding.adventofcode;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DayTwo {

   private static final int[] letterCount = new int['z' - 'a' + 1];
   private static final String[] boxIds = Util.streamResource("2.lst").toArray(String[]::new);

   public static void main(String[] args) {
      final long start = currentTimeMillis();

      final OptionalInt checkSum = stream(boxIds)
         .flatMap(DayTwo::countDoubleAndTrippleDigits)
         .collect(Collectors.toMap(
            identity(),
            i -> 1,
            (a, b) -> a + b)).values().stream()
         .mapToInt(Integer::intValue)
         .reduce((a, b) -> a * b);

      final long mid = currentTimeMillis();

      final Optional<String> bestMatch = stream(boxIds)
         .flatMap(boxId ->
            stream(boxIds)
               .filter(otherId -> !boxId.equals(otherId))
               .map(otherId -> sameChars(boxId.toCharArray(), otherId.toCharArray())))
         .max(Comparator.comparingInt(String::length));
      final long end = currentTimeMillis();

      out.printf("Ans1: %s (%d ms)\n", checkSum, mid - start);
      out.printf("Ans2: %s (%d ms)\n", bestMatch, end - mid);
      out.printf("Total (%d ms)\n", end - start);
   }

   public static Stream<Integer> countDoubleAndTrippleDigits(String boxId) {
      Arrays.fill(letterCount, 0);
      for (char c : boxId.toCharArray()) {
         letterCount[c - 'a']++;
      }

      return IntStream.of(letterCount)
         .filter(i -> i > 1 && i < 4)
         .sorted()
         .distinct()
         .boxed();
   }

   public static String sameChars(char[] boxId1, char[] boxId2) {
      final int maxFail=2;
      final int length = boxId1.length;
      assert length == boxId2.length;
      StringBuilder same = new StringBuilder();
      for (int i = 0; i < length; i++) {
         final char boxId1i = boxId1[i];
         if (boxId1i == boxId2[i]) {
            same.append(boxId1i);
         } else if(same.length()+maxFail < i){
            break;
         }
      }
      return same.toString();
   }

}
