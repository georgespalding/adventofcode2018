package com.github.georgespalding.adventofcode.template;

public class DaySample {

   static final boolean debug = false;

   public static void main(String[] args) {
      final Day<Object, Object> day = new Day<>();
      // TODO Load/parse data
      day.start();

      day.partOne("TODO Part1");
      day.partTwo("TODO Part2");

      day.output();
   }
}
