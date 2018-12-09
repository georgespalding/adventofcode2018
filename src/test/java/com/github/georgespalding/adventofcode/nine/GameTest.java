package com.github.georgespalding.adventofcode.nine;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Optional;

public class GameTest {

   @Test
   public void testExamples() {
      final Game game = new Game(9);
      final Optional<Player> winner = game.getWinnerAtMarble(25);
      Assertions.assertThat(winner).isPresent();
      Assertions.assertThat(winner.get().score()).isEqualTo(32);
      Assertions.assertThat(winner.get().id).isEqualTo(5);
   }

   @Test
   public void testGames() {
      testGame(10, 1618, 8317);
      testGame(13, 7999, 146373);
      testGame(17, 1104, 2764);
      testGame(21, 6111, 54718);
      testGame(30, 5807, 37305);
   }

   public void testGame(int players, int lastMarble, int expectedWinningScore) {
      final Game game = new Game(players);
      final Optional<Player> winner = game.getWinnerAtMarble(lastMarble);
      Assertions.assertThat(winner).isPresent();
      Assertions.assertThat(winner.get().score()).isEqualTo(expectedWinningScore);
      //Assertions.assertThat(winner.get().id).isEqualTo(5);
   }
}