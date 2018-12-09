package com.github.georgespalding.adventofcode.nine;

public class Player {

   final int id ;
   private int winnings;

   public Player(int id) {this.id = id;}

   int winnings() {
      return winnings;
   }

   @Override
   public String toString() {
      return "Player#" + id + ": " + winnings();
   }

   public void addWinnings(int winnings) {
      this.winnings += winnings;
   }
}
