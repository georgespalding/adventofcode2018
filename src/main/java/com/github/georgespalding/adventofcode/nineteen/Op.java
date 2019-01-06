package com.github.georgespalding.adventofcode.nineteen;

import static java.lang.Math.toIntExact;

enum Op {
   addr {
      /**
       * (add register) stores into register C the result of adding register A and register B.
       */
      @Override
      void apply(Register reg, long regA, long regB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) + reg.get(regB));
      }

      @Override
      String debug(Register reg, long regA, long regB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " + " + reg.regName(regB);
      }

      @Override
      String debug(long regA, long regB, long regC) {
         return debugRR("+", regA, regB, regC);
      }
   },
   addi {
      /**
       * (add immediate) stores into register C the result of adding register A and value B.
       */
      @Override
      void apply(Register reg, long regA, long valB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) + valB);
      }

      @Override
      String debug(Register reg, long regA, long valB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " + " + valB;
      }

      @Override
      String debug(long regA, long valB, long regC) {
         return debugRI("+", regA, valB, regC);
      }
   },
   mulr {
      /**
       * (multiply register) stores into register C the result of multiplying register A and register B.
       */
      @Override
      void apply(Register reg, long regA, long regB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) * reg.get(regB));
      }

      @Override
      String debug(Register reg, long regA, long regB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " * " + reg.regName(regB);
      }

      @Override
      String debug(long regA, long regB, long regC) {
         return debugRR("*", regA, regB, regC);
      }
   },
   muli {
      /**
       * (multiply immediate) stores into register C the result of multiplying register A and value B.
       */
      @Override
      void apply(Register reg, long regA, long valB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) * valB);
      }

      @Override
      String debug(Register reg, long regA, long valB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " * " + valB;
      }

      @Override
      String debug(long regA, long valB, long regC) {
         return debugRI("*", regA, valB, regC);
      }
   },
   banr {
      /**
       * (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
       */
      @Override
      void apply(Register reg, long regA, long regB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) & reg.get(regB));
      }

      @Override
      String debug(Register reg, long regA, long regB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " & " + reg.regName(regB);
      }

      @Override
      String debug(long regA, long regB, long regC) {
         return debugRR("&", regA, regB, regC);
      }
   },
   bani {
      /**
       * (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
       */
      @Override
      void apply(Register reg, long regA, long valB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) & valB);
      }

      @Override
      String debug(Register reg, long regA, long valB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " X " + valB;
      }

      @Override
      String debug(long regA, long valB, long regC) {
         return debugRI("&", regA, valB, regC);
      }
   },
   borr {
      /**
       * (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
       */
      @Override
      void apply(Register reg, long regA, long regB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) | reg.get(regB));
      }

      @Override
      String debug(Register reg, long regA, long regB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " | " + reg.regName(regB);
      }

      @Override
      String debug(long regA, long regB, long regC) {
         return debugRR("|", regA, regB, regC);
      }
   },
   bori {
      /**
       * (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.
       */
      @Override
      void apply(Register reg, long regA, long valB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) | valB);
      }

      @Override
      String debug(Register reg, long regA, long valB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " | " + valB;
      }

      @Override
      String debug(long regA, long valB, long regC) {
         return debugRI("|", regA, valB, regC);
      }
   },
   setr {
      /**
       * (set register) copies the contents of register A into register C. (Input B is ignored.)
       */
      @Override
      void apply(Register reg, long regA, long ignored, long regC) {
         reg.set(toIntExact(regC), reg.get(regA));
      }

      @Override
      String debug(Register reg, long regA, long ignored, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA);
      }

      @Override
      String debug(long regA, long ignored, long regC) {
         return Register.name(regC) + " = " + Register.name(regA) + ";";
      }
   },
   seti {
      /**
       *  (set immediate) stores value A into register C. (Input B is ignored.)
       */
      @Override
      void apply(Register reg, long valA, long ignored, long regC) {
         reg.set(toIntExact(regC), valA);
      }

      @Override
      String debug(Register reg, long valA, long ignored, long regC) {
         return reg.regName(regC) + "=" + valA;
      }

      @Override
      String debug(long valA, long ignored, long regC) {
         return Register.name(regC) + " = " + valA + ";";
      }
   },
   gtir {
      /**
       * (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, long valA, long regB, long regC) {
         reg.set(toIntExact(regC), valA > reg.get(regB) ? 1 : 0);
      }

      @Override
      String debug(Register reg, long valA, long regB, long regC) {
         return reg.regName(regC) + "=" + valA + " > " + reg.regName(regB);
      }

      @Override
      String debug(long valA, long regB, long regC) {
         return debugBool(">", "" + valA, Register.name(regB), regC);
      }
   },
   gtri {
      /**
       * (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, long regA, long valB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) > valB ? 1 : 0);
      }

      @Override
      String debug(Register reg, long regA, long valB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " > " + valB;
      }

      @Override
      String debug(long regA, long valB, long regC) {
         return debugBool(">", Register.name(regA), "" + valB, regC);
      }
   },
   gtrr {
      /**
       * (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, long regA, long regB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) > reg.get(regB) ? 1 : 0);
      }

      @Override
      String debug(Register reg, long regA, long regB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " > " + reg.regName(regB);
      }

      @Override
      String debug(long regA, long regB, long regC) {
         return debugBool(">", Register.name(regA), Register.name(regB), regC);
      }
   },
   eqir {
      /**
       * (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, long valA, long regB, long regC) {
         reg.set(toIntExact(regC), valA == reg.get(regB) ? 1 : 0);
      }

      @Override
      String debug(Register reg, long valA, long regB, long regC) {
         return reg.regName(regC) + "=" + valA + " == " + reg.regName(regB);
      }

      @Override
      String debug(long valA, long regB, long regC) {
         return debugBool("==", "" + valA, Register.name(regB), regC);
      }
   },
   eqri {
      /**
       * (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, long regA, long valB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) == valB ? 1 : 0);
      }

      @Override
      String debug(Register reg, long regA, long valB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " == " + valB;
      }

      @Override
      String debug(long regA, long valB, long regC) {
         return debugBool("==", Register.name(regA), "" + valB, regC);
      }
   },
   eqrr {
      /**
       * (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, long regA, long regB, long regC) {
         reg.set(toIntExact(regC), reg.get(regA) == reg.get(regB) ? 1 : 0);
      }

      @Override
      String debug(Register reg, long regA, long regB, long regC) {
         return reg.regName(regC) + "=" + reg.regName(regA) + " == " + reg.regName(regB);
      }

      @Override
      String debug(long regA, long regB, long regC) {
         return debugBool("==", Register.name(regA), Register.name(regB), regC);
      }
   },
   ;

   abstract String debug(Register reg, long regA, long valB, long regC);

   abstract String debug(long A, long B, long C);

   protected String debugRI(String symbol, long regA, long valB, long regC) {
      if (regA == regC) {
         return Register.name(regC) + " " + symbol + "= " + valB + ";";
      } else {
         return Register.name(regC) + "=" + Register.name(regA) + " " + symbol + " " + valB + ";";
      }
   }

   protected String debugRR(String symbol, long regA, long regB, long regC) {
      if (regA == regC) {
         return Register.name(regC) + " " + symbol + "= " + Register.name(regB) + ";";
      } else if (regB == regC) {
         return Register.name(regC) + " " + symbol + "= " + Register.name(regA) + ";";
      } else {
         return Register.name(regC) + " = " + Register.name(regA) + " " + symbol + " " + Register.name(regB) + ";";
      }
   }

   protected String debugBool(String symbol, String A, String B, long regC) {
      return Register.name(regC) + " = " + A + " " + symbol + " " + B + " ? 1 : 0;";
   }

   abstract void apply(Register reg, long A, long B, long C);
}
