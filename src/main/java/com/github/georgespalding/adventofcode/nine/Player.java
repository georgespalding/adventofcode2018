package com.github.georgespalding.adventofcode.nine;

public class Player {

   final int id ;
   private long winnings;

   public Player(int id) {this.id = id;}

   long winnings() {
      return winnings;
   }

   @Override
   public String toString() {
      return "Player#" + id + ": " + winnings();
   }

   public void addWinnings(long winnings) {
      this.winnings += winnings;
   }
}
