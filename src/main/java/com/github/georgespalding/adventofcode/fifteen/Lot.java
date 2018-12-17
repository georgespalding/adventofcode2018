package com.github.georgespalding.adventofcode.fifteen;

import com.github.georgespalding.adventofcode.Pair;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
      Set<Lot> additions = Stream.of(n, w, e, s)
         .filter(o -> !alreadySelected.contains(o))
         .collect(Collectors.toSet());
      alreadySelected.addAll(additions);
      additions.forEach(l -> l.floodableArea(alreadySelected));
   }

   boolean connected(Lot other) {
      return floodableArea().contains(other);
   }

   Stream<Lot> adjacentSpace() {
      return Stream.of(
         n,
         w,
         e,
         s)
         .filter(Objects::nonNull)
         .filter(l -> l.occupier == null);
   }

   Pair<Integer, Lot> bestLotsToward(Lot other) {
      final Set<Lot> alreadyCovered = new HashSet<>();
      int dist = 0;
      Set<Lot> next = Collections.singleton(other);
      do {
         alreadyCovered.addAll(next);
         next = next.stream().flatMap(Lot::adjacentSpace)
            .filter(l -> !alreadyCovered.contains(l))
            .collect(Collectors.toSet());
         dist++;

         Optional<Lot> first = adjacentSpace().filter(next::contains).findFirst();
         if (first.isPresent()) {
            return Pair.fromEntry(dist, first.get());
         }
      } while (!next.isEmpty());
      return null;
   }

   @Override
   public String toString() {
      return "Lot{" +
         "pos=" + pos +
         '}';
   }
}
