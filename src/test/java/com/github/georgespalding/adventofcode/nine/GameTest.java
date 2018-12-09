package com.github.georgespalding.adventofcode.nine;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.OptionalLong;

public class GameTest {

   @Test
   public void testGames() {
      testGame(9, 25, 32);
      testGame(10, 1618, 8317);
      testGame(13, 7999, 146373);
      testGame(17, 1104, 2764);
      testGame(21, 6111, 54718);
      testGame(30, 5807, 37305);
   }

   public void testGame(int players, int lastMarble, int expectedWinningScore) {
      final Game game = new Game(players);
      final OptionalLong winningScore = game.getWinnerAtMarble(lastMarble);
      Assertions.assertThat(winningScore).isPresent();
      Assertions.assertThat(winningScore.getAsLong()).isEqualTo(expectedWinningScore);
   }
}