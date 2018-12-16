package com.github.georgespalding.adventofcode.fourteen;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import com.github.georgespalding.adventofcode.template.Day;

import java.util.stream.IntStream;

public class DayFourteen {

   static final boolean debug = false;

   public static void main(String[] args) {
      final int limit = 825401;
      final int total = limit + 10;
      final Day<Object, Object> dayNine = new Day<>();
      int[] recipes = new int[total + 2];
      int elf1 = 0;
      int elf2 = 1;
      recipes[elf1] = 3;
      recipes[elf2] = 7;
      int recipeCount = 2;

      dayNine.start();

      while (recipeCount <= total) {
         if (debug) {
            printSitu(elf1, elf2, recipes, recipeCount);
         }
         final int recipeElf1 = recipes[elf1];
         final int recipeElf2 = recipes[elf2];
         int sum = recipeElf1 + recipeElf2;
         if (sum > 9) {
            recipes[recipeCount++] = 1;
            recipes[recipeCount++] = sum % 10;
         } else {
            recipes[recipeCount++] = sum;
         }
         elf1 = (elf1 + 1 + recipeElf1) % recipeCount;
         elf2 = (elf2 + 1 + recipeElf2) % recipeCount;
      }

      dayNine.partOne(stream(pickTenAfter(recipes, limit))
         .boxed()
         .map(Object::toString)
         .collect(joining()));
      dayNine.partTwo("TODO Part2");

      dayNine.output();
   }

   private static int[] pickTenAfter(int[] recipes, int pos) {
      return IntStream.range(pos, 10 + pos)
         .map(i -> recipes[i])
         .toArray();
   }

   private static void printSitu(int elf1, int elf2, int[] recipes, int recipeCount) {
      final StringBuilder sb = new StringBuilder();
      for (int i = 0; i < recipeCount; i++) {
         if (elf1 == i) {
            sb.append("(").append(recipes[i]).append(")");
         } else if (elf2 == i) {
            sb.append("[").append(recipes[i]).append("]");
         } else {
            sb.append(" ").append(recipes[i]).append(" ");
         }
      }
      System.out.println(sb);
   }

}
