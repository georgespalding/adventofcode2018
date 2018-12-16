package com.github.georgespalding.adventofcode.thirteen;

import static java.util.Arrays.stream;

import com.github.georgespalding.adventofcode.Pair;
import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DayThirteen {

   static final boolean debug = false;

   public static void main(String[] args) {
      final Day<Object, Object> dayNine = new Day<>();

      final Pair<List<MineCart>, LinkedList<Track[]>> cartsAndTracks = TrackParser.parse(Util.streamResource("13.lst"));
      final List<MineCart> carts = cartsAndTracks.getKey();
      final LinkedList<Track[]> tracks = cartsAndTracks.getVal();
      dayNine.start();

      int iterations = 0;
      Optional<MineCart> firstCrash;
      do {
         if (debug) {
            System.out.println(drawTracks(tracks));
         }
         firstCrash = carts.stream().sorted()
            .filter(cart -> !cart.doTick())
            .findFirst();
         iterations++;
      } while (firstCrash.isEmpty());
      if (debug) {
         System.out.println("iterations:" + iterations);
      }
      dayNine.partOne(firstCrash.get().getPos());
      dayNine.partTwo("TODO Part2");

      dayNine.output();
   }

   public static String drawTracks(LinkedList<Track[]> tracks) {
      final int X = tracks.stream().mapToInt(ts -> ts.length).max().getAsInt();
      final StringBuilder sb = new StringBuilder(tracks.size() * (1 + X));
      tracks.forEach(trackArr -> {
         stream(trackArr)
            .map(Optional::ofNullable)
            .map(o -> o.map(Track::symbol).orElse(' '))
            .forEach(sb::append);
         sb.append('\n');
      });
      return sb.toString();
   }

}
