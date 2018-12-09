package com.github.georgespalding.adventofcode.nine;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

public class DayNine {

   static final boolean debug = false;

   public static void main(String[] args) {
      final long load = currentTimeMillis();
      final Game game=new Game(427);
      final long start = currentTimeMillis();
      final long ans1= game.getWinnerAtMarble(70723).getAsLong();
      final long mid = currentTimeMillis();
      final long ans2 = game.getWinnerAtMarble(99 * 70723).getAsLong();
      final long end = currentTimeMillis();

      out.printf("Load: (%d ms)\n", start - load);
      out.printf("Ans1: %s (%d ms)\n", ans1, mid - start);
      out.printf("Ans2: %s (%d ms)\n", ans2, end - mid);
      out.printf("Total (%d ms)\n", end - start);

   }
}
