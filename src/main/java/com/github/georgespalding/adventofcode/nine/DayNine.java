package com.github.georgespalding.adventofcode.nine;

import com.github.georgespalding.adventofcode.template.Day;

public class DayNine {

   static final boolean debug = false;

   public static void main(String[] args) {
      final Day<Long, Long> dayNine = new Day<>();
      final Game game = new Game(427, 100 * 70723);
      dayNine.start();
      dayNine.partOne(game.getWinnerAtMarble(70723).getAsLong());
      dayNine.partTwo(game.getWinnerAtMarble(99 * 70723).getAsLong());

      dayNine.output();
   }
}
