package com.github.georgespalding.adventofcode.thirteen;

import static java.util.Optional.ofNullable;

abstract class Track {

   private final Point pos;
   private MineCart cart;

   Track(Point pos, MineCart mineCart) {
      this.pos = pos;
      if (mineCart != null) {
         this.cart = mineCart;
         this.cart.setTrack(this);
      }
   }

   boolean accept(MineCart cart) {
      if (this.cart != null) {
         System.out.println("BOOM!!!");
         return false;
      } else {
         align(cart);
         this.cart = cart;
         return true;
      }
   }

   boolean north(Track track) {
      return false;
   }

   boolean east(Track track) {
      return false;
   }

   boolean south(Track track) {
      return false;
   }

   boolean west(Track track) {
      return false;
   }

   boolean hasCart() {
      return cart != null;
   }

   MineCart getCart() {
      return cart;
   }

   abstract void align(MineCart cart);

   abstract Track next(Direction direction);

   abstract String symbol();

   Track leaveAndGetNext() {
      try {
         return next(cart.getDirection());
      } finally {
         this.cart = null;
      }
   }

   public Point getPos() {
      return pos;
   }

   public String desc() {
      return toString() + "@" + pos;
   }

   @Override
   public String toString() {
      return ofNullable(cart)
         .map(MineCart::toString)
         .orElseGet(this::symbol);
   }
}

