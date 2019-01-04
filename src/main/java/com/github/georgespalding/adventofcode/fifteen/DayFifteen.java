package com.github.georgespalding.adventofcode.fifteen;

import static java.util.stream.Collectors.toList;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.List;
import java.util.OptionalInt;

public class DayFifteen {

   static final boolean debug = true;
   private static final Day<Object, Integer> day15 = new Day<>();
   private static final String INPUT = "15/15.lst";

   public static void main(String[] args) {
      {
         final Cavern cavern = new Cavern(Util.streamResource(INPUT), 3);

         day15.start();

         int rounds = 0;
         while (cavern.warIsStillOn()) {
            boolean roundEndedPrematurely = cavern.playRound(false, rounds + 1);
            if (!roundEndedPrematurely) {
               rounds++;
               System.out.println("After " + rounds + " rounds:");
            } else {
               System.out.println("After " + (rounds + 1) + " rounds:");
            }
            System.out.println(cavern.toString());
         }

         int hpSum = cavern.unitsInTurnOrder().stream()
            .filter(Unit::isAlive)
            .mapToInt(Unit::getHitPoints)
            .sum();
         day15.partOne(hpSum * rounds);
      }

//                      System.exit(0);
      
      int lo = 3;//TODO change to 3
      int hi = 35;//TODO change to 35
      boolean hiOk = runGame(hi).isPresent();
      assert hiOk : "Hi elfAttack " + hi + " is not high enough";
      OptionalInt lowestElfAttack = OptionalInt.empty();
      OptionalInt elfLowestWinningScore = OptionalInt.empty();
      while (hi - lo > 1) {
         int next = (lo + hi) / 2;
         OptionalInt elfWinningScore = runGame(next);
         if (elfWinningScore.isPresent()) {
            // go lower
            hi = next;
            elfLowestWinningScore = elfWinningScore;
            lowestElfAttack = OptionalInt.of(next);
         } else {
            lo = next;
         }
      }
      System.out.println("Elf attack: " + lowestElfAttack);
      day15.partTwo(elfLowestWinningScore.getAsInt());     
      day15.output();
   }

   private static OptionalInt runGame(int elfAttach) {
      Cavern cavern = new Cavern(Util.streamResource(DayFifteen.INPUT), elfAttach);
      final List<Unit> origOrder = cavern.unitsInTurnOrder();

      int rounds = 0;

      //TODO save time by aborting when the first elf dies
      while (cavern.warIsStillOn()) {
         boolean roundEndedPrematurely = cavern.playRound(
            debug && elfAttach == 12, rounds + 1);
         if (!roundEndedPrematurely) {
            rounds++;
            debug("After " + rounds + " rounds:");
         } else {
            debug("After " + (rounds + 1) + " rounds:");
         }
         debug(cavern.toString());
      }
      final long survivedElves = cavern.getElves().stream().filter(Unit::isAlive).count();
      final int hpSum = cavern.getElves().stream()
         .filter(Unit::isAlive)
         .mapToInt(Unit::getHitPoints)
         .sum();

      System.out.println(origOrder.stream()
         .map(Unit::getHitPoints)
         .collect(toList()));
      System.out.println("Elf attack: " + elfAttach
         + " Surviving elves: " + survivedElves
         + " of " + cavern.getElves().size() + " in total");
      System.out.println("Combat ends after " + rounds + " full rounds");
      System.out.println((survivedElves == 0 ? "Goblins" : "Elves") + " win with " + hpSum + " total hit points left");
      System.out.println("Outcome: " + rounds + " * " + hpSum + " = " + (rounds * hpSum));
      System.out.println();
      if (survivedElves == cavern.getElves().size()) {
         return OptionalInt.of(rounds * hpSum);
      } else {
         return OptionalInt.empty();
      }
   }

   static void debug(String stuff) {
      if (debug) {
         System.out.println(stuff);
      }
   }
}
