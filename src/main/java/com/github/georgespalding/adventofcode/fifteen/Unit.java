package com.github.georgespalding.adventofcode.fifteen;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

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
      return this.symbol != other.symbol;
   }

   boolean containsEnemy(Lot lot) {
      return ofNullable(lot.occupier).map(this::isEnemy).orElse(false);
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

   Lot getLot() {
      return lot;
   }

   List<Unit> adjacentEnemies() {
      return getLot().adjacentLots()
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

}
