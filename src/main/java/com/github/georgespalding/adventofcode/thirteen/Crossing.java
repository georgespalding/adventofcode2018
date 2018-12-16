package com.github.georgespalding.adventofcode.thirteen;

class Crossing extends Track {

   private Track north;
   private Track east;
   private Track south;
   private Track west;

   Crossing(Point pos, MineCart mineCart) {
      super(pos, mineCart);
   }

   boolean north(Track track) {
      this.north = track;
      return track.south(this);
   }

   boolean east(Track track) {
      this.east = track;
      return true;
   }

   boolean south(Track track) {
      this.south = track;
      return true;
   }

   boolean west(Track track) {
      this.west = track;
      return track.east(this);
   }

   @Override
   void align(MineCart cart) {
      cart.chooseAnyDirection();
   }

   @Override
   Track next(Direction direction) {
      switch (direction) {
         case North:
            return north;
         case East:
            return east;
         case South:
            return south;
         case West:
            return west;
         default:
            throw new IllegalArgumentException("Unexpected direction:" + direction);
      }
   }

   @Override
   public char symbol() {
      return '+';
   }
}
