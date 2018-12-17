package com.github.georgespalding.adventofcode.fifteen;

import static java.util.Comparator.comparingInt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Unit implements Comparable<Unit> {

   private final char symbol;
   private final int attackPower;
   private int hitPoints = 200;
   private Lot lot;

   Unit(char symbol, Lot lot, int attackPower) {
      this.symbol = symbol;
      this.lot = lot;
      this.attackPower = attackPower;
   }

   List<Point> availableRange() {
      return Collections.emptyList();
   }

   @Override
   public int compareTo(Unit o) {
      return this.lot.pos.compareTo(o.lot.pos);
   }

   char symbol() {
      return symbol;
   }

   boolean isEnemy(Unit other) {
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

   public int getAttackPower() {
      return attackPower;
   }

   public int getHitPoints() {
      return hitPoints;
   }

   public Lot getLot() {
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

   public void attack(Unit enemy) {
      enemy.receiveAttack(attackPower);
   }

   void receiveAttack(int attackPower) {
      hitPoints -= attackPower;
      if (hitPoints <= 0) {
         lot.occupier = null;
         lot = null;
      }
   }

   boolean attemptAttack() {
      final List<Unit> adjacentEnemies = adjacentEnemies();
      if (!adjacentEnemies.isEmpty()) {
         adjacentEnemies.stream().min(comparingInt(Unit::getHitPoints)).ifPresent(this::attack);
         return true;
      } else {
         return false;
      }
   }

   public boolean isAlive() {
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

   public boolean isAd(Lot l) {
      return l.occupier != null && this.isEnemy(l.occupier);
   }
}
