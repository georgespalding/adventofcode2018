package com.github.georgespalding.adventofcode.eighteen;

import static com.github.georgespalding.adventofcode.Pair.fromEntry;
import static java.lang.System.out;

import com.github.georgespalding.adventofcode.Pair;
import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.HashMap;
import java.util.Map;

public class Day18 {

   static final boolean debug = true;

   public static void main(String[] args) {
      final Day<Pair<Long, Integer>, Pair<Long, Integer>> day = new Day<>();
      final Area area = new Area(Util.streamResource("18/18.lst"));
      out.println(area.toString());
      day.start();
      while (area.minute() < 10) {
         area.nextMinuteFaster();
         if (debug) {
            out.println("After " + area.minute()
               + " minute" + (area.minute() == 0 ? "" : "s")
               + " value: " + (area.count(Use.wooded) * area.count(Use.lumberyard)));
            out.println(area.toString());
         }
      }
      day.partOne(fromEntry(area.count(Use.wooded) * area.count(Use.lumberyard), area.minute()));

      final int END = 1000_000_000;
      final Map<Long, Integer> minuteBySeen = new HashMap<>();
      boolean hyperJumpDone = false;
      while (area.minute() < END) {
         Long res = area.nextMinuteFaster();
         if (!hyperJumpDone && minuteBySeen.containsKey(res)) {
            final int currMinute = area.minute();
            final int cycle = currMinute - minuteBySeen.get(res);
            final int cyclesAhead = (END - currMinute) / cycle;
            area.minute(currMinute + cycle * cyclesAhead);
            hyperJumpDone = true;
            out.println("Minute: " + currMinute + " Cycle: " + cycle + " Skip ahead " + cyclesAhead + " to " + area.minute());
         } else {
            minuteBySeen.put(res, area.minute());
         }
      }
      day.partTwo(fromEntry(area.count(Use.wooded) * area.count(Use.lumberyard), area.minute()));

      day.output();
   }
}
