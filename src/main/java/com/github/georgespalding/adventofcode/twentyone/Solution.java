package com.github.georgespalding.adventofcode.twentyone;

import static java.util.Comparator.comparingLong;

import com.github.georgespalding.adventofcode.Pair;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class Solution {

   private static final boolean debug = false;
   private long r0;
   private long r1 = 0;
   private long r2 = 0;
   private long r3 = 0;
   private long ip = 0;
   private long r5 = 0;
   private long ops = 0;
   private Pair<Long,Long> minR3AndOp;
   private Pair<Long,Long> lastR3AndOp;
   private final HashSet<Long> seenR3Vals=new HashSet<>(10677);

   private Solution(long initR0) {
      this.r0 = initR0;
   }

   public static void main(String[] args) {
      Day<Object, Object> day = new Day<>();

      long maxOps = 2606_000_000L;//3_000_000_000L;
      final Solution routine = new Solution(0);
      routine.execute(maxOps);

      System.out.println("r0=0, r3 vals collected:" + routine.seenR3Vals.size()+" results");
      day.start();


      day.partOne(routine.minR3AndOp);
      
      day.partTwo(routine.lastR3AndOp);
      day.output();
   }

   @Override
   public String toString() {
      return "ip=" + ip + " [" + r0 + "," + r1 + "," + r2 + "," + r3 + "," + ip + "," + r5 + "]";
   }

   private long op() {
      switch ((int) ip) {
         case 0: ops++;r3 = 123; // seti 123 0 3
         case 1: ops++;r3 &= 456; // bani 3 456 3
         case 2: ops++;r3 = r3 == 0b1001000 ? 1 : 0; // eqri 3 72 3
         case 3: ops++;return r3 + 3; // addr 3 4 4
         case 4: ops++;return 0; // seti 0 0 4
         case 5: ops++;r3 = 0; // seti 0 5 3
         case 6: ops++;r2=r3 | 0b10000000000000000; // bori 3 65536 2
         case 7: ops++;r3 = 7637914; // seti 7637914 8 3
         case 8: ops++;r1=r2 & 255; // bani 2 255 1
         case 9: ops++;r3 += r1; // addr 3 1 3
         case 10:ops++; r3 &= 16777215; // bani 3 16777215 3
         case 11:ops++; r3 *= 65899; // muli 3 65899 3
         case 12:ops++; r3 &= 16777215; // bani 3 16777215 3
         case 13:ops++; r1 = 256 > r2 ? 1 : 0; // gtir 256 2 1
         case 14:ops++; return r1 + 14; // addr 1 4 4
         case 15:ops++; return 16; // addi 4 1 4
         case 16:ops++; return 27; // seti 27 1 4
         case 17:ops++; r1 = 0; // seti 0 7 1
         case 18:ops++; r5=r1 + 1; // addi 1 1 5
         case 19:ops++; r5 *= 256; // muli 5 256 5
         case 20:ops++; r5 = r5 > r2 ? 1 : 0; // gtrr 5 2 5
         case 21:ops++; return r5 + 21; // addr 5 4 4
         case 22:ops++; return 23; // addi 4 1 4
         case 23:ops++; return 25; // seti 25 3 4
         case 24:ops++; r1 += 1; // addi 1 1 1
         case 25:ops++; return 17; // seti 17 0 4
         case 26:ops++; r2 = r1; // setr 1 8 2
         case 27:ops++; return 7; // seti 7 7 4
         case 28: // eqrr 3 0 1
            ops++;
            if(!seenR3Vals.contains(r3)) {
               if (debug) {
                  System.out.println("r3=" + r3 + " ops: " + ops);
               }
               lastR3AndOp= Pair.fromEntry(ops,r3);
               if(minR3AndOp==null)
                  minR3AndOp=lastR3AndOp;
               seenR3Vals.add(r3);
            }
            if (r3 == r0) {
               r1 = 1;
            } else {
               r1 = 0;
            }
         case 29:ops++; return r1 + 29; // addr 1 4 4
         case 30:ops++; return 5; // seti 5 5 4
      }
      throw new AssertionError("ShitFuck: ip " + this.ip + " not implemented in switch");
   }

   private long execute(long maxOps) {
      while (ip >= 0 && ip <= 30 && ops<=maxOps) {
         final long prev = ip;
         ip = op();
         ip++;
         if (debug) {
            System.out.printf("%d: prev=%02d next=%02d %d %d %d %02d %d %d\n", ops, prev, ip, r0, r1, r2, r3, ip, r5);
         }
      }
      if (ops <= maxOps) {
         System.out.println("r0="+r0+" Success after " + ops + " ops.");
      } else {
         System.out.println("r0="+r0+" Abort after " + ops + " ops.");
      }

      return ops;
   }

}
