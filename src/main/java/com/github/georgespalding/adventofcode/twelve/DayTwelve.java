package com.github.georgespalding.adventofcode.twelve;

import static com.github.georgespalding.adventofcode.Util.streamResource;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.github.georgespalding.adventofcode.template.Day;

import java.util.List;

public class DayTwelve {

   static final boolean debug = false;

   public static void main(String[] args) {
      final Day<Long, String> dayNine = new Day<>();
      final int iterations = 20;
      final List<String> lines = streamResource("12.lst").collect(toList());
      final BinaryThing thing = new BinaryThing(
         lines.stream()
            .filter(s -> s.startsWith("initial state: "))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("initial state not found")),
         iterations + 2,
         lines.stream().filter(l -> l.contains(" => ")));

      dayNine.start();
      if (debug) out.println(thing.header());

      if (debug) out.printf("%2d: %s\n", 0, thing.toString());
      range(0, iterations)
         .forEach(i -> {
            thing.generation();
            if (debug) out.printf("%2d: %s\n", i, thing.toString());
         });

      dayNine.partOne(thing.potNumSum());
      dayNine.partTwo("TODO Part2");

      dayNine.output();
   }
}
