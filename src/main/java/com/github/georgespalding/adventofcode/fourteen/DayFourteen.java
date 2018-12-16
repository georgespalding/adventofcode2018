package com.github.georgespalding.adventofcode.fourteen;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import com.github.georgespalding.adventofcode.template.Day;

public class DayFourteen {

   private static final boolean debug = false;
   private static final int limit = 825401;
   private static final int[] search = new int[] { 8, 2, 5, 4, 0, 1 };
   //      private static final int limit = 51589;
   //      private static final int[] search = new int[] { 5, 1, 5, 8, 9 };
   //      private static final int limit = 18;
   //      private static final int[] search = new int[] { 9,2,5,1,0 };
   private static final int total = (20207075 + 10);
   private static final int[] recipes = new int[total + 2];

   public static void main(String[] args) {
      final Day<String, Integer> dayNine = new Day<>();
      int elf1 = 0;
      int elf2 = 1;
      recipes[elf1] = 3;
      recipes[elf2] = 7;
      int recipeCount = 2;

      dayNine.start();

      int searchPos = 0;
      while (recipeCount <= total) {
         if (debug) {
            printSitu(elf1, elf2, recipeCount);
         }
         final int recipeElf1 = recipes[elf1];
         final int recipeElf2 = recipes[elf2];
         int sum = recipeElf1 + recipeElf2;
         int digit = sum % 10;
         if (sum > 9) {
            recipes[recipeCount++] = 1;
            if (searchPos < search.length) {
               searchPos = search[searchPos] == 1
                  ? searchPos + 1
                  : 0;
               if (searchPos == search.length) {
                  dayNine.partTwo(recipeCount - search.length);
               }
            }
         }
         recipes[recipeCount++] = digit;
         if (searchPos < search.length) {
            searchPos = search[searchPos] == digit
               ? searchPos + 1
               : 0;
            if (searchPos == search.length) {
               dayNine.partTwo(recipeCount - search.length);
            }
         }
         if (recipeCount == limit+11) {
            dayNine.partOne(stream(stream(recipes, limit, 10 + limit)
               .toArray())
               .boxed()
               .map(Object::toString)
               .collect(joining()));
         }

         elf1 = (elf1 + 1 + recipeElf1) % recipeCount;
         elf2 = (elf2 + 1 + recipeElf2) % recipeCount;
      }

      dayNine.output();
   }

   private static void printSitu(int elf1, int elf2, int recipeCount) {
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
