package com.github.georgespalding.adventofcode.sixteen;

import java.util.Arrays;

class Register {

   private final int[] register = new int[4];

   void set(int reg, int val) {
      assert reg > 0 : "Illegal access! register:" + reg;
      assert reg < 4 : "Illegal access! register:" + reg;
      register[reg] = val;
   }

   int get(int reg) {
      assert reg > 0 : "Illegal access! register:" + reg;
      assert reg < 4 : "Illegal access! register:" + reg;
      return register[reg];
   }

   void load(Register other) {
      System.arraycopy(other.register, 0, register, 0, register.length);
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(register);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      Register register1 = (Register) o;
      return Arrays.equals(register, register1.register);
   }
}
