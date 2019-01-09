package com.github.georgespalding.adventofcode.twentyone;

import static java.util.stream.Stream.empty;

import com.github.georgespalding.adventofcode.LineParser;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Program {

   private final Instruction[] instructions;
   private int ipReg;

   Program(Stream<String> input) {
      this.instructions = input
         .flatMap(this::parse)
         .toArray(Instruction[]::new);
   }

   private Stream<Instruction> parse(String line) {
      final LineParser lp = new LineParser(line);
      if (line.trim().startsWith("#ip ")) {
         lp.next(' ');
         ipReg = lp.nextInt('<');
         return empty();
      } else {
         return Stream.of(new Instruction(
            Op.valueOf(lp.next(' ')),
            lp.nextLong(' '),
            lp.nextLong(' '),
            lp.nextLong('<')));
      }
   }

   @Override
   public String toString() {
      AtomicInteger l = new AtomicInteger();
      return "Program ipReg=" + ipReg +
         ", instructions=\n" + Arrays
         .stream(instructions)
         .map(instruction -> instruction.dbg(ipReg, l.get()) + " // " + instruction)
         .map(s -> String.format("case %d: %s", l.getAndIncrement(), s))
         .collect(Collectors.joining("\n"));
   }
}
