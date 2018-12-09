package com.github.georgespalding.adventofcode.template;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

public class Day<A1, A2> {

   A1 ans1;
   A2 ans2;
   long load = currentTimeMillis();
   long loadMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
   long start;
   long startMem;
   long mid;
   long midMem;
   long end;
   long endMem;

   public void start() {
      start = currentTimeMillis();
      startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
   }

   public void partOne(A1 ans1) {
      this.ans1 = ans1;
      mid = currentTimeMillis();
      midMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
   }

   public void partTwo(A2 ans2) {
      this.ans2 = ans2;
      end = currentTimeMillis();
      endMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
   }

   public void output() {
      out.printf("Load: (%d ms) %d b\n", start - load, startMem - loadMem);
      out.printf("Ans1: %s (%d ms) %d b\n", ans1, mid - start, midMem - startMem);
      out.printf("Ans2: %s (%d ms) %d b\n", ans2, end - mid, endMem - midMem);
      out.printf("Total (%d ms) %d b\n", end - start, endMem - startMem);
   }
}
