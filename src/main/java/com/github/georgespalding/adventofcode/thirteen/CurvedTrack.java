package com.github.georgespalding.adventofcode.thirteen;

import static com.github.georgespalding.adventofcode.Pair.fromEntry;
import static com.github.georgespalding.adventofcode.thirteen.Direction.East;
import static com.github.georgespalding.adventofcode.thirteen.Direction.North;
import static com.github.georgespalding.adventofcode.thirteen.Direction.South;
import static com.github.georgespalding.adventofcode.thirteen.Direction.West;

import com.github.georgespalding.adventofcode.Pair;

class CurvedTrack extends Track {

   private Pair<Direction, Track> northSouth;
   private Pair<Direction, Track> eastWest;

   CurvedTrack(Point pos, MineCart cart) {
      super(pos, cart);
   }

   boolean north(Track track) {
      if (track.south(this)) {
         this.northSouth = Pair.fromEntry(North, track);
         return true;
      } else {
         return false;
      }
   }

   boolean east(Track track) {
      if (eastWest != null) {
         return false;
      } else {
         this.eastWest = fromEntry(East, track);
         return true;
      }
   }

   boolean south(Track track) {
      if (northSouth != null) {
         return false;
      } else {
         this.northSouth = fromEntry(South, track);
         return true;
      }
   }

   boolean west(Track track) {
      if (track.east(this)) {
         this.eastWest = Pair.fromEntry(West, track);
         return true;
      } else {
         return false;
      }
   }

   @Override
   void align(MineCart cart) {
      //TODO this requires some though.

      if (northSouth.getKey() == cart.getDirection()) {
         cart.setDirection(eastWest.getKey());
      } else if (eastWest.getKey() == cart.getDirection()) {
         cart.setDirection(northSouth.getKey());
      }
   }

   @Override
   Track next(Direction direction) {
      return null;
   }

   @Override
   public String symbol() {
      //     _
      //    /        |
      //    |       _/
      return ((northSouth.getKey() == South && eastWest.getKey() == East)
                 || (northSouth.getKey() == North && eastWest.getKey() == West))
         ? "/" : "\\";
   }

}
