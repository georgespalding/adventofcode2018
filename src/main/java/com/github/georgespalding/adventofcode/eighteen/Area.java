package com.github.georgespalding.adventofcode.eighteen;

import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Area {

   private final List<Acre[]> lines;
   private final byte[] reuse;
   private int minute;

   Area(Stream<String> cavernLines) {
      lines = new ArrayList<>();
      cavernLines
         .map(String::toCharArray)
         .forEach(ls -> {
            final int y = lines.size();
            lines.add(range(0, ls.length)
               .mapToObj(x -> new Acre(Use.valueOf(ls[x]), new Point(x, y)))
               .toArray(Acre[]::new));
         });

      // Connect lots
      lines.stream()
         .flatMap(Arrays::stream)
         .filter(Objects::nonNull)
         .forEach(this::connect);
      reuse = new byte[lines.size() * lines.get(0).length];
   }

   private void connect(Acre acre) {
      final Point loc = acre.loc;
      if (loc.y > 0) {
         final Acre[] aboveLine = lines.get(loc.y - 1);
         rangeClosed(
            loc.x - (loc.x > 0 ? 1 : 0),
            loc.x + (loc.x + 1 <= aboveLine.length - 1 ? 1 : 0))
            .mapToObj(i -> aboveLine[i])
            .forEach(acre::connect);
      }
      if (loc.x > 0) {
         acre.connect(lines.get(loc.y)[loc.x - 1]);
      }
   }

   @Override
   public String toString() {
      final boolean isEven = minute % 2 == 0;
      final StringBuilder sb = new StringBuilder();
      for (Acre[] line : lines) {
         for (Acre acre : line) {
            sb.append(acre.toChar(isEven));
         }
         sb.append('\n');
      }
      return sb.toString();
   }

   UUID nextMinute(boolean useResult) {
      final boolean isEven = minute % 2 == 0;
      IntStream.range(0, lines.size())
         //.parallel()
         .forEach(i -> {
            final Acre[] acres = lines.get(i);
            int length = acres.length;
            final int start = length * i;
            for (int j = 0; j < length; j++) {
               if (useResult) {
                  reuse[start + j] = (byte) acres[j].nextUse(isEven).symbol();
               } else {
                  acres[j].nextUse(isEven);
               }
            }
         });
      minute++;
      return useResult
         ? UUID.nameUUIDFromBytes(reuse)
         : UUID.randomUUID();
   }

   long count(Use use) {
      final boolean isEven = minute % 2 == 0;
      return lines.stream().flatMap(Arrays::stream)
         .map(a -> a.getUse(isEven))
         .filter(use::equals)
         .count();
   }

   public int minute() {
      return minute;
   }

   public void minute(int minute) {
      this.minute = minute;
   }
}
