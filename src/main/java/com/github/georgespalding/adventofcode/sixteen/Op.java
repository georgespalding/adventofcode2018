package com.github.georgespalding.adventofcode.sixteen;

enum Op {
   addr {
      /**
       * (add register) stores into register C the result of adding register A and register B.
       */
      @Override
      void apply(Register reg, int regA, int regB, int regC) {
         reg.set(regC, reg.get(regA) + reg.get(regB));
      }
   },
   addi {
      /**
       * (add immediate) stores into register C the result of adding register A and value B.
       */
      @Override
      void apply(Register reg, int regA, int valB, int regC) {
         reg.set(regC, reg.get(regA) + valB);
      }
   },
   mulr {
      /**
       * (multiply register) stores into register C the result of multiplying register A and register B.
       */
      @Override
      void apply(Register reg, int regA, int regB, int regC) {
         reg.set(regC, reg.get(regA) * reg.get(regB));
      }
   },
   muli {
      /**
       * (multiply immediate) stores into register C the result of multiplying register A and value B.
       */
      @Override
      void apply(Register reg, int regA, int valB, int regC) {
         reg.set(regC, reg.get(regA) * valB);
      }
   },
   banr {
      /**
       * (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
       */
      @Override
      void apply(Register reg, int regA, int regB, int regC) {
         reg.set(regC, reg.get(regA) & reg.get(regB));
      }
   },
   bani {
      /**
       * (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
       */
      @Override
      void apply(Register reg, int regA, int valB, int regC) {
         reg.set(regC, reg.get(regA) & valB);
      }
   },
   borr {
      /**
       * (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
       */
      @Override
      void apply(Register reg, int regA, int regB, int regC) {
         reg.set(regC, reg.get(regA) | reg.get(regB));
      }
   },
   bori {
      /**
       * (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.
       */
      @Override
      void apply(Register reg, int regA, int valB, int regC) {
         reg.set(regC, reg.get(regA) | valB);
      }
   },
   setr {
      /**
       * (set register) copies the contents of register A into register C. (Input B is ignored.)
       */
      @Override
      void apply(Register reg, int regA, int ignored, int regC) {
         reg.set(regC, reg.get(regA));
      }
   },
   seti {
      /**
       *  (set immediate) stores value A into register C. (Input B is ignored.)
       */
      @Override
      void apply(Register reg, int valA, int ignored, int regC) {
         reg.set(regC, valA);
      }
   },
   gtir {
      /**
       * (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, int valA, int regB, int regC) {
         reg.set(regC, valA > reg.get(regB) ? 1 : 0);
      }
   },
   gtri {
      /**
       * (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, int regA, int valB, int regC) {
         reg.set(regC, reg.get(regA) > valB ? 1 : 0);
      }
   },
   gtrr {
      /**
       * (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, int regA, int regB, int regC) {
         reg.set(regC, reg.get(regA) > reg.get(regB) ? 1 : 0);
      }
   },
   eqir {
      /**
       * (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, int valA, int regB, int regC) {
         reg.set(regC, valA == reg.get(regB) ? 1 : 0);
      }
   },
   eqri {
      /**
       * (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, int regA, int valB, int regC) {
         reg.set(regC, reg.get(regA) == valB ? 1 : 0);
      }
   },
   eqrr {
      /**
       * (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
       */
      @Override
      void apply(Register reg, int regA, int regB, int regC) {
         reg.set(regC, reg.get(regA) == reg.get(regB) ? 1 : 0);
      }
   },
   ;

   abstract void apply(Register reg, int A, int B, int regC);

}
