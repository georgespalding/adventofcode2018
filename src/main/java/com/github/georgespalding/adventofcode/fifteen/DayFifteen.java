package com.github.georgespalding.adventofcode.fifteen;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

public class DayFifteen {

   static final boolean debug = false;

   static final Day<Object, Object> day15 = new Day<>();

   public static void main(String[] args) {
      final Cavern cavern = new Cavern(Util.streamResource("15.lst"));

      day15.start();

      int rounds=0;
      while(!cavern.carnageIsOver()){
         System.out.println(cavern.toString());
         rounds++;
         cavern.playRound();
      }

      day15.partOne("TODO Part1");
      day15.partTwo("TODO Part2");

      day15.output();
   }
}
