package com.github.georgespalding.adventofcode.thirteen;

class MineCart implements Comparable<MineCart> {

   private Direction direction;
   private Track track;
   private int turns;

   MineCart(Direction direction) {this.direction = direction;}

   public void setTrack(Track track) {
      this.track = track;
   }

   boolean doTick() {
      final Track next = track.leaveAndGetNext();
      return next.accept(this);
   }

   public Direction getDirection() {
      return direction;
   }

   public void setDirection(Direction direction) {
      this.direction = direction;
   }

   @Override
   public String toString() {
      return direction.toString();
   }

   /**
    * left the first time,
    * goes straight the second time,
    * turns right the third time,
    * and then repeats those directions starting again with
    * left the fourth time,
    * straight the fifth time, and so on.
    */
   public void chooseAnyDirection() {
      switch (turns++ % 3) {
         case 0:
            // Turn left
            direction = Direction.values()[(Direction.SIZE + direction.ordinal() - 1) % Direction.SIZE];
            break;
         case 1:
         default:
            // Go straight
            break;
         case 2:
            direction = Direction.values()[(Direction.SIZE + direction.ordinal() + 1) % Direction.SIZE];
      }
   }

   @Override
   public int compareTo(MineCart o) {
      return track.getPos().compareTo(o.track.getPos());
   }

   String desc() {
      return toString() + " in " + track.desc();
   }
}
