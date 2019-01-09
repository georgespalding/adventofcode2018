package com.github.georgespalding.adventofcode.twentyone;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

public class Day21 {

   public static boolean debug=false;

   public static void main(String[] args) {
      final Program program = new Program(Util.streamResource("21/21.lst"));

      System.out.println(program);
      System.exit(0);
   }
}
