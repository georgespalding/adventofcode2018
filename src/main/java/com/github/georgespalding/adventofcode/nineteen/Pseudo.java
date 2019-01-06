package com.github.georgespalding.adventofcode.nineteen;

public class Pseudo {

   public static void main(String[] args) {
      System.out.println((27*28+29)*30* 14*32);
      mod();
   }

   public static void orig() {
      int r0 = 0;
      int r1 = 0;
      int r2 = 0;
      int ip = 0;
      int r4 = 0;
      int r5 = 0;

      /*00*/   ip += 16;
      /*01*/   r2 = 1;
      /*02*/   r4 = 1;
      /*03*/   r1 = r2 * r4;
      /*04*/   r1 = r1 == r5 ? 1 : 0;
      /*05*/   ip += r1;
      /*06*/   ip += 1;
      /*07*/   r0 += r2;
      /*08*/   r4 += 1;
      /*09*/   r1 = r4 > r5 ? 1 : 0;
      /*10*/   ip += r1;
      /*11*/   ip = 2;
      /*12*/   r2 += 1;
      /*13*/   r1 = r2 > 13 ? 1 : 0;
      /*14*/   ip += r1;
      /*15*/   ip = 1;
      /*16*/   ip *= 16;
      /*17*/   r5 += 2;
      /*18*/   r5 *= r5;
      /*19*/   r5 *= 19;
      /*20*/   r5 *= 11;
      /*21*/   r1 += 3;
      /*22*/   r1 *= 22;
      /*23*/   r1 += 12;
      /*24*/   r5 += r1;
      /*25*/   ip += r0;
      /*26*/   ip = 0;
      /*27*/   r1 = 10550400;
      /*33*/   r5 += r1;
      /*34*/   r0 = 0;
      /*35*/   ip = 0;
   }

   public static void mod() {
      int r0 = 0;
      int r1 = 0;
      int r2 = 0;
      int ip = 0;
      int r4 = 0;
      int r5 = 0;

      end:
      do {
         ip += 16;
         r2 = 1;
         do {
            r4 = 1;
            do {
               r1 = r2 * r4;
               if (r1 != r5 || r1 > 1) {
                  r0 += r2;
               }
               if (r1 < 2) {
                  r4 += 1;
               }
               if (r1 > 2) {
                  System.out.println("TODO jump ahead r1=" + r1);
               }
               //      r1 = r4 > r5 ? 1 : 0;
            } while (r4 > r5);
            System.out.println("TODO jump ahead r1=" + r1);
            ip += r1;//TODO jump
            r2 += 1;
         } while (r2 <= r5);
         System.out.println("TODO jump ahead ip=" + ip + " ip *= ip" + (ip * ip));
         ip *= ip;
         r5 += 2;
         r5 *= r5;
         r5 *= ip;
         r5 *= 11;
         r1 += 3;
         r1 *= ip;
         r1 += 12;
         r5 += r1;
         ip += r0;
         ip = 0;
         r1 = ip;
         r1 *= ip;
         r1 += ip;
         r1 *= ip;
         r1 *= 14;
         r1 *= ip;
         r5 += r1;
      }while (ip<35);
   }
}
