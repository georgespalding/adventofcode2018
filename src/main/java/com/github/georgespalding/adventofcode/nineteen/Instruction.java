package com.github.georgespalding.adventofcode.nineteen;

import static java.lang.String.format;

public class Instruction {

   final Op op;
   final long A, B, C;

   Instruction(Op op, long a, long b, long c) {
      this.op = op;
      A = a;
      B = b;
      C = c;
   }

   String dbg(Register reg){
      return op.debug(reg, A, B, C);
   }
   void apply(Register reg) {
      op.apply(reg, A, B, C);
   }

   @Override
   public String toString() {
      return format("%s %d %d %d", op, A, B, C);
   }

   String dbg(){
      return op.debug(A,B,C);
   }
}
