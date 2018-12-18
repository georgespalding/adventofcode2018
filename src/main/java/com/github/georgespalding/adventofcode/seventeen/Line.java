package com.github.georgespalding.adventofcode.seventeen;

import java.util.stream.Stream;

class Line {

   private final Point start;
   private final Point end;

   private Line(Point start, Point end) {
      this.start = start;
      this.end = end;
   }

   static Line horizontal(int y, int startX, int endX) {
      return new Line(new Point(startX, y), new Point(endX, y));
   }

   static Line vertical(int x, int startY, int endY) {
      return new Line(new Point(x, startY), new Point(x, endY));
   }

   Stream<Point> points() {
      return Stream.of(start, end);
   }

   void draw(char[][] grid, int offsetX) {
      for (int x = start.x-offsetX; x <= end.x-offsetX; x++) {
         for (int y = start.y; y <= end.y; y++) {
            grid[x][y] = '#';
         }
      }
   }
}
