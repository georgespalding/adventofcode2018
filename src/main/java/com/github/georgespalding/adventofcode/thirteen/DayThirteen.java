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
      dayNine.start();

      int iterations = 0;
      Optional<MineCart> firstCrash;
      do {
         if (debug) {
            System.out.println(drawTracks(cartsAndTracks.getVal()));
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
      do {
         if (debug) {
            System.out.println(drawTracks(cartsAndTracks.getVal()));
         }
         carts.stream()
            .filter(cart -> !cart.isCrashed())
            .sorted()
            .forEach(MineCart::doTick);
         iterations++;
      } while (carts.stream().filter(cart->!cart.isCrashed()).count()>1);
      dayNine.partTwo(carts.stream().filter(cart->!cart.isCrashed()).findFirst().map(MineCart::getPos).get());

      dayNine.output();
   }

   public static String drawTracks(LinkedList<Track[]> tracks) {
      final int X = tracks.stream().mapToInt(ts -> ts.length).max().getAsInt();
      final StringBuilder sb = new StringBuilder(tracks.size() * (1 + X));
      tracks.forEach(trackArr -> {
         stream(trackArr)
            .map(Optional::ofNullable)
            .map(o -> o.map(Track::toString).orElse(" "))
            .forEach(sb::append);
         sb.append('\n');
      });
      return sb.toString();
   }

}
