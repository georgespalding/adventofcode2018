package com.github.georgespalding.adventofcode;

import static java.lang.Integer.compare;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;
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

      System.out.println("Extremes: " + extremes);
      final Set<Point> remainingPoints = new HashSet<>(Arrays.asList(points));
      final Set<Point> edgePoints = new HashSet<>();

      for (Direction d : Direction.values()) {
         Point current = d.closest(remainingPoints.stream());
         Optional<Point> next;
         do {
            System.out.println(d + ": " + current);
            edgePoints.add(current);
            next = d.findNextClockWise(current, remainingPoints);
            if (next.isPresent()) {
               System.out.println("k: " + current.coefficient(next.get()));
               current = next.get();
            }
         } while (next.isPresent());
      }
      remainingPoints.removeAll(edgePoints);

      System.out.println("Edges:    " + edgePoints);
      System.out.println("Internal: " + remainingPoints);
      plotPoints(
         new Point(
            extremes.get(Direction.LEFT).getX(),
            extremes.get(Direction.DOWN).getY()),
         new Point(
            extremes.get(Direction.RIGHT).getX(),
            extremes.get(Direction.UP).getY()),
         edgePoints,
         remainingPoints);
   }

   private static void plotPoints(
      Point LOWERLEFT,
      Point UPPERRIGHT,
      Set<Point> edges,
      Set<Point> internal
   ) {
      for (int y = UPPERRIGHT.getY() + 1; y >= LOWERLEFT.getY() - 1; y--) {
         System.out.printf("%3s: %s-%s", y, LOWERLEFT.getX(), UPPERRIGHT.getX());
         for (int x = LOWERLEFT.getX() - 1; x < UPPERRIGHT.getX() + 1; x++) {
            final Point curr = new Point(x, y);

            if (edges.contains(curr)) {
               System.out.print("*");
            } else if (internal.contains(curr)) {
               System.out.print(".");
            } else {
               System.out.print(" ");
            }
         }
         System.out.println("|");
      }
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
            return remaining.filter(p -> current.getY() >= p.getY());
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

      final int x;
      final int y;

      Point(int x, int y) {
         this.x = x;
         this.y = y;
      }

      static Point parse(String line) {
         LineParser p = new LineParser(line);
         return new Point(p.nextInt(','), p.nextInt('<'));
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
            : yDiff > 0
               ? Double.POSITIVE_INFINITY
               : Double.NEGATIVE_INFINITY;
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
         return String.format("{ %3s, %3s}", x, y);
      }

      @Override
      public int compareTo(Point o) {
         final int xc = compare(x, o.x);
         return xc != 0
            ? xc
            : compare(y, o.y);
      }
   }
}
