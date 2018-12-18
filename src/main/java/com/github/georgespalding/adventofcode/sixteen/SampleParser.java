package com.github.georgespalding.adventofcode.sixteen;

import com.github.georgespalding.adventofcode.LineParser;

import java.util.ArrayList;
import java.util.List;

class SampleParser {

   final List<Sample> samples = new ArrayList<>();
   final List<int[]> programSteps = new ArrayList<>();
   private Register before;
   private Integer opcode;
   private int A, B, C;
   private Register after;
   private int numNewLines = 0;
   private boolean phaseII = false;

   /*
   Before: [2, 0, 3, 3]
   0 2 0 0
   After:  [2, 0, 3, 3]
   */
   void accept(String line) {

      if (line.trim().isEmpty()) {
         numNewLines++;
         if (numNewLines > 2) {
            phaseII = true;
         }
         return;
      } else {
         numNewLines = 0;
      }
      final LineParser lp = new LineParser(line);
      if (!phaseII) {
         if (before == null) {
            before = parseRegister(lp);
         } else if (opcode == null) {
            opcode = lp.nextInt(' ');
            A = lp.nextInt(' ');
            B = lp.nextInt(' ');
            C = lp.nextInt('<');
         } else if (after == null) {
            after = parseRegister(lp);
            samples.add(new Sample(before, after, opcode, A, B, C));
            before = null;
            after = null;
            opcode = null;
         }
      } else {
         programSteps.add(parseProgramStep(lp));
      }
   }

   private int[] parseProgramStep(LineParser lp) {
      int[] ret = new int[4];
      ret[0] = lp.nextInt(' ');
      ret[1] = lp.nextInt(' ');
      ret[2] = lp.nextInt(' ');
      ret[3] = lp.nextInt(' ');
      return ret;
   }

   private Register parseRegister(LineParser lp) {
      lp.next('[');
      Register reg = new Register();
      reg.set(0, lp.nextInt(','));
      reg.set(1, lp.nextInt(','));
      reg.set(2, lp.nextInt(','));
      reg.set(3, lp.nextInt(']'));
      return reg;
   }

}
