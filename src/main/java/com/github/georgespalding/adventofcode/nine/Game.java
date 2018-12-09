package com.github.georgespalding.adventofcode.nine;

import static java.lang.System.out;
import static java.util.OptionalLong.empty;
import static java.util.OptionalLong.of;
import static java.util.stream.IntStream.rangeClosed;
import static java.util.stream.LongStream.of;

import java.util.OptionalLong;
import java.util.stream.LongStream;

public class Game {

   private final Ring<Integer> ring = new Ring<>();
   private final long[] players;
   private int round = 0;

   Game(int numPlayers) {
      ring.add(0);
      players = new long[numPlayers];
   }

   OptionalLong getWinnerAtMarble(int lastMarble) {
      rangeClosed(1, lastMarble).forEach(marble -> {
         round++;
         makeMove()
            .ifPresent(winnings ->
               players[(round - 1) % players.length] += winnings);
         if (DayNine.debug) {
            out.println(this);
         }
      });

      return of(players).max();
   }

   private OptionalLong makeMove() {
      if (round % 23 == 0) {
         for (int i = 0; i < 7; i++) {
            if (ring.previous()) {
               out.println("Backed up to head at move " + round + " ring size:" + ring.size());
            }
         }
         return OptionalLong.of(round + ring.remove());
      } else {
         if (ring.next()) {
            out.println("Passed head at move " + round + " ring size:" + ring.size());
         }
         ring.add(round);
         return empty();
      }
   }

   @Override
   public String toString() {
      return String.format("[%2d]%s", round % players.length, ring);
   }
}
