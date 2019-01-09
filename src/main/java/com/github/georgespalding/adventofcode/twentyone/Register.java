package com.github.georgespalding.adventofcode.twentyone;

import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.stream.LongStream;

class Register {

   private final long[] register = new long[6];
   private final int ipReg;

   Register(int ipReg) {this.ipReg = ipReg;}

   public static String name(long reg) {
      return "r" + reg;
   }

   void set(int reg, long val) {
      assert reg >= 0 : "Illegal access! register:" + reg;
      assert reg < 6 : "Illegal access! register:" + reg;
      if(reg==4 && val!=25&& val!=27){
         long currPtr=register[reg];
         if(currPtr!=val && val-currPtr>1) {
            System.out.println("Long jump from " + currPtr + " to " + val);
         }
      }
      register[reg] = val;
   }

   long get(long reg) {
      return get(toIntExact(reg));
   }

   long get(int reg) {
      assert reg >= 0 : "Illegal access! register:" + reg;
      assert reg < 6 : "Illegal access! register:" + reg;
      return register[reg];
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

   @Override
   public String toString() {
      return LongStream.of(register).boxed().collect(toList()).toString();
   }

   long[] copyState() {
      long[] copy = new long[6];
      System.arraycopy(register, 0, copy, 0, 6);
      return copy;
   }

   String regName(long reg) {
      return name(reg) + ":" + get(reg);
      //      assert reg < 6 : "Illegal access! register:" + reg;
      //      switch (toIntExact(reg)){
      //         case 0: return "fadern("+get(reg)+")";
      //         case 1: return "jesus("+get(reg)+")";
      //         case 2: return "anden("+get(reg)+")";
      //         case 3: return "muhammed("+get(reg)+")";
      //         case 4: return "shiva("+get(reg)+")";
      //         case 5: return "budda("+get(reg)+")";
      //      }
      //      throw new ArrayIndexOutOfBoundsException("Register index is out of bounds:"+reg);
   }

   void loadCtr(long ipCtr) {
      set(ipReg, ipCtr);
   }

   long fetchCtr() {
      return get(ipReg);
   }
}
