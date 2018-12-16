package com.github.georgespalding.adventofcode;

import java.awt.*;
import java.util.Objects;

class Claim {

   final String id;
   final Rectangle rect;

   Claim(String id, Rectangle rect) {
      this.id = id;
      this.rect = rect;
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      Claim claim = (Claim) o;
      return id.equals(claim.id);
   }

   @Override
   public String toString() {
      return "Claim{id='" + id + "'}";
   }

}
