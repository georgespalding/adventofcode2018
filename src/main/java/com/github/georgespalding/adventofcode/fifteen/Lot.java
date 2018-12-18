package com.github.georgespalding.adventofcode.fifteen;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

class Lot implements Comparable<Lot> {

   final Point pos;
   Lot n;
   Lot w;
   Lot e;
   Lot s;
   Unit occupier;

   Lot(Point pos) {
      this.pos = pos;
   }

   @Override
   public int compareTo(Lot o) {
      return this.pos.compareTo(o.pos);
   }

   Set<Lot> floodableArea() {
      final HashSet<Lot> alreadySelected = new HashSet<>();
      floodableArea(alreadySelected);
      return alreadySelected;
   }

   void floodableArea(Set<Lot> alreadySelected) {
      Set<Lot> additions = of(n, w, e, s)
         .filter(o -> !alreadySelected.contains(o))
         .collect(toSet());
      alreadySelected.addAll(additions);
      additions.forEach(l -> l.floodableArea(alreadySelected));
   }

   boolean connected(Lot other) {
      return floodableArea().contains(other);
   }

   Stream<Lot> adjacentSpace() {
      return adjacentLots()
         .filter(l -> l.occupier == null);
   }

   Stream<Lot> adjacentLots() {
      return of(
         n,
         w,
         e,
         s)
         .filter(Objects::nonNull);
   }

   // flood fill until closest enemies reached, return the candidate spot
   Optional<Lot> bestPlaceToAttackEnemy() {
      final Unit thisUnit = this.occupier;
      assert thisUnit != null : "Missing unit in this when searching for units to attack";
      final Set<Lot> alreadyCovered = new HashSet<>();
      alreadyCovered.add(this);
      Set<Lot> next = adjacentSpace().collect(toSet());
      do {
         // Avbrottsvillkor någon av next är granne med denna
         Optional<Lot> first = next.stream()
            .flatMap(Lot::adjacentLots)
            .filter(l -> !alreadyCovered.contains(l))
            .filter(thisUnit::containsEnemy)
            .sorted()
            .findFirst();
         if (first.isPresent()) {
            return first;
         }

         alreadyCovered.addAll(next);
         next = next.stream()
            .flatMap(Lot::adjacentSpace)
            .filter(l -> !alreadyCovered.contains(l))
            .collect(toSet());

      } while (!next.isEmpty());
      return Optional.empty();
   }

   // flood fill until closest enemies reached, return the candidate spot
   Optional<Lot> bestStepToReach(Lot lot) {
      final Set<Lot> candidateSteps = this.adjacentSpace().collect(toSet());
      final Set<Lot> alreadyCovered = new HashSet<>();
      alreadyCovered.add(lot);
      Set<Lot> next = lot.adjacentSpace().collect(toSet());
      do {

         // Avbrottsvillkor någon av next är granne med denna
         Optional<Lot> first = next.stream()
            .filter(candidateSteps::contains)
            .sorted()
            .findFirst();
         if (first.isPresent()) {
            return first;
         }

         alreadyCovered.addAll(next);
         next = next.stream()
            .flatMap(Lot::adjacentSpace)
            .filter(l -> !alreadyCovered.contains(l))
            .collect(toSet());

      } while (!next.isEmpty());
      return Optional.empty();
   }

   @Override
   public String toString() {
      return "Lot{" +
         "pos=" + pos +
         '}';
   }
}
