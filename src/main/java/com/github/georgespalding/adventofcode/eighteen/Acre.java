package com.github.georgespalding.adventofcode.eighteen;

import java.util.ArrayList;
import java.util.List;

class Acre {

   final Point loc;
   private final List<Acre> surroundingAcres = new ArrayList<>();
   private Use oddUse;
   private Use evenUse;

   Acre(Use use, Point loc) {
      this.evenUse = use;
      this.loc = loc;
   }

   void adjacent(Acre adjacent) {
      surroundingAcres.add(adjacent);
   }

   Use nextUse(boolean isEven) {
      if (isEven) {
         return oddUse = evenUse.nextUse(surroundingAcres.stream().map(acre -> acre.evenUse));
      } else {
         return evenUse = oddUse.nextUse(surroundingAcres.stream().map(acre -> acre.oddUse));
      }
   }

   void connect(Acre acre) {
      surroundingAcres.add(acre);
      acre.surroundingAcres.add(this);
   }

   char toChar(boolean isEven) {
      return (isEven ? evenUse : oddUse).symbol();
   }

   Use getUse(boolean isEven) {
      return isEven ? evenUse : oddUse;
   }
}
