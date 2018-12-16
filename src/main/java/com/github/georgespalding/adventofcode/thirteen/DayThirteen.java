package com.github.georgespalding.adventofcode.thirteen;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.List;

public class DayThirteen {

   public static void main(String[] args) {
      final Day<Object, Object> dayNine = new Day<>();

      final List<MineCart> carts = TrackParser.parse(Util.streamResource("13.lst"));
      carts.stream().sorted().map(MineCart::desc).forEach(System.out::println);
      dayNine.start();

      dayNine.partOne("TODO Part1");
      dayNine.partTwo("TODO Part2");

      dayNine.output();
   }
}
