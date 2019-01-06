package com.github.georgespalding.adventofcode.nineteen;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

public class Day19 {

   public static boolean debug=false;

   public static void main(String[] args) {
      final Day<Long, Object> day = new Day<>();
      final Program program = new Program(Util.streamResource("19/19.lst"));

      System.out.println(program);
      day.start();

      Register reg = program.createRegister();
      program.execute(reg);
      day.partOne(reg.get(0));

      program.reset();
      Register reg2 = program.createRegister();
      reg2.set(0,1);
      //program.execute(reg2);
      day.partTwo(3+3*10551314/2);

      day.output();
   }
}
