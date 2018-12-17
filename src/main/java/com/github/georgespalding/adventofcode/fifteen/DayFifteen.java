package com.github.georgespalding.adventofcode.fifteen;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

public class DayFifteen {

   static final boolean debug = false;

   static final Day<Object, Object> day15 = new Day<>();

   public static void main(String[] args) {
      final Cavern cavern = new Cavern(Util.streamResource("15.lst"));

      day15.start();

      int rounds = 0;
      while (!cavern.carnageIsOver()) {
         boolean roundEndedPRematurely=cavern.playRound();
         if(!roundEndedPRematurely) {
            rounds++;
            System.out.println("After " + rounds + " rounds:");
            System.out.println(cavern.toString());
         }
      }

      int hpSum = cavern.unitsInTurnOrder().stream()
         .filter(u -> u.getHitPoints() > 0)
         .mapToInt(Unit::getHitPoints)
         .sum();
      System.out.println("Outcome: " + hpSum + " * " + rounds + " = " + hpSum * rounds);
      cavern.unitsInTurnOrder().stream()
         .filter(u -> u.getHitPoints() > 0)
         .forEach(g -> System.out.println("Winner:" + g));
      day15.partOne(hpSum * rounds);



      day15.partTwo("TODO Part2");

      day15.output();
   }
}
