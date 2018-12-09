package com.github.georgespalding.adventofcode.nine;

import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparingInt;
import static java.util.stream.IntStream.rangeClosed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
      for (int marble = 1; marble <= lastMarble; marble++) {
         turn();
         if (DayNine.debug) {
            out.println(this);
         }
      }

      return players().max(comparingInt(Player::score));
   }

   public void turn() {
      round++;
      final List<Integer> wonMarbles = makeMove();
      if (!wonMarbles.isEmpty()) {
         currentPlayer().acceptMarbles(wonMarbles);
      }
   }

   public Player currentPlayer(){
      return players[(round-1) % players.length];
   }
   private List<Integer> makeMove() {
      if (round % 23 == 0) {
         final List<Integer> points = new ArrayList<>(2);
         points.add(round);
         for (int i = 0; i < 7; i++) {
            ring.previous();
         }
         points.add(ring.remove());
         return unmodifiableList(points);
      } else {
         ring.next();
         ring.add(round);
         return Collections.emptyList();
      }
   }

   public Stream<Player> players() {
      return stream(players);
   }

   @Override
   public String toString() {
      return String.format("[%2d]%s", currentPlayer().id, ring);
   }
}
