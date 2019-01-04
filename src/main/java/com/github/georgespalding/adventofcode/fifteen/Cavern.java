package com.github.georgespalding.adventofcode.fifteen;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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

   /**
    * You scan the area, generating a map of
    * the walls (#),
    * open cavern (.),
    * and starting position of every
    * Goblin (G) and
    * Elf (E) (your puzzle input).
    */
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
                     //Each unit, either Goblin or Elf, has 3 attack power...
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

   /**
    * For instance,
    * the order in which units take their turns within a round is the reading order of their starting positions in that round,
    * regardless of the type of unit or whether other units have moved after the round started.
    */
   List<Unit> unitsInTurnOrder() {
      return unitsBySymbol.values().stream()
         .flatMap(Collection::stream)
         .filter(Unit::isAlive)
         .sorted(Unit.turnOrder)
         .collect(toList());
   }

   /**
    * Combat proceeds in rounds;
    * in each round,
    * each unit that is still alive takes a turn,
    * resolving all of its actions before the next unit's turn begins.
    * On each unit's turn,
    * it tries to move into range of an enemy (if it isn't already)
    * and then attack (if it is in range).
    */
   boolean playRound(boolean debug, int round) {
      AtomicInteger curr = new AtomicInteger();
      final Iterator<Unit> uter = unitsInTurnOrder().iterator();
      boolean warOver = false;
      while (uter.hasNext() && !warOver) {
         final Unit u = uter.next();
         // Deferred filtering!!!
         if (!u.isAlive()) {
            continue;
         }

         assert u.getHitPoints() > 0 : "Dead units don't fight";
         // Any enemies within striking distance?
         List<Unit> adjacentEnemies = u.adjacentEnemiesInWeaknessOrder();
         if (adjacentEnemies.isEmpty()) {
            final Optional<Lot> bestMove = u.bestMoveToAttackEnemy();

            if (debug) {
               System.out.println("During " + round + ":" + curr.incrementAndGet() + " " + u + " -> " + bestMove);
               System.out.println(toString());
            }

            if (bestMove.isPresent()) {
               u.moveTo(bestMove.get());
               adjacentEnemies = u.adjacentEnemiesInWeaknessOrder();
            }
         }
         // After moving (or if the unit began its turn in range of a target),
         // the unit attacks.
         if (!adjacentEnemies.isEmpty()) {
            warOver = adjacentEnemies.stream()
               .findFirst()
               .map(u::attack)
               .map(tru -> areAllEnemiesDefeated(u))
               .orElse(false);
         } else{
         // If the unit cannot reach (find an open path to) any of the squares that are in range,
         // it ends its turn.
         }
      }
      return uter.hasNext();
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("   ");
      for (int x = 0; x < lines.get(0).length; x++) {
         sb.append(x%10);
      }
      sb.append('\n');
      AtomicInteger y = new AtomicInteger();
      lines.forEach(ls -> {
         sb.append(String.format("%02d ",y.getAndIncrement()));
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
         DayFifteen.debug("battle is over");
      }
      return enemyUnits.isEmpty();
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
