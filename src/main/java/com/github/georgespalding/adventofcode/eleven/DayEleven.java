package com.github.georgespalding.adventofcode.eleven;

import static java.lang.System.out;

import com.github.georgespalding.adventofcode.template.Day;

public class DayEleven {

   static final boolean debug = true;

   public static void main(String[] args) {
      final Day<Object, Object> dayNine = new Day<>();
      if (debug) {
         out.println(new Grid(8, 300, 300).getCell(3, 5));
         out.println(new Grid(57, 300, 300).getCell(122, 79));
         out.println(new Grid(39, 300, 300).getCell(217, 196));
         out.println(new Grid(71, 300, 300).getCell(101, 153));
         out.println(new Grid(18, 300, 300).findBestSquare(3));
         out.println(new Grid(42, 300, 300).findBestSquare(3));
      }
      Grid part1 = new Grid(8772, 300, 300);
      dayNine.start();

      dayNine.partOne(part1.findBestSquare(3));
      dayNine.partTwo(part1.findBestSquare());

      dayNine.output();
   }
}
