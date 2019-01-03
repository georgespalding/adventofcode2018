package com.github.georgespalding.adventofcode.fifteen;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.github.georgespalding.adventofcode.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Stream;

class Unit implements Comparable<Unit> {

   private final char symbol;
   private final int attackPower;
   private int hitPoints = 200;
   private Lot lot;

   Unit(char symbol, Lot lot, int attackPower) {
      this.symbol = symbol;
      this.lot = lot;
      this.attackPower = attackPower;
   }

   @Override
   public int compareTo(Unit o) {
      return this.lot.pos.compareTo(o.lot.pos);
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

   private Stream<Lot> adjacentFreeOrHostile() {
      return lot.adjacentLots().filter(this::isFreeOrHostile);
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

   List<Unit> adjacentEnemies() {
      return lot.adjacentLots()
         .map(l -> l.occupier)
         .filter(Objects::nonNull)
         .filter(this::isEnemy)
         .sorted()
         .collect(toList());
   }

   boolean attack(Unit enemy) {
      return enemy.receiveAttack(attackPower);
   }

   private boolean receiveAttack(int attackPower) {
      hitPoints -= attackPower;
      //if (DayFifteen.debug) System.out.println(symbol + " at " + lot.pos + " struck -" + attackPower + " hitPoints: " + hitPoints);
      if (hitPoints <= 0) {
         if (symbol == 'E') {
            System.out.println("==== ELF DEATH ================");
         }
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

   Optional<Lot> bestMoveToAttackEnemy() {
      // TODO cache
      final Map<Lot, OptionalInt> cache = new HashMap<>();

      Map<OptionalInt, List<Lot>> bestMoves = adjacentFreeOrHostile()
         .collect(groupingBy(this::distanceToFirstLotBorderingEnemyFrom));
      Optional<Lot> bestMove = bestMoves.entrySet().stream()
         .filter(e -> e.getKey().isPresent()).min(Comparator.comparingInt(e -> e.getKey().getAsInt()))
         .flatMap(ls -> ls.getValue().stream().sorted().findFirst());
      if (DayFifteen.debug) {
         if (bestMove.isPresent()) {
            System.out.println("Picked: " + bestMove + "from " + bestMoves);
         } else {
            System.out.println("No move to be made");
         }
      }
      return bestMove;
   }

   private OptionalInt distanceToFirstLotBorderingEnemyFrom(Lot aLot) {
      Set<Lot> alreadySearched = new HashSet<>();
      alreadySearched.add(aLot);
      int dist = 1;
      List<Lot> edges = Collections.singletonList(aLot);
      do {
         // Find bordering to enemy
         final Optional<Lot> firstBorderingToEnemy = edges.stream()
            .filter(b -> b.adjacentLots()
               //Skip this check?
               // .filter(l -> !alreadySearched.contains(l))
               .anyMatch(l -> isEnemy(l.occupier)))
            .sorted()
            .findFirst();
         if (firstBorderingToEnemy.isPresent()) {
            return OptionalInt.of(dist);
         }
         edges = flood(edges, alreadySearched);
         alreadySearched.addAll(edges);
         dist++;
      } while (!edges.isEmpty());
      return OptionalInt.empty();
   }

   private OptionalInt distanceToFirstEnemyFrom(Lot someLot) {
      Set<Lot> alreadySearched = new HashSet<>();
      alreadySearched.add(someLot);
      int dist = 0;
      List<Lot> edges = Collections.singletonList(someLot);
      Optional<Lot> firstEnemy;
      do {
         dist++;
         edges = flood(edges, alreadySearched);
         //// Kollar nu efter rutor bredvid en fiende...
         //firstEnemy = edges.stream().filter(l -> l.adjacentLots().anyMatch(a->isEnemy(a.occupier))).findFirst();
         firstEnemy = edges.stream().filter(l -> isEnemy(l.occupier)).findFirst();
         if (firstEnemy.isPresent()) {
            return OptionalInt.of(dist);
         }
         alreadySearched.addAll(edges);
      } while (!edges.isEmpty());
      return OptionalInt.empty();
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