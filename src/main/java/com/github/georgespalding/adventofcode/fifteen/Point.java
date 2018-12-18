package com.github.georgespalding.adventofcode.fifteen;

import static java.lang.Integer.compare;

import java.util.Objects;

public class Point implements Comparable<Point> {

   final int x;
   final int y;

   Point(int x, int y) {
      this.x = x;
      this.y = y;
   }

   int getX() {
      return x;
   }

   int getY() {
      return y;
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
      return String.format("{%2s,%2s}", x, y);
   }

   @Override
   public int compareTo(Point o) {
      final int yc = compare(y, o.y);
      return yc != 0
         ? yc
         : compare(x, o.x);
   }

}
