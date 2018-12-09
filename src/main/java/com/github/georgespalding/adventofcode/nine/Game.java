package com.github.georgespalding.adventofcode.nine;

import static java.lang.System.out;
import static java.util.OptionalLong.empty;
import static java.util.stream.IntStream.rangeClosed;
import static java.util.stream.LongStream.of;

import java.util.OptionalLong;

public class Game {

   private final Ring ring;
   private final long[] players;
   private int round = 0;

   Game(int numPlayers, int finalSize) {
      this.ring = new Ring(finalSize);
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
            ring.previous();
         }
         return OptionalLong.of(round + ring.remove());
      } else {
         ring.next();
         ring.add(round);
         return empty();
      }
   }

   @Override
   public String toString() {
      return String.format("[%2d]%s", round % players.length, ring);
   }
}
