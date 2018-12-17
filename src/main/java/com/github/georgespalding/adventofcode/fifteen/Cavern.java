package com.github.georgespalding.adventofcode.fifteen;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import com.github.georgespalding.adventofcode.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

class Cavern {

   private final List<Lot[]> lines;
   private final Map<Character, List<Unit>> unitsBySymbol;

   Cavern(Stream<String> cavernLines) {
      lines = new ArrayList<>();
      unitsBySymbol = new HashMap<>();
      cavernLines
         .map(String::toCharArray)
         .forEach(ls -> {
            final int y = lines.size();
            lines.add(range(0, ls.length)
               .mapToObj(x -> {
                  final Point pos = new Point(x, y);

                  final Lot lot;
                  char symbol = ls[x];
                  if (symbol == '#') {
                     lot = null;
                  } else {
                     lot = new Lot(pos);
                     switch (symbol) {
                        case 'E':
                        case 'G':
                           lot.occupier = new Unit(symbol, lot);
                           unitsBySymbol
                              .computeIfAbsent(symbol, smbl -> new ArrayList<>())
                              .add(lot.occupier);
                           break;
                     }
                  }
                  return lot;
               }).toArray(Lot[]::new));
         });

      // Connect lots
      lines.stream()
         .flatMap(Arrays::stream)
         .filter(Objects::nonNull)
         .sorted()
         .forEach(this::connect);
   }

   List<Unit> unitsInTurnOrder() {
      return unitsBySymbol.values().stream()
         .flatMap(Collection::stream)
         .sorted()
         .collect(toList());
   }

   void playRound() {
      List<Unit> unitsInTurnOrder = unitsInTurnOrder();
      unitsInTurnOrder
         .stream()
         .filter(u -> u.getHitPoints() > 0)
         .forEach(u -> {
               final List<Unit> enemyUnits = unitsBySymbol.get(u.symbol() == 'E' ? 'G' : 'E');
               // Is war over?
               if (enemyUnits.isEmpty()) {
                  System.out.println("battle is over");
               } else {
                  // Any enemies within striking distance?
                  List<Unit> adjacentEnemies = u.adjacentEnemies();
                  if (!adjacentEnemies.isEmpty()) {
                     adjacentEnemies.stream().findFirst().ifPresent(u::attack);
                  } else {
                     final Lot lot = u.getLot();
                     final Map<Integer, List<Pair<Integer, Lot>>> enemiesByProximity = enemyUnits.stream()
                        .map(Unit::getLot)
                        .map(lot::bestLotsToward)
                        .filter(Objects::nonNull)
                        .collect(groupingBy(Pair::getKey));
                     final OptionalInt distClosestEnemy = enemiesByProximity.keySet()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .max();
                     if (distClosestEnemy.isEmpty()) {
                        System.out.println("WTF:" + u.getLot().pos);
                     } else {
                        final Lot bestMove = enemiesByProximity
                           .get(distClosestEnemy.getAsInt()).stream()
                           .sorted(Comparator.comparing(Pair::getVal))
                           .findFirst().get()
                           .getVal();
                        u.moveTo(bestMove);
                     }
                  }
               }
            }
         );
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      for (int y = 0; y < lines.size(); y++) {
         final Lot[] ls = lines.get(y);
         for (int x = 0; x < ls.length; x++) {
            final Lot lot = ls[x];
            if (lot == null) {
               sb.append('#');
            } else {
               sb.append(ofNullable(lot.occupier)
                  .map(Unit::symbol)
                  .orElse('.'));
            }
         }
         sb.append('\n');
      }
      return sb.toString();
   }

   // always do this for the lot closest to 0,0
   void connect(Lot lot) {
      final Point loc = lot.pos;
      lotAt(loc.x + 1, loc.y).ifPresent(olot -> {
         lot.e = olot;
         olot.w = lot;
      });
      lotAt(loc.x, loc.y + 1).ifPresent(olot -> {
         lot.s = olot;
         olot.n = lot;
      });
   }

   Stream<Lot> inRange(Lot lot) {
      return Stream.of(
         lot.n,
         lot.w,
         lot.e,
         lot.s)
         .filter(Objects::nonNull)
         .filter(l -> l.occupier == null);
   }

   Optional<Lot> lotAt(int x, int y) {
      return ofNullable(lines)
         .filter(ls -> ls.size() > y)
         .map(ls -> ls.get(y))
         .filter(lots -> lots.length > x)
         .map(lots -> lots[x]);
   }

   public boolean carnageIsOver() {
      return unitsBySymbol.values().stream()
         .anyMatch(l -> l.stream()
            .filter(Unit::isAlive).findFirst().isEmpty());
   }
}
