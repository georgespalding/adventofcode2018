package com.github.georgespalding.adventofcode.fifteen;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.OptionalInt;

public class DayFifteen {

   public static final String INPUT = "15/15.lst";
   static final boolean debug = false;
   static final Day<Object, Integer> day15 = new Day<>();

   public static void main(String[] args) {
      Cavern cavern = new Cavern(Util.streamResource(INPUT), 3);

      day15.start();

      int rounds = 0;
      while (!cavern.carnageIsOver()) {
         boolean roundEndedPRematurely = cavern.playRound();
         if (!roundEndedPRematurely) {
            rounds++;
            System.out.println("After " + rounds + " rounds:");
         } else {
            System.out.println("After " + (rounds + 1) + " rounds:");
         }
         System.out.println(cavern.toString());
      }

      int hpSum = cavern.unitsInTurnOrder().stream()
         .filter(u -> u.getHitPoints() > 0)
         .mapToInt(Unit::getHitPoints)
         .sum();
      cavern.unitsInTurnOrder().stream()
         .filter(u -> u.getHitPoints() > 0)
         .forEach(g -> System.out.println("Winner:" + g));
      day15.partOne(hpSum * rounds);

      int lo = 3;
      boolean loOk = cavern.getElves().stream().allMatch(u -> u.getHitPoints() > 0);
      assert !loOk : "Lo elfAttack " + lo + " is not low enough";
      int hi = 36;
      boolean hiOk = runGame(INPUT, hi).isPresent();
      assert hiOk : "Hi elfAttack " + hi + " is not high enough";
      OptionalInt elfLowestWinningScore = OptionalInt.empty();
      while (hi - lo > 1) {
         int next = (lo + hi) / 2;
         OptionalInt elfWinningScore = runGame(INPUT, next);
         if (elfWinningScore.isPresent()) {
            // go lower
            hi = next;
            elfLowestWinningScore = elfWinningScore;
         } else {
            lo = next;
         }
      }
      day15.partTwo(elfLowestWinningScore.getAsInt());

      day15.output();
   }

   public static OptionalInt runGame(String input, int elfAttach) {
      Cavern cavern = new Cavern(Util.streamResource(input), elfAttach);

      int rounds = 0;
      while (!cavern.carnageIsOver()) {
         boolean roundEndedPRematurely = cavern.playRound();
         if (!roundEndedPRematurely) {
            rounds++;
         } else {
            System.out.println("After " + (rounds + 1) + " rounds:");
            System.out.println(cavern.toString());
         }
      }
      final long survivedElves = cavern.getElves().stream().filter(u -> u.getHitPoints() > 0).count();
      final int hpSum = cavern.getElves().stream()
         .filter(u -> u.getHitPoints() > 0)
         .mapToInt(Unit::getHitPoints)
         .sum();

      System.out.println("Elf attack: " + elfAttach
         + " Surviving elfs: " + survivedElves
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
