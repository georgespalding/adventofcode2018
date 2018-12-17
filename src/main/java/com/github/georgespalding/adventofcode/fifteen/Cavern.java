package com.github.georgespalding.adventofcode.fifteen;

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
                     int attackForce=3;
                     switch (symbol) {
                        case 'E':
                           attackForce=elfAttackForce;
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

   List<Unit> getElves(){
      return unitsBySymbol.get('E');
   }
   List<Unit> unitsInTurnOrder() {
      return unitsBySymbol.values().stream()
         .flatMap(Collection::stream)
         .filter(u -> u.getHitPoints() > 0)
         .sorted()
         .collect(toList());
   }

   boolean playRound() {
      List<Unit> unitsInTurnOrder = unitsInTurnOrder();
      return unitsInTurnOrder
         .stream()
         // Deferred filtering!!!
         .filter(u -> u.getHitPoints() > 0)
         .anyMatch(u -> {
            assert u.getHitPoints() > 0 : "Dead units don't fight";
            final List<Unit> enemyUnits = unitsBySymbol.get(u.symbol() == 'E' ? 'G' : 'E')
               .stream().filter(e -> e.getHitPoints() > 0)
               .collect(toList());
            // Is war over?
            if (enemyUnits.isEmpty()) {
               if(DayFifteen.debug)System.out.println("battle is over");
               return true;
            }
            // Any enemies within striking distance?
            if (!u.attemptAttack()) {
               final Lot lot = u.getLot();
               Optional<Lot> target = lot.bestPlaceToAttackEnemy();
               if (target.isPresent()) {
                  Optional<Lot> step = lot.bestStepToReach(target.get());
                  if (step.isPresent()) {
                     u.moveTo(step.get());
                     u.attemptAttack();
                  }
               }
            }
            return false;
         });
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
         sb.append("   ").append(Arrays.stream(ls)
            .filter(l -> l != null && l.occupier != null)
            .map(l -> l.occupier)
            .map(u -> u.symbol() + "(" + u.getHitPoints() + ")")
            .collect(Collectors.joining(", ")));
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
