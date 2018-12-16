package com.github.georgespalding.adventofcode.thirteen;

class StraightTrack extends Track {

   private final boolean horizontal;
   private Track first;
   private Track second;

   StraightTrack(Point pos, MineCart mineCart, boolean horizontal) {
      super(pos, mineCart);
      this.horizontal = horizontal;
   }

   boolean north(Track track) {
      if (!horizontal) {
         first = track;
         return track.south(this);
      } else {
         return false;
      }
   }

   boolean east(Track track) {
      if (horizontal) {
         second = track;
         return true;
      } else {
         return false;
      }
   }

   boolean south(Track track) {
      if (!horizontal) {
         second = track;
         return true;
      } else {
         return false;
      }
   }

   boolean west(Track track) {
      if (horizontal) {
         first = track;
         return track.east(this);
      } else {
         return false;
      }
   }

   @Override
   void align(MineCart cart) {
      // nothing to do, just go straight
   }

   @Override
   Track next(Direction direction) {
      switch (direction) {
         case East:
            assert horizontal : "Did not expect a cart entering sideways";
            return second;
         case West:
            assert horizontal : "Did not expect a cart entering sideways";
            return first;
         case North:
            assert !horizontal : "Did not expect a cart entering sideways";
            return first;
         case South:
            assert horizontal : "Did not expect a cart entering sideways";
            return second;
         default:
            return null;
      }
   }

   @Override
   public char symbol() {
      return horizontal ? '-' : '|';
   }
}
