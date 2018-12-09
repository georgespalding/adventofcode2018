package com.github.georgespalding.adventofcode.nine;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

public class DayNine {

   static final boolean debug = false;

   public static void main(String[] args) {
      final long load = currentTimeMillis();
      final Game game = new Game(427, 100 * 70723);
      final long start = currentTimeMillis();
      final long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
      final long ans1= game.getWinnerAtMarble(70723).getAsLong();
      final long mid = currentTimeMillis();
      final long midMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
      final long ans2 = game.getWinnerAtMarble(99 * 70723).getAsLong();
      final long end = currentTimeMillis();
      final long endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

      out.printf("Load: (%d ms)\n", start - load);
      out.printf("Ans1: %s (%d ms) %d b\n", ans1, mid - start, midMem - startMem);
      out.printf("Ans2: %s (%d ms) %d b\n", ans2, end - mid, endMem - midMem);
      out.printf("Total (%d ms) %d b\n", end - start, endMem - startMem);

   }
}
