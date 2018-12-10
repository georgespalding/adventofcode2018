package com.github.georgespalding.adventofcode.ten;

import static java.lang.Long.compare;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.github.georgespalding.adventofcode.LineParser;

import java.util.Objects;

public class Point implements Comparable<Point> {

   private final long x;
   private final long y;

   Point(LineParser parser) {
      // yadda yadda=<-10569, -21315>
      parser.next('<');
      x = parser.nextInt(',');
      y = parser.nextInt('>');
   }

   Point(long x, long y) {
      this.x = x;
      this.y = y;
   }

   static Point box(Point a, Point b) {
      return new Point(
         max(a.x, b.x) - min(a.x, b.x),
         max(a.y, b.y) - min(a.y, b.y));
   }

   public long getX() {
      return x;
   }

   public long getY() {
      return y;
   }

   Point times(long scalarMultiplier) {
      return new Point(x * scalarMultiplier, y * scalarMultiplier);
   }

   @Override
   public int compareTo(Point o) {
      final int xc = compare(x, o.x);
      return xc != 0
         ? xc
         : compare(y, o.y);
   }

   Point add(Point other) {
      return new Point(x + other.x, y + other.y);
   }

   Point subtract(Point other) {
      return new Point(x - other.x, y - other.y);
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
      Point Point = (Point) o;
      return x == Point.x &&
         y == Point.y;
   }

   @Override
   public String toString() {
      return String.format("{%4d,%4d}", x, y);
   }

}
