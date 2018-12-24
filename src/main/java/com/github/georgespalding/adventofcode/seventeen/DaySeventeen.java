package com.github.georgespalding.adventofcode.seventeen;

import static com.github.georgespalding.adventofcode.seventeen.GeoMapParser.parse;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

public class DaySeventeen {

   static final boolean debug = true;

   public static void main(String[] args) {
      final Day<Object, Object> day = new Day<>();
      final GeoMap map = parse(Util.streamResource("17/17.lst"));

      day.start();
      map.run();
      long[] res= map.countChars(new char[]{'|','~'});
      day.partOne(res[0]+res[1]);

      day.partTwo(res[1]);

      day.output();
   }
}
