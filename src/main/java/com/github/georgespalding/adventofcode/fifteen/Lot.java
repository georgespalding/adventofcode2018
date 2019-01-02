package com.github.georgespalding.adventofcode.fifteen;

import static java.util.stream.Stream.of;

import java.util.Objects;
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

   @Override
   public String toString() {
      return "Lot{" +
         "pos=" + pos +
         '}';
   }
}
