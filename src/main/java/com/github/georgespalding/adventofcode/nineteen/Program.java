package com.github.georgespalding.adventofcode.nineteen;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;

import com.github.georgespalding.adventofcode.LineParser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class Program {

   private final Instruction[] instructions;
   private int ipReg;
   private long ipCtr;
   private long numSteps;

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

   void execDebug(Register reg) {
      long hit3 = 0;
      long first3 = -2;
      long[] first = null;
      while (ipCtr >= 0 && ipCtr < instructions.length) {
         final Instruction instruction = instructions[Math.toIntExact(ipCtr)];
         reg.loadCtr(ipCtr);

         final long ipCtrAtStart = ipCtr;
         final String regBefore = reg.toString();
         if (ipCtrAtStart == 11 && ++hit3 > 10) {
            if (first3 == -2) {
               first3++;
            } else if (first3 == -1) {
               first3 = numSteps;
               first = reg.copyState();
            } else {
               final long second3 = numSteps;
               final long[] second = reg.copyState();
               long cycle = second3 - first3;
               long[] diff = new long[second.length];
               for (int i = 0; i < second.length; i++) {
                  diff[i] = second[i] - first[i];
               }
               out.println(LongStream.of(diff).boxed().collect(toList()));

               first3 = -1;
               List<Integer> diffIndexes = IntStream.range(0, 6)
                  .filter(i -> diff[i] != 0)
                  .boxed()
                  .collect(toList());

               if (diffIndexes.size() == 1) {
                  Integer diffIndex = diffIndexes.get(0);
                  final long diffReg = reg.get(diffIndex);
                  long delta = diff[diffIndex];
                  if (delta != 1 || diffIndex != 4) {
                     out.println(">>>>>>>> delta:" + delta + " diffIndex: " + diffIndex);
                  }
                  //TODO dont hardcode;
//                  long skipAheadCycles = (reg.get(5) - diffReg - 1) / delta;

                  //numSteps += cycle * skipAheadCycles;
//                  out.println(format("<ip=%d %s %s %s", ipCtrAtStart, regBefore, instruction, reg));
//                  out.println("Cycle: " + cycle + " skip ahead:" + skipAheadCycles + " cycles");
                  reg.set(2, reg.get(5) );
                  reg.set(4, reg.get(5) );
               }
            }
         }

         instruction.apply(reg);
         ipCtr = reg.fetchCtr();
         ipCtr++;
         numSteps++;
         //         if (numSteps < 1000)
         {
            out.println(format("ip=%d %s %s %s", ipCtrAtStart, regBefore, instruction, reg));
            //         } else {
            //            out.println();
         }
      }
      out.println("Program end after " + numSteps);
   }

   void execute(Register reg) {
      while (ipCtr >= 0 && ipCtr < instructions.length) {
         final Instruction instruction = instructions[Math.toIntExact(ipCtr)];
         final String dbg = format(" ip=%02d after %s", ipCtr, instruction.dbg(reg));
         reg.loadCtr(ipCtr);
         instruction.apply(reg);
         ipCtr = reg.fetchCtr();
         ipCtr++;
         numSteps++;
         if(instruction.C==0)
         out.println(reg.toString() + dbg);
      }
      out.println("Program end after " + numSteps);
   }

   boolean isInstructionPointer(Instruction instruction) {
      return instruction.C == ipReg;
   }

   Register createRegister() {
      return new Register(ipReg);
   }

   void reset() {
      ipCtr = 0;
      numSteps = 0;
   }

   @Override
   public String toString() {
      AtomicInteger l = new AtomicInteger();
      return "Program ipReg=" + ipReg +
         ", instructions=\n" + Arrays
         .stream(instructions)
         .map(Instruction::dbg)
         .map(s -> String.format("case %d: %s return ip;", l.getAndIncrement(), s))
         .collect(Collectors.joining("\n"));
   }
}
