package com.github.georgespalding.adventofcode.sixteen;

import static com.github.georgespalding.adventofcode.Pair.fromEntry;
import static java.util.Arrays.stream;
import static java.util.EnumSet.noneOf;
import static java.util.stream.Collectors.toCollection;

import com.github.georgespalding.adventofcode.Pair;

import java.util.EnumSet;

class Sample {

   private final Register before;
   private final Register after;
   private final int opcode;
   private final int A, B, C;

   Sample(Register before, Register after, int opcode, int a, int b, int c) {
      this.before = before;
      this.after = after;
      this.opcode = opcode;
      A = a;
      B = b;
      C = c;
   }

   long countMatchingOps() {
      final Register reg = new Register();

      return stream(Op.values())
         .filter(op -> this.matches(op, reg))
         .count();
   }

   Pair<Integer, EnumSet<Op>> matchingOps() {
      final Register reg = new Register();

      return fromEntry(opcode, stream(Op.values())
         .filter(op -> this.matches(op, reg))
         .collect(toCollection(() -> noneOf(Op.class))));
   }

   private boolean matches(Op op, Register reg) {
      reg.load(before);
      op.apply(reg, A, B, C);
      return reg.equals(after);
   }

   public int getOpcode() {
      return opcode;
   }
}
