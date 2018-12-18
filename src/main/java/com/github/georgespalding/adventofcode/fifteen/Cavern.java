package com.github.georgespalding.adventofcode.fifteen;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Cavern {

   private final List<Lot[]> lines;
   private final Map<Character, List<Unit>> unitsBySymbol;

   Cavern(Stream<String> cavernLines, int elfAttackForce) {
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
                     int attackForce = 3;
                     switch (symbol) {
                        case 'E':
                           attackForce = elfAttackForce;
                        case 'G':
                           lot.occupier = new Unit(symbol, lot, attackForce);
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

   List<Unit> getElves() {
      return unitsBySymbol.get('E');
   }

   List<Unit> unitsInTurnOrder() {
      return unitsBySymbol.values().stream()
         .flatMap(Collection::stream)
         .filter(Unit::isAlive)
         .sorted()
         .collect(toList());
   }

   boolean playRound(boolean debug, int round) {
      AtomicInteger curr = new AtomicInteger();
      return unitsInTurnOrder()
         .stream()
         // Deferred filtering!!!
         .filter(Unit::isAlive)
         .anyMatch(u -> {
            assert u.getHitPoints() > 0 : "Dead units don't fight";
            // Any enemies within striking distance?
            List<Unit> adjacentEnemies = u.adjacentEnemies();
            if (adjacentEnemies.isEmpty()) {
               final Lot lot = u.getLot();
               final Optional<Lot> target = lot.bestPlaceToAttackEnemy();
               if (target.isPresent()) {
                  final Optional<Lot> step = lot.bestStepToReach(target.get());
                  if (step.isPresent()) {
                     u.moveTo(step.get());
                     adjacentEnemies = u.adjacentEnemies();
                  }
               }
            }
            if (!adjacentEnemies.isEmpty()) {
               return adjacentEnemies.stream().min(comparingInt(Unit::getHitPoints))
                  .map(u::attack)
                  .filter(b -> {
                     debug(debug, round, curr, u);
                     return b;
                  })
                  .filter(attackResult -> attackResult)
                  .map(tru -> areAllEnemiesDefeated(u))
                  .orElse(false);
            }
            debug(debug, round, curr, u);
            return false;
         });
   }

   private void debug(boolean debug, int round, AtomicInteger curr, Unit u) {
      if (debug) {
         System.out.println("During " + round + ":" + curr.incrementAndGet() + " " + u);
         System.out.println(toString());
         try {
            Thread.sleep(400);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      lines.forEach(ls -> {
         stream(ls)
            .forEach(lot -> {
               if (lot == null) {
                  sb.append('#');
               } else {
                  sb.append(ofNullable(lot.occupier)
                     .map(Unit::symbol)
                     .orElse('.'));
               }
            });
         sb.append("   ").append(stream(ls)
            .filter(l -> l != null && l.occupier != null)
            .map(l -> l.occupier)
            .map(u -> u.symbol() + "(" + u.getHitPoints() + ")")
            .collect(Collectors.joining(", ")));
         sb.append('\n');
      });
      return sb.toString();
   }

   // always do this for the lot closest to 0,0
   private void connect(Lot lot) {
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

   private Optional<Lot> lotAt(int x, int y) {
      return ofNullable(lines)
         .filter(ls -> ls.size() > y)
         .map(ls -> ls.get(y))
         .filter(lots -> lots.length > x)
         .map(lots -> lots[x]);
   }

   private boolean areAllEnemiesDefeated(Unit u) {
      // Is war over?
      final List<Unit> enemyUnits = unitsBySymbol.get(u.symbol() == 'E' ? 'G' : 'E')
         .stream().filter(e -> e.getHitPoints() > 0)
         .collect(toList());
      if (enemyUnits.isEmpty()) {
         if (DayFifteen.debug) {
            System.out.println("battle is over");
         }
         return true;
      }
      return false;
   }

   boolean warIsStillOn() {
      return unitsBySymbol.values().stream()
         // No team has no survivors yet.
         .noneMatch(l -> l.stream()
            .filter(Unit::isAlive)
            .findFirst()
            .isEmpty());
   }
}
