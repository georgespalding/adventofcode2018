package com.github.georgespalding.adventofcode.thirteen;

class MineCart implements Comparable<MineCart> {

   private Direction direction;
   private Track track;
   private int turns;

   MineCart(Direction direction) {this.direction = direction;}

   void setTrack(Track track) {
      this.track = track;
   }

   boolean doTick() {
      track = track.doTick();
      return track.accept(this);
   }

   Direction getDirection() {
      return direction;
   }

   void setDirection(Direction direction) {
      this.direction = direction;
   }

   /**
    * left the first time,
    * goes straight the second time,
    * turns right the third time,
    * and then repeats those directions starting again with
    * left the fourth time,
    * straight the fifth time, and so on.
    */
   void chooseAnyDirection() {
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

   @Override
   public String toString() {
      return direction.toString();
   }

   Object getPos() {
      return track.getPos();
   }
}
