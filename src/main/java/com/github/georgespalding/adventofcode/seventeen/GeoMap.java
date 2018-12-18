package com.github.georgespalding.adventofcode.seventeen;

import com.github.georgespalding.adventofcode.Pair;

import java.util.OptionalInt;

public class GeoMap {

   private final Point spring;
   private final char[][] grid;
   private final int offsetX;

   GeoMap(char[][] grid, int offsetX) {
      this.grid = grid;
      this.spring = new Point(500, 0);
      this.offsetX = offsetX;
   }

   OptionalInt trickleDown(Point source) {
      final int x = source.getX() - offsetX;
      char c;
      for (int y = source.getY(); y < grid[0].length; y++) {
         c = grid[x][y];
         switch (c) {
            default:
               System.out.println("What is this: " + c);
            case '~':
            case '#':
               return OptionalInt.of(y - 1);
            case 0:
            case '|':
            case '+':
               grid[x][y] = '|';
               break;
         }
      }
      // Speeding on to the center of the earth...
      return OptionalInt.empty();
   }

   void breadth() {
      // need left and right x pos
      // also indicate if x pos stopped due to a wall or emptiness outside
   }

   @Override
   public String toString() {

      int width = grid.length;
      int height = grid[0].length;
      final StringBuilder hdrs = new StringBuilder(width + 1);
      final StringBuilder tens = new StringBuilder(width + 1);
      final StringBuilder digs = new StringBuilder(width + 1);
      // header
      for (int x = offsetX; x < offsetX + width; x++) {
         hdrs.append(x / 100);
         tens.append((x % 100) / 10);
         digs.append(x % 10);
      }
      final StringBuilder sb = new StringBuilder((3 + height) * (width + 1));
      final int debugCap = sb.capacity();
      sb.append(hdrs).append("\n");
      sb.append(tens).append("\n");
      sb.append(digs).append("\n");

      for (int y = 0; y < height; y++) {
         final boolean springY = y == spring.getY();
         for (int x = 0; x < width; x++) {
            final char c = grid[x][y];
            if (springY && x == spring.getX() - offsetX) {
               sb.append('+');
            } else {
               sb.append(c == 0 ? '.' : c);
            }
         }
         sb.append('\n');
      }
      assert debugCap == sb.capacity() : "Failure to allocate big enough StringBuilder";
      return sb.toString();
   }
}
