package com.github.georgespalding.adventofcode.thirteen;

public enum Direction {
   North("^"),
   East(">"),
   South("v"),
   West("<");
   static final int SIZE = values().length;
   private final String sympol;

   Direction(String symbol) {
      this.sympol = symbol;
   }

   @Override
   public String toString() {
      return sympol;
   }
}
