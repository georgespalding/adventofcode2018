package com.github.georgespalding.adventofcode.fifteen;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

import com.github.georgespalding.adventofcode.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

class Unit {

   /**
    * For instance, the order in which units take their turns within a round is the reading order of their starting positions in that round, regardless of the type of unit or whether other units have moved after the round started.
    */
   static final Comparator<Unit> turnOrder =
      comparing(o -> o.lot.pos);
   /**
    * The adjacent target with the fewest hit points is selected;
    * in a tie, the adjacent target with the fewest hit points
    * which is first in reading order is selected.
    */
   private static final Comparator<Unit> sortEnemies =
      comparingInt((Unit o) -> o.hitPoints)
         .thenComparing(o -> o.lot.pos);
   private final char symbol;
   private final int attackPower;
   // Each unit, either Goblin or Elf, ... starts with 200 hit points.
   private int hitPoints = 200;
   private Lot lot;

   Unit(char symbol, Lot lot, int attackPower) {
      this.symbol = symbol;
      this.lot = lot;
      this.attackPower = attackPower;
   }

   char symbol() {
      return symbol;
   }

   private boolean isEnemy(Unit other) {
      return other != null && this.symbol != other.symbol;
   }

   private boolean isFreeOrHostile(Lot other) {
      return other.occupier == null || this.symbol != other.occupier.symbol;
   }

   void moveTo(Lot lot) {
      assert this.lot.adjacentSpace().anyMatch(l -> l == lot) : "Can't move to a lot that is not adjacent!";
      assert lot.occupier == null : "Can't move to a lot with a unit in it!";
      this.lot.occupier = null;
      this.lot = lot;
      this.lot.occupier = this;
   }

   int getHitPoints() {
      return hitPoints;
   }

   /**
    * To attack, the unit first determines all of the targets that are in range of it by being immediately adjacent to it.
    * If there are no such targets, the unit ends its turn.
    * Otherwise, the adjacent target with the fewest hit points is selected;
    * in a tie, the adjacent target with the fewest hit points which is first in reading order is selected.
    */
   List<Unit> adjacentEnemiesInWeaknessOrder() {
      return lot.adjacentLots()
         .map(l -> l.occupier)
         .filter(this::isEnemy)
         .sorted(Unit.sortEnemies)
         .collect(toList());
   }

   /**
    * The unit deals damage equal to its attack power to the selected target,
    * reducing its hit points by that amount.
    */
   boolean attack(Unit enemy) {
      return enemy.receiveAttack(attackPower);
   }

   /**
    * If this reduces its hit points to 0 or fewer,
    * the selected target dies: its square becomes . and it takes no further turns.
    */
   private boolean receiveAttack(int attackPower) {
      hitPoints -= attackPower;
      if (hitPoints <= 0) {
         lot.occupier = null;
         lot = null;
         return true;
      } else {
         return false;
      }
   }

   boolean isAlive() {
      return hitPoints > 0;
   }

   @Override
   public String toString() {
      return "Unit{" +
         "symbol=" + symbol +
         ", attackPower=" + attackPower +
         ", hitPoints=" + hitPoints +
         ", lot=" + lot +
         '}';
   }

   Optional<Lot> bestMoveToAttackEnemy(Collection<Unit> enemies) {
      final Set<Lot> enemyLots = enemies.stream()
         .map(r -> r.lot)
         .flatMap(Lot::adjacentSpace)
         .collect(Collectors.toSet());
      return closestTarget(lot, enemyLots)
         .flatMap(dest ->
            this.lot.adjacentSpace()
               .map(startLot -> Pair.fromEntry(shortestDistance(startLot, dest), startLot))
               .filter(p1 -> p1.getKey().isPresent())
               .min(comparingInt((Pair<OptionalInt, Lot> o) -> o.getKey().getAsInt())
                  .thenComparing(Pair::getVal))
               .map(Pair::getVal));
   }

   private OptionalInt shortestDistance(Lot start, Lot dest) {
      final Set<Lot> enemyLots = Set.of(dest);
      int dist = 0;
      List<Lot> edges = Collections.singletonList(start);
      final Set<Lot> alreadySearched = new HashSet<>(edges);

      while (!edges.isEmpty()) {
         for (Lot l : edges) {
            if (enemyLots.contains(l)) {
               return OptionalInt.of(dist);
            }
         }
         dist++;
         edges = flood(edges, alreadySearched);
         alreadySearched.addAll(edges);
      }
      return OptionalInt.empty();
   }

   private Optional<Lot> closestTarget(Lot aLot, Set<Lot> enemyLots) {
      List<Lot> edges = Collections.singletonList(aLot);
      final Set<Lot> alreadySearched = new HashSet<>(edges);

      while (!edges.isEmpty()) {
         Optional<Lot> first = edges.stream().filter(enemyLots::contains).sorted().findFirst();
         if (first.isPresent()) {
            return first;
         }
         edges = flood(edges, alreadySearched);
         alreadySearched.addAll(edges);
      }
      return Optional.empty();
   }

   private List<Lot> flood(List<Lot> edges, Set<Lot> alreadySearched) {
      return edges.stream()
         //         .flatMap(Lot::adjacentSpace)
         .flatMap(Lot::adjacentLots)
         .filter(this::isFreeOrHostile)
         .filter(l -> !alreadySearched.contains(l))
         .sorted()
         .distinct()
         .collect(toList());
   }

}