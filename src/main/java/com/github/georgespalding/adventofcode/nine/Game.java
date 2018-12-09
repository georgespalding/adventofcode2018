package com.github.georgespalding.adventofcode.nine;

import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparingLong;
import static java.util.OptionalLong.empty;
import static java.util.OptionalLong.of;
import static java.util.stream.IntStream.rangeClosed;

import java.util.Optional;
import java.util.OptionalLong;

public class Game {

   private final Ring<Integer> ring = new Ring<>();
   private final Player[] players;
   private int round = 0;

   Game(int numPlayers) {
      ring.add(0);
      players = rangeClosed(1, numPlayers)
         .mapToObj(Player::new)
         .toArray(Player[]::new);
   }

   Optional<Player> getWinnerAtMarble(int lastMarble) {
      rangeClosed(1, lastMarble).forEach(marble -> {
         round++;
         makeMove()
            .ifPresent(winnings ->
               currentPlayer().addWinnings(winnings));
         if (DayNine.debug) {
            out.println(this);
         }
      });

      return stream(players).max(comparingLong(Player::winnings));
   }

   private Player currentPlayer() {
      return players[(round-1) % players.length];
   }

   private OptionalLong makeMove() {
      if (round % 23 == 0) {
         for (int i = 0; i < 7; i++) {
            if (ring.previous()) {
               out.println("Backed up to head at move " + round + " ring size:" + ring.size());
            }
         }
         return of(round + ring.remove());
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
      return String.format("[%2d]%s", currentPlayer().id, ring);
   }
}
