package com.github.georgespalding.adventofcode;

import static java.lang.Integer.compare;
import static java.lang.Math.abs;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;
import static java.util.Optional.empty;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.rangeClosed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DaySix {

   public static void main(String[] args) {
      final Point[] points = Util.streamResource("6.lst")
         .map(Point::parse)
         .sorted()
         .toArray(Point[]::new);

      EnumMap<Direction, Point> extremes = stream(Direction.values())
         .collect(toMap(
            identity(),
            d -> d.closest(stream(points)),
            (a, b) -> a,
            () -> new EnumMap<>(Direction.class)));

      out.println("Extremes: " + extremes);
      final Set<Point> remainingPoints = new HashSet<>(asList(points));
      final Set<Point> edgePoints = new HashSet<>();

      for (Direction d : Direction.values()) {
         Point current = d.closest(remainingPoints.stream());
         Optional<Point> next;
         do {
            out.println(d + ": " + current);
            edgePoints.add(current);
            next = d.findNextClockWise(current, remainingPoints);
            if (next.isPresent()) {
               out.println("k: " + current.coefficient(next.get()));
               final boolean sameRowOrColumn = current.getX() == next.get().getX() || current.getY() == next.get()
                  .getY();
               current = next.get();
               if (sameRowOrColumn) {
                  edgePoints.add(current);
                  next = empty();
               }
            }
         } while (next.isPresent());
      }
      remainingPoints.removeAll(edgePoints);

      out.println("Edges:    " + edgePoints);
      out.println("Internal: " + remainingPoints);
      final Point lower_left = new Point(
         extremes.get(Direction.LEFT).getX(),
         extremes.get(Direction.DOWN).getY(),
         "LOWER_LEFT");
      final Point upper_right = new Point(
         extremes.get(Direction.RIGHT).getX(),
         extremes.get(Direction.UP).getY(),
         "UPPER_RIGHT");
      //      plotPoints(
      //         lower_left,
      //         upper_right,
      //         edgePoints,
      //         remainingPoints,
      //         asList(points));
      out.println(remainingPoints.size());
      out.println("======================");

      calcDistances(lower_left, upper_right, asList(points));
   }

   private static void calcDistances(Point LOWERLEFT, Point UPPERRIGHT, List<Point> points) {
      final long region = rangeClosed(LOWERLEFT.getY(), UPPERRIGHT.getY())
         .mapToObj(y -> rangeClosed(LOWERLEFT.getX(), UPPERRIGHT.getX()).mapToObj(x -> new Point(x, y, " ")))
         .flatMap(identity())
         .map(loc -> Pair.fromEntry(loc, points.stream().mapToInt(loc::manhattanDistance).sum()))
         .sorted(comparingInt(p -> p.getVal()))
         //.peek(out::println)
         .takeWhile(p -> p.getVal() < 10000)
         .count();
      //.collect(Collectors.toList());
      out.println(region);
   }

   private static void plotPoints(
      Point LOWERLEFT,
      Point UPPERRIGHT,
      Set<Point> edges,
      Set<Point> internal,
      Collection<Point> all
   ) {
      Map<Point, List<Point>> closestPointsForPoint = new HashMap<>();
      //      for (int y = UPPERRIGHT.getY(); y >= LOWERLEFT.getY(); y--) {
      for (int y = LOWERLEFT.getY(); y <= UPPERRIGHT.getY(); y++) {
         out.printf("%3s: %s-%s: |", y, LOWERLEFT.getX(), UPPERRIGHT.getX());
         for (int x = LOWERLEFT.getX(); x <= UPPERRIGHT.getX(); x++) {
            final Point curr = new Point(x, y, " ");
            if (edges.contains(curr)) {
               out.print("x");
            } else if (internal.contains(curr)) {
               closestPointsForPoint.computeIfAbsent(curr, c -> new ArrayList<>())
                  .add(curr);
               out.print("+");
            } else {
               final int minDist = all.stream()
                  .mapToInt(curr::manhattanDistance)
                  .min()
                  .orElse(-1);
               final List<Point> closest = all.stream()
                  .filter(p -> minDist == curr.manhattanDistance(p))
                  .collect(Collectors.toList());
               switch (closest.size()) {
                  case 0:
                     throw new RuntimeException("Oops");
                  case 1:
                     final Point clos = closest.get(0);
                     closestPointsForPoint.computeIfAbsent(clos, c -> new ArrayList<>())
                        .add(new Point(x, y, clos.id.toLowerCase()));
                     out.print(clos.id);
                     break;
                  default:
                     //Collision
                     out.print("*");
               }
            }
         }
         out.println("|");
      }
      out.println();
      final ToIntFunction<Pair<Point, Integer>> getVal = Pair::getVal;
      internal.stream()
         .map(p -> Pair.fromEntry(p, closestPointsForPoint.getOrDefault(p, emptyList()).size()))
         .sorted(comparingInt(getVal).reversed())
         .forEach(out::println);
   }

   enum Direction {
      UP(Point::getY, false) {
         public Stream<Point> findNextClockWise(Point current, Stream<Point> remaining) {
            // Vi är längst upp, kolla till höger
            return remaining.filter(p -> current.getX() <= p.getX());
         }
      },
      RIGHT(Point::getX, false) {
         public Stream<Point> findNextClockWise(Point current, Stream<Point> remaining) {
            // Vi är längst till höger, kolla neråt
            return remaining
               .filter(p -> current.getY() >= p.getY())
               .filter(p -> current.getX() >= p.getY())
               ;
         }
      },
      DOWN(Point::getY, true) {
         public Stream<Point> findNextClockWise(Point current, Stream<Point> remaining) {
            // Vi är längst ner, kolla till vänster
            return remaining.filter(p -> current.getX() >= p.getX());
         }
      },
      LEFT(Point::getX, true) {
         public Stream<Point> findNextClockWise(Point current, Stream<Point> remaining) {
            // Vi är längst till vänster, kolla uppåt
            return remaining.filter(p -> current.getY() <= p.getY());
         }
      },
      ;

      //private final ToIntFunction<Point> extractor;
      private final Comparator<Point> comparator;

      Direction(
         ToIntFunction<Point> extractor,
         boolean reverse
      ) {
         //this.extractor = extractor;
         final Comparator<Point> comparator = comparingInt(extractor);
         this.comparator = reverse
            ? comparator.reversed()
            : comparator;
      }

      public Optional<Point> findNextClockWise(Point current, Set<Point> remaining) {
         return findNextClockWise(
            current,
            remaining.stream()
               .filter(p -> !current.equals(p)))
            .max(comparingDouble(current::coefficient));
      }

      public abstract Stream<Point> findNextClockWise(Point current, Stream<Point> remaining);

      public Point closest(Stream<Point> points) {
         return points.max(comparator)
            .orElseThrow(() -> new RuntimeException("No point closest to " + name()));
      }
   }

   static class Point implements Comparable<Point> {

      static final String ids = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
      static int ID = 0;
      final int x;
      final int y;
      final String id;
      Point(int x, int y, String id) {
         this.x = x;
         this.y = y;
         this.id = id;
      }

      static Point parse(String line) {
         LineParser p = new LineParser(line);
         return new Point(p.nextInt(','), p.nextInt('<'), ids.substring(ID++, ID));
      }

      int getX() {
         return x;
      }

      int getY() {
         return y;
      }

      double coefficient(Point other) {
         // k=(by-ay)/(bx-ax)
         int xDiff = other.x - x;
         int yDiff = other.y - y;
         return xDiff != 0
            ? yDiff / (double) xDiff
            : Double.POSITIVE_INFINITY;
      }

      @Override
      public int hashCode() {
         return Objects.hash(x, y);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         }
         if (o == null || getClass() != o.getClass()) {
            return false;
         }
         Point point = (Point) o;
         return x == point.x &&
            y == point.y;
      }

      @Override
      public String toString() {
         return String.format("%s:{ %3s, %3s}", id, x, y);
      }

      @Override
      public int compareTo(Point o) {
         final int xc = compare(x, o.x);
         return xc != 0
            ? xc
            : compare(y, o.y);
      }

      public int manhattanDistance(Point other) {
         return abs(x - other.x) + abs(y - other.y);
      }
   }
}
