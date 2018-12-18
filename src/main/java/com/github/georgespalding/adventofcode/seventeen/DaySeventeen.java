package com.github.georgespalding.adventofcode.seventeen;

import static com.github.georgespalding.adventofcode.seventeen.GeoMapParser.parse;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

public class DaySeventeen {

   static final boolean debug = false;

   public static void main(String[] args) {
      final Day<Object, Object> day = new Day<>();
      final GeoMap map = parse(Util.streamResource("17/17.lst"));
      System.out.println(map);
      day.start();

      

      day.partOne("TODO Part1");
      day.partTwo("TODO Part2");

      day.output();
   }
}
