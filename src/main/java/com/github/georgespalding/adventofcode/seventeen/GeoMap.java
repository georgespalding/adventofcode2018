package com.github.georgespalding.adventofcode.seventeen;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;
import static java.util.stream.Stream.empty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

public class GeoMap {

   private final char[][] grid;
   private final int offsetX;
   private final int minY;
   private final Source spring;
   private final Map<Point, Source> index = new HashMap<>();
   private int round;

   GeoMap(char[][] grid, int offsetX, int minY) {
      this.grid = grid;
      spring = getOrCreateSource(new Point(500, 0));
      // prevent the spring from being counted as sand
      grid[spring.loc.getX() - offsetX][spring.loc.getY()] = '+';
      this.offsetX = offsetX;
      this.minY = minY;
   }

   Source getOrCreateSource(Point loc) {
      return index.computeIfAbsent(loc, p -> new Source(p));
   }

   void run() {
      // 23000 is too low
      // 31937 is too high
      // System.out.println(GeoMap.this.toString());
      while (spring.isNotDone()) {
         round++;
         spring.solve();
      }
      System.out.println("Round: " + round);
      //      System.out.println(this);
   }

   long[] countChars(char [] chars) {
      long[] res=new long[chars.length];
      final int length = grid[0].length;
      for (final char[] xRow : grid) {
         for (int y = minY; y < length; y++) {
            for (int i = 0; i < chars.length; i++) {
               if (chars[i] == xRow[y]) {
                  res[i]++;
                  break;
               }
            }
         }
      }
      return res;
   }

   @Override
   public String toString() {

      int width = grid.length;
      int height = Math.min(spring.maxY() + 30, grid[0].length);//grid[0].length;
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
         for (char[] chars : grid) {
            final char c = chars[y];
            // don't overwrite the well
            sb.append(c == 0 ? '.' : c);
         }
         sb.append('\n');
      }
      assert debugCap == sb.capacity() : "Failure to allocate big enough StringBuilder";
      return sb.toString();
   }

   private SwellEnd overflow(int x) {
      return new SwellEnd(x, true);
   }

   private SwellEnd wall(int x) {
      return new SwellEnd(x, false);
   }

   class Source {

      final Point loc;
      final Set<Source> parents = new HashSet<>();
      Source left;
      Source right;
      String nodeDone;

      Source(Point loc) {
         this.loc = loc;
      }

      Stream<Source> children() {
         return Stream.of(ofNullable(left), ofNullable(right))
            .flatMap(Optional::stream);
      }

      void nodeDone(String nodeDone) {
         //assert this.nodeDone == null : "Dup reasons for done: " + this.nodeDone + " and now " + nodeDone;
         this.nodeDone = nodeDone;
         System.out.println("Done: " + nodeDone);
      }

      void speedingToEternity() {
         nodeDone("eternity");
      }

      void flooded() {
         nodeDone("flooded");
         parents.forEach(parent -> {
            if (parent.left == this) {
               parent.left = null;
            } else if (parent.right == this) {
               parent.right = null;
            }
         });
         index.remove(loc);
      }

      boolean hasChildren() {
         return left != null || right != null;
      }

      boolean isNotDone() {
         return nodeDone == null;
      }

      /*
      har inte fyllt kärl
      har noder som dött
      har runnit över en eller flera gånger.

       */
      void solve() {
         round++;
         while (isNotDone()) {
            while (!hasChildren() && isNotDone()) {
               trickleDown();
            }
            if (left != null && left.isNotDone()) {
               left.solve();
            }
            if (right != null && right.isNotDone()) {
               right.solve();
            }
            if (hasChildren() && children().noneMatch(Source::isNotDone)) {
               nodeDone("Indeed children are done");
            }
         }
         //         System.out.println(GeoMap.this.toString());
      }

      void trickleDown() {
         final char[] colX = grid[loc.getX() - offsetX];
         if (colX[loc.y] == '~') {
            // Under water...
            flooded();
            return;
         }
         for (int y = loc.getY() + 1; y < colX.length; y++) {
            final char c = colX[y];
            switch (c) {
               default:
                  assert false : "What is this: " + c;
               case '~':
               case '#':
                  swellToTheSides(new Point(loc.getX(), y - 1));
                  return;
               case 0:
                  colX[y] = '|';
                  break;
               case '+':
               case '|':
                  break;
            }
         }
         // Speeding on to the center of the earth...
         speedingToEternity();
      }

      private void swellToTheSides(Point surfaceStart) {
         final int startX = surfaceStart.getX() - offsetX;
         final int Y = surfaceStart.getY();

         // search left
         final SwellEnd leftEnd = swellToTheSide(startX, Y, -1);
         // search right
         final SwellEnd rightEnd = swellToTheSide(startX, Y, 1);

         final char fill = leftEnd.overflow || rightEnd.overflow
            ? '|'
            : '~';
         rangeClosed(leftEnd.x, rightEnd.x).forEach(x -> grid[x][Y] = fill);

         leftEnd.stream(Y).map(GeoMap.this::getOrCreateSource).forEach(s -> {
            this.left = s;
            s.parents.add(this);
         });
         rightEnd.stream(Y).map(GeoMap.this::getOrCreateSource).forEach(s -> {
            this.right = s;
            s.parents.add(this);
         });
      }

      private SwellEnd swellToTheSide(int startX, int Y, int direction) {
         final int extremeX = direction > 0 ? grid[startX].length : 0;
         for (int edgeX = startX; edgeX != extremeX; edgeX += direction) {
            final char below = grid[edgeX][Y + 1];
            if (below != '#' && below != '~') {
               return overflow(edgeX);
            } else {
               char c = grid[edgeX + direction][Y];
               if (c == '#') {
                  return wall(edgeX);
               }
            }
         }
         throw new IllegalStateException("This Can't happen at x:" + startX + " direction:" + direction);
      }

      int maxY() {
         return Math.max(
            loc.getY(),
            children()
               .mapToInt(Source::maxY)
               .max()
               .orElse(-1));
      }

      @Override
      public int hashCode() {
         return Objects.hash(loc);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         }
         if (o == null || getClass() != o.getClass()) {
            return false;
         }
         Source source = (Source) o;
         return loc.equals(source.loc);
      }

      @Override
      public String toString() {
         return "Source{" +
            "loc=" + loc +
            ", nodeDone='" + nodeDone + '\'' +
            '}';
      }
   }

   class SwellEnd {

      final int x;
      final boolean overflow;

      SwellEnd(int x, boolean overflow) {
         this.x = x;
         this.overflow = overflow;
      }

      Stream<Point> stream(int y) {
         return overflow
            ? Stream.of(new Point(x + offsetX, y))
            : empty();
      }
   }
}
