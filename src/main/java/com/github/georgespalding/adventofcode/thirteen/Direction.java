package com.github.georgespalding.adventofcode.thirteen;

public enum Direction {
   North("^") {
      @Override
      Direction opposite() {
         return South;
      }
   },
   East(">") {
      @Override
      Direction opposite() {
         return West;
      }
   },
   South("v") {
      @Override
      Direction opposite() {
         return North;
      }
   },
   West("<") {
      @Override
      Direction opposite() {
         return East;
      }
   };
   static final int SIZE = values().length;
   private final String sympol;

   Direction(String symbol) {
      this.sympol = symbol;
   }

   abstract Direction opposite();

   @Override
   public String toString() {
      return sympol;
   }
}
