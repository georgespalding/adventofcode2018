package com.github.georgespalding.adventofcode.ten;

import static java.util.stream.Collectors.toList;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.List;

public class DayTen {

   static final boolean debug = false;

   public static void main(String[] args) {
      final Day<Object, Object> dayNine = new Day<>();
      List<MovingPoint> movingPoints = Util.streamResource("10.lst")
         .map(MovingPoint::new)
         .collect(toList());
      dayNine.start();

      dayNine.partOne("TODO Part1");
      dayNine.partTwo("TODO Part2");

      dayNine.output();
   }

}
