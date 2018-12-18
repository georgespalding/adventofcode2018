package com.github.georgespalding.adventofcode.sixteen;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import com.github.georgespalding.adventofcode.Pair;
import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DaySixteen {

   static final boolean debug = false;

   public static void main(String[] args) {
      final Day<Long, Object> day = new Day<>();
      final SampleParser pi = new SampleParser();
      Util.streamResource("16/16.lst")
         //.takeWhile(l -> pi.numNewLines < 2)
         .forEach(pi::accept);

      day.start();

      long problemsWithThreeOrMoreOpcodes = pi.samples.stream()
         .mapToLong(Sample::countMatchingOps)
         .filter(i -> i >= 3)
         .count();

      day.partOne(problemsWithThreeOrMoreOpcodes);

      // Antal problem per opcode
      final Map<Integer, Long> problemCountPerOpCode = pi.samples.stream()
         .collect(groupingBy(Sample::getOpcode, counting()));

      final Map<Integer, List<EnumSet<Op>>> possibleOpsPerOpCode = new HashMap<>();
      pi.samples.stream()
         .map(Sample::matchingOps)
         .forEach(p -> possibleOpsPerOpCode
            .computeIfAbsent(p.getKey(), i -> new ArrayList<>()).add(p.getVal()));

      final Op[] solutions = new Op[16];

      do {
         //Hitta Op som är ensam om att existera i alla lösningar for någon opcode;
         possibleOpsPerOpCode.entrySet().stream()
            .sorted(Comparator.comparingInt(e -> -e.getValue().size()))
            .map(e -> {
               final Integer opcode = e.getKey();
               final Long problemCount = problemCountPerOpCode.get(e.getKey());
               final Map<Op, Long> opMatchedProplems = e.getValue().stream().flatMap(Collection::stream)
                  .collect(groupingBy(Function.identity(), counting()));
               final List<Op> opsThatMatchedAllSamplesForOpCode = opMatchedProplems.entrySet().stream()
                  .filter(each -> each.getValue().equals(problemCount))
                  .map(Map.Entry::getKey)
                  .collect(Collectors.toList());
               return opsThatMatchedAllSamplesForOpCode.size() == 1
                  ? Optional.of(Pair.fromEntry(opcode, opsThatMatchedAllSamplesForOpCode.get(0)))
                  : Optional.<Pair<Integer, Op>>empty();
            })
            .flatMap(Optional::stream)
            .forEach(sol -> {
               solutions[sol.getKey()] = sol.getVal();
               possibleOpsPerOpCode.remove(sol.getKey());
               possibleOpsPerOpCode.values().forEach(l -> l.forEach(possibleOps -> possibleOps.remove(sol.getVal())));
            });
      } while (!possibleOpsPerOpCode.isEmpty());

      if (debug) {
         for (int i = 0; i < solutions.length; i++) {
            System.out.printf("%2d: %s\n", i, solutions[i]);
         }
      }

      final Register phaseIIRegister = new Register();
      pi.programSteps.forEach(prgStp ->
         solutions[prgStp[0]].apply(phaseIIRegister, prgStp[1], prgStp[2], prgStp[3]));

      day.partTwo(phaseIIRegister.get(0));

      day.output();
   }
}
