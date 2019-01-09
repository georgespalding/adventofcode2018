package com.github.georgespalding.adventofcode.twentyone;

import static java.lang.String.format;

public class Instruction {

   final long A, B, C;
   private final Op op;

   Instruction(Op op, long a, long b, long c) {
      this.op = op;
      A = a;
      B = b;
      C = c;
   }

   void apply(Register reg) {
      op.apply(reg, A, B, C);
   }

   @Override
   public String toString() {
      return format("%s %d %d %d", op, A, B, C);
   }

   String dbg(int ipReg, int index) {
      switch (op) {
         case addr:
            return debugRR(ipReg, index, "+");
         case addi:
            return debugRI(ipReg, index, "+");
         case mulr:
            return debugRR(ipReg, index, "*");
         case muli:
            return debugRI(ipReg, index, "*");
         case banr:
            return debugRR(ipReg, index, "&");
         case bani:
            return debugRI(ipReg, index, "&");
         case borr:
            return debugRR(ipReg, index, "|");
         case bori:
            return debugRI(ipReg, index, "|");
         case setr:
            if (ipReg == C) {
               return "return " + Register.name(A) + ";";
            } else {
               return Register.name(C) + " = " + Register.name(A) + ";";
            }
         case seti:
            if (ipReg == C) {
               return "return " + A + ";";
            } else {
               return Register.name(C) + " = " + A + ";";
            }
         case gtir:
            return debugBool(ipReg, ">", "" + A, ipReg == B ? index + "" : Register.name(B));
         case gtri:
            return debugBool(ipReg, ">", ipReg == A ? index + "" : Register.name(A), "" + B);
         case gtrr:
            return debugBool(
               ipReg,
               ">",
               ipReg == A ? index + "" : Register.name(A),
               ipReg == B ? index + "" : Register.name(B));
         case eqir:
            return debugBool(ipReg, "==", "" + A, ipReg == B ? index + "" : Register.name(B));
         case eqri:
            return debugBool(ipReg, "==", ipReg == A ? index + "" : Register.name(A), "" + B);
         case eqrr:
            return debugBool(
               ipReg,
               "==",
               ipReg == A ? index + "" : Register.name(A),
               ipReg == B ? index + "" : Register.name(B));
         default:
            throw new AssertionError("Unkown op:" + op);
      }
   }

   private String debugRI(int ipReg, int index, String symbol) {
      if (ipReg == C) {
         if(ipReg==A){
            switch (symbol) {
               case "+":
                  return "return " + (index + B) + ";";
               case "*":
                  return "return " + (index * B) + ";";
               default:
                  return "return " + index + " " + symbol + " " + B + ";";
            }
         }else {
            return "return " + Register.name(A) + " " + symbol + " " + B + ";";
         }
      }
      if (A == C) {
         return Register.name(C) + " " + symbol + "= " + B + ";";
      } else {
         String a = ipReg == A ? index + "" : Register.name(A);
         return Register.name(C) + "=" + a + " " + symbol + " " + B + ";";
      }
   }

   private String debugRR(int ipReg, int index, String symbol) {
      String a = ipReg == A ? index + "" : Register.name(A);
      String b = ipReg == B ? index + "" : Register.name(B);
      if (ipReg == this.C) {
         return "return " + a + " " + symbol + " " + b + ";";
      }
      if (A == C) {
         return Register.name(C) + " " + symbol + "= " + b + ";";
      } else if (B == C) {
         return Register.name(C) + " " + symbol + "= " + a + ";";
      } else {
         return Register.name(C) + " = " + a + " " + symbol + " " + b + ";";
      }
   }

   private String debugBool(int ipReg, String symbol, String a, String b) {
      if (ipReg == C) {
         return "return " + a + " " + symbol + " " + b + " ? 1 : 0;";
      } else {
         return Register.name(C) + " = " + a + " " + symbol + " " + b + " ? 1 : 0;";
      }
   }
}
