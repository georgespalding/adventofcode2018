package com.github.georgespalding.adventofcode.twenty;

import static java.lang.Math.abs;
import static java.util.Collections.singleton;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;

import com.github.georgespalding.adventofcode.fifteen.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grid {

   private final Element start;
   private final List<List<Element>> gridRows;
   private final Set<Element> reachableRooms = new HashSet<>();
   private final Element furthestRoom;
   private final List<Element> cornerOffices;

   Grid(int roomRadius, IntSupplier is) {
      int radius = 2 * roomRadius + 1;
      int siz = radius * 2 + 1;

      gridRows = new ArrayList<>(siz);
      Element tmpStart = null;
      for (int y = 0; y < siz; y++) {
         List<Element> currRow = new ArrayList<>(siz);
         for (int x = 0; x < siz; x++) {
            final Element e;
            if (x == y && x == radius) {
               tmpStart = e = new Element(0, 0, 'X');
            } else if (y % 2 == 0) {
               e = new Element(x - radius, y - radius, x % 2 == 0 ? '#' : '?');
            } else {
               e = new Element(x - radius, y - radius, x % 2 != 0 ? '.' : '?');
            }
            if (x > 0) {
               currRow.get(x - 1).westOf(e);
            }
            if (y > 0) {
               gridRows.get(y - 1).get(x).northOf(e);
            }
            currRow.add(e);
         }
         gridRows.add(currRow);
      }
      start = tmpStart;

      final Element end = parseRegex(start, is, false);

      // turn ? into #
      gridRows.stream().flatMap(Collection::stream)
         .filter(e -> e.symbol == '?')
         .forEach(e -> e.symbol = '#');

      // Prune rows
      final Element firstCompleteWallToTheNorth = findWallUpDown(e -> e.N);
      final Element firstCompleteWallToTheSouth = findWallUpDown(e -> e.S);

      Iterator<List<Element>> iterator = gridRows.iterator();
      boolean delete = true;
      while (iterator.hasNext()) {
         List<Element> next = iterator.next();
         if (next.contains(firstCompleteWallToTheNorth)) {
            delete = false;
         } else if (next.contains(firstCompleteWallToTheSouth)) {
            iterator.next();
            delete = true;
         }
         if (delete) {
            iterator.remove();
         }
      }
      // Prune columns
      pruneToEdge(true);
      pruneToEdge(false);

      List<Element> firstRoomRow = gridRows.get(1);
      List<Element> lastRoomRow = gridRows.get(gridRows.size() - 2);
      cornerOffices = Stream.of(
         firstRoomRow.get(1),
         firstRoomRow.get(firstRoomRow.size() - 2),
         lastRoomRow.get(1),
         lastRoomRow.get(lastRoomRow.size() - 2))
         .sorted()
         .collect(Collectors.toList());
      furthestRoom = cornerOffices.stream()
         .sorted(comparingInt(e -> -start.manhattanDistance(e)))
         .findFirst().get();
      if (Day20.debug) {
         // turn unreachable . into # (if there are any?)
         final long unreachableRooms = gridRows.stream().flatMap(Collection::stream)
            .filter(e -> e.symbol == '.')
            .filter(e -> !reachableRooms.contains(e))
            .peek(e -> System.out.println("Unreachable room:" + e))
            .peek(e -> e.symbol = '#')
            .count();
         System.out.println("Unreachable rooms:" + unreachableRooms);

         gridRows.get(0).get(0).symbol = '+';
         gridRows.get(0).get(gridRows.get(0).size() - 1).symbol = 'x';
         gridRows.get(gridRows.size() - 1).get(0).symbol = '*';
         gridRows.get(gridRows.size() - 1).get(gridRows.get(gridRows.size() - 1).size() - 1).symbol = '0';

         System.out.println("Corners:"
            + gridRows.get(0).get(0) + ", "
            + gridRows.get(0).get(gridRows.get(0).size() - 1) + ", "
            + gridRows.get(gridRows.size() - 1).get(0) + ", "
            + gridRows.get(gridRows.size() - 1).get(gridRows.get(gridRows.size() - 1).size() - 1));
         System.out.println("Corner offices:" + cornerOffices);
         System.out.println("Furthest room:" + furthestRoom);
      }
   }

   private void pruneToEdge(boolean fromStart) {
      final Function<Element, Element> direction = fromStart
         ? e -> e.W
         : e -> e.E;
      Element keep = findWall(direction, e -> e.N, e -> e.S);

      int distanceToEdge = 0;
      while (keep != null) {
         distanceToEdge++;
         keep = direction.apply(keep);
      }
      final int toDelete = distanceToEdge;
      gridRows.forEach(els -> {
         for (int i = 1; i < toDelete; i++) {
            els.remove(fromStart ? 0 : els.size() - 1);
         }
      });
   }

   private Element findWallUpDown(Function<Element, Element> direction) {
      return findWall(direction, e -> e.E, e -> e.W);
   }

   private Element findWall(
      Function<Element, Element> direction,
      Function<Element, Element> oneSide,
      Function<Element, Element> otherSide
   ) {
      Element firstCompleteWallinDirection = direction.apply(start);
      // Prune rows and cols and from the beginning
      while (!(search(firstCompleteWallinDirection, oneSide)
                  && search(firstCompleteWallinDirection, otherSide))) {
         firstCompleteWallinDirection = direction.andThen(direction).apply(firstCompleteWallinDirection);
      }
      return firstCompleteWallinDirection;
   }

   private boolean search(Element first, Function<Element, Element> next) {
      Element eastCheck = first;
      while (eastCheck != null) {
         if (eastCheck.symbol != '#') {
            return false;
         }
         eastCheck = next.apply(eastCheck);
      }
      return true;
   }

   private Element parseRegex(final Element curr, IntSupplier is, boolean inParen) {
      Element at = curr;
      int next;
      //List<Element> ends = new ArrayList<>();
      out:
      while ((next = is.getAsInt()) != -1) {
         char symbol = (char) next;
         switch (symbol) {
            case 'N':
            case 'S':
            case 'E':
            case 'W':
               at = at.go(symbol);
               reachableRooms.add(at);
               break;
            case '|':
               assert inParen : "At pos " + Day20.pos + ";Got | expr while NOT in paren";
               //ends.add(at);
               // start over from pos we were at when we hit the paren
               at = curr;
               break;
            case '(':
               at = parseRegex(at, is, true);
               break;
            case ')':
               assert inParen : "At pos " + Day20.pos + ";Got ) expr while NOT searching for )";
               break out;
            case '$':
               assert !inParen : "At pos " + Day20.pos + ";End of expr while searching for )";
               break out;
            case '^':
               break;
            default:
               throw new AssertionError("Unhandled symbol: '" + symbol + "'");
         }
      }

      //assert ends.stream().distinct().count() <= 1 :  "At pos " + Day20.pos + ";Expected all detours to end up in the same place:" + ends;
      return at;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      gridRows.forEach(res -> {
         res.stream().map(e -> e.symbol).forEach(sb::append);
         sb.append('\n');
      });
      return sb.toString();
   }

   Map<Element, Integer> calcMinDoorsPassedToEachRoom() {
      final Set<Element> notVisited = new HashSet<>(reachableRooms);
      final Map<Element, Integer> roomDistances = new HashMap<>();
      Set<Element> edgeRooms = singleton(start);
      int steps = 0;
      while (!notVisited.isEmpty()) {
         Set<Element> newEdgeRooms = edgeRooms.stream()
            .flatMap(Element::adjacentRooms)
            .filter(notVisited::contains)
            .collect(Collectors.toSet());
         notVisited.removeAll(newEdgeRooms);
         steps++;
         for (Element r : newEdgeRooms) {
            roomDistances.put(r, steps);
         }
         edgeRooms = newEdgeRooms;
      }
      return roomDistances;
   }

   Map.Entry<Integer, List<Element>> shortestToFurthestRoom() {
      Map<Element, Integer> roomDist = calcMinDoorsPassedToEachRoom();
      return reachableRooms.stream()
         .collect(groupingBy(roomDist::get))
         .entrySet().stream()
         .sorted(comparingInt(each -> -each.getKey()))
         .findFirst().get();
   }

   class Element implements Comparable<Element> {

      private final Point loc;
      char symbol;
      Element N, S, E, W;

      Element(int x, int y, char symbol) {
         this.symbol = symbol;
         this.loc = new Point(x, y);
      }

      void westOf(Element e) {
         E = e;
         e.W = this;
      }

      void northOf(Element e) {
         S = e;
         e.N = this;
      }

      Element go(char next) {
         switch (next) {
            case 'N':
               N.symbol = '-';
               return N.N;
            case 'S':
               S.symbol = '-';
               return S.S;
            case 'E':
               E.symbol = '|';
               return E.E;
            case 'W':
               W.symbol = '|';
               return W.W;
            default:
               throw new AssertionError("Expected '" + next + "' to be N,S,E,W.");
         }
      }

      @Override
      public String toString() {
         return symbol + "@" + loc;
      }

      public int manhattanDistance(Element other) {
         return abs(loc.x - other.loc.x) + abs(loc.y - other.loc.y);
      }

      @Override
      public int compareTo(Element o) {
         return loc.compareTo(o.loc);
      }

      Stream<Element> adjacentRooms() {
         Stream.Builder<Element> b = Stream.builder();
         if (N.symbol == '-') {
            b.add(N.N);
         }
         if (S.symbol == '-') {
            b.add(S.S);
         }
         if (E.symbol == '|') {
            b.add(E.E);
         }
         if (W.symbol == '|') {
            b.add(W.W);
         }
         return b.build();
      }
   }
}
