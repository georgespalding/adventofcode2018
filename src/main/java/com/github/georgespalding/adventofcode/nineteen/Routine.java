package com.github.georgespalding.adventofcode.nineteen;

import com.github.georgespalding.adventofcode.template.Day;

public class Routine {

   private long r0;
   private long r1 = 0;
   private long r2 = 0;
   private long ip = 0;
   private long r4 = 0;
   private long r5 = 0;

   private Routine(int initR0) {
      this.r0 = initR0;
   }

   public static void main(String[] args) {
      Day<Long, Long> day = new Day<>();
      day.start();
      day.partOne(new Routine(0).execute());
      //day.partTwo(new Routine(1).execute());
      day.partTwo(3L+3L*10551314/2);
      day.output();
   }

   @Override
   public String toString() {
      return "ip=" + ip + " [" + r0 + "," + r1 + "," + r2 + "," + ip + "," + r4 + "," + r5 + "]";
   }

   private long op() {
      switch ((int) ip) {
         case 0: return 16;
         case 1: r2 = 1;
         case 2: r4 = 1;
         case 3: r1 = r2 * r4;
         case 4: r1 = r1 == r5 ? 1 : 0;
         case 5: if(r1==0)return 7;
//         case 5: return 5 + r1;
//         case 6: return 6 + 1;
         case 7: r0 += r2;
         case 8: r4 += 1;
         case 9: r1 = r4 > r5 ? 1 : 0;
         case 10: return 10 + r1;
         case 11: return 2;
         case 12: r2 += 1;
         case 13: r1 = r2 > r5 ? 1 : 0;
         case 14: return 14 + r1;
         case 15: return 1;
         case 16: return 16 * 16;
         case 17: r5 += 2;
         case 18: r5 *= r5;
         case 19: r5 *= 19;
         case 20: r5 *= 11;
         case 21: r1 += 3;
         case 22: r1 *= 22;
         case 23: r1 += 12;
         case 24: r5 += r1;
         case 25: return 25 + r0;
         case 26: return 0;
         case 27: r1 = 27;
         case 28: r1 *= 28;
         case 29: r1 += 29;
         case 30: r1 *= 30;
         case 31: r1 *= 14;
         case 32: r1 *= 32;
         case 33: r5 += r1;
         case 34: r0 = 0;
         case 35: return 0;
      }
      throw new AssertionError("ShitFuck: ip " + this.ip + " not implemented in switch");
   }
   
   private long execute() {
      long ops=0;
      while (ip >= 0 && ip < 35) {
         final long prev=ip;
         ip = op();
         ip++;
         if (ops%10000000<8)
            System.out.printf("%d: prev=%02d next=%02d %d %d %d %02d %d %d\n",ops,prev,ip,r0,r1,r2,ip,r4,r5);
         ops++;
      }
      System.out.println("Ops: "+ops);
      return r0;
   }

}
