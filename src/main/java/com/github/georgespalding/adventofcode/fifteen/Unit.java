package com.github.georgespalding.adventofcode.fifteen;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import com.github.georgespalding.adventofcode.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

   private Optional<Lot> selectBestDestinationLot(Collection<Unit> enemies) {
      return enemyRangeDistance(enemies)
         .entrySet().stream()
         .collect(groupingBy(Entry::getValue))
         .entrySet().stream()
         .min(comparingInt(Entry::getKey))
         .flatMap(ls -> ls
            .getValue().stream()
            .min(comparing(Entry::getKey))
            .map(Entry::getKey));
   }

   OptionalInt shortestDistance(Lot start, Lot dest) {
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

   Map<Lot, Integer> enemyRangeDistance(Collection<Unit> enemies) {
      return enemyRangeDistance(lot, enemies);
   }

   Map<Lot, Integer> enemyRangeDistance(Lot aLot, Collection<Unit> enemies) {
      final Set<Lot> enemyLots = enemies.stream()
         .map(r -> r.lot)
         .flatMap(Lot::adjacentSpace)
         .collect(Collectors.toSet());
      final Map<Lot, Integer> res = new HashMap<>();
      int dist = 0;
      List<Lot> edges = Collections.singletonList(aLot);
      final Set<Lot> alreadySearched = new HashSet<>(edges);

      while (!edges.isEmpty()) {
         for (Lot l : edges) {
            if (enemyLots.contains(l)) {
               res.put(l, dist);
            }
         }
         dist++;
         edges = flood(edges, alreadySearched);
         alreadySearched.addAll(edges);
      }
      return res;
   }

   Optional<Lot> closestTarget(Lot aLot, Set<Lot> enemyLots) {
      final Map<Lot, Integer> res = new HashMap<>();
      int dist = 0;
      List<Lot> edges = Collections.singletonList(aLot);
      final Set<Lot> alreadySearched = new HashSet<>(edges);

      while (!edges.isEmpty()) {
         Optional<Lot> first = edges.stream().filter(enemyLots::contains).sorted().findFirst();
         if (first.isPresent()) {
            return first;
         }
         dist++;
         edges = flood(edges, alreadySearched);
         alreadySearched.addAll(edges);
      }
      return Optional.empty();
   }

   Optional<Lot> bestMoveToAttackEnemy() {
      // TODO cache
      final Map<Lot, OptionalInt> cache = new HashMap<>();

      Map<OptionalInt, List<Lot>> bestMoves = adjacentFreeOrHostile()
         .collect(groupingBy(this::distanceToFirstLotBorderingEnemyFrom));
      Optional<Lot> bestMove = bestMoves.entrySet().stream()
         .filter(e -> e.getKey().isPresent())
         .min(comparingInt(e -> e.getKey().getAsInt()))
         // If multiple squares are in range and tied for being reachable in the fewest steps,
         // the square which is first in reading order is chosen.
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

   /**
    * To move, the unit first considers the squares that are in range and determines which of those squares it could reach in the fewest steps.
    * A step is a single movement to any adjacent (immediately up, down, left, or right) open (.) square.
    * Units cannot move into walls or other units.
    * The unit does this while considering the current positions of units and does not do any prediction about where units will be later.
    * If the unit cannot reach (find an open path to) any of the squares that are in range, it ends its turn.
    * If multiple squares are in range and tied for being reachable in the fewest steps, the square which is first in reading order is chosen.
    */
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
               .filter(l -> !alreadySearched.contains(l))
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