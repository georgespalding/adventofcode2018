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
      Cavern cavern = new Cavern(Util.streamResource(INPUT), 3);

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
      cavern.unitsInTurnOrder().stream()
         .filter(Unit::isAlive)
         .forEach(g -> System.out.println("Winner:" + g));
      day15.partOne(hpSum * rounds);
      int lo = 2;
      boolean loOk = cavern.getElves().stream().allMatch(Unit::isAlive);
      assert !loOk : "Lo elfAttack " + lo + " is not low enough";
      int hi = 50;
      boolean hiOk = runGame(INPUT, hi).isPresent();
      assert hiOk : "Hi elfAttack " + hi + " is not high enough";
      OptionalInt lowestElfAttack = OptionalInt.empty();
      OptionalInt elfLowestWinningScore = OptionalInt.empty();
      while (hi - lo > 1) {
         int next = (lo + hi) / 2;
         OptionalInt elfWinningScore = runGame(INPUT, next);
         if (elfWinningScore.isPresent()) {
            // go lower
            hi = next;
            elfLowestWinningScore = elfWinningScore;
            lowestElfAttack = OptionalInt.of(next);
         } else {
            lo = next;
         }
      }
      System.out.println("Elf attack: " +lowestElfAttack);
      day15.partTwo(elfLowestWinningScore.getAsInt());
      day15.output();
   }

   private static OptionalInt runGame(String input, int elfAttach) {
      Cavern cavern = new Cavern(Util.streamResource(input), elfAttach);
      final List<Unit> origOrder = cavern.unitsInTurnOrder();

      int rounds = 0;
      //TODO save time by aborting when the first elf dies
      while (cavern.warIsStillOn()) {
         boolean roundEndedPrematurely = cavern.playRound(elfAttach == 12, rounds + 1);
         if (!roundEndedPrematurely) {
            rounds++;
            if (debug) {
               System.out.println("After " + rounds + " rounds:");
            }
         } else {
            if (debug) {
               System.out.println("After " + (rounds + 1) + " rounds:");
            }
         }
         if (debug) {
            System.out.println(cavern.toString());
         }
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
}
