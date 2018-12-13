package com.github.georgespalding.adventofcode.twelve;

import static com.github.georgespalding.adventofcode.Util.streamResource;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

import com.github.georgespalding.adventofcode.template.Day;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.LongStream;

public class DayTwelve {

   static final boolean debug = false;

   public static void main(String[] args) {
      final Day<Long, BigInteger> dayTwelve = new Day<>();
      final List<String> lines = streamResource("12.lst").collect(toList());
      final BinaryThing thing = new BinaryThing(
         lines.stream()
            .filter(s -> s.startsWith("initial state: "))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("initial state not found")),
         lines.stream().filter(l -> l.contains(" => ")));

      dayTwelve.start();
      if (debug) out.println(thing.header());

      if (debug) out.printf("%2d: %s\n", 0, thing.toString());
      rangeClosed(1, 20)
         .forEach(i -> {
            thing.generation();
            if (debug) out.printf("%2d: %s\n", i, thing.toString());
         });
      dayTwelve.partOne(thing.potNumSum());

      LongStream.rangeClosed(21L, 100)
         .forEach(i -> {
            thing.generation();
            if (debug) out.printf("%2d: %s\n", i, thing.toString());
         });

      dayTwelve.partTwo(thing.hyperJumpAt100());

      dayTwelve.output();
   }
}
