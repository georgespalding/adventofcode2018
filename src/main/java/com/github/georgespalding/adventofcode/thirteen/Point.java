package com.github.georgespalding.adventofcode.thirteen;

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
