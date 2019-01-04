package com.github.georgespalding.adventofcode.eighteen;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public enum Use {
   open('.') {
      protected Use nextUse(Stream<Use> surrounding) {
         return nextUse(wooded, surrounding);
      }
   },
   wooded('|') {
      protected Use nextUse(Stream<Use> surrounding) {
         return nextUse(lumberyard, surrounding);
      }
   },
   lumberyard('#') {
      protected Use nextUse(Stream<Use> surrounding) {
         final AtomicBoolean haveLumb = new AtomicBoolean();
         final AtomicBoolean haveWood = new AtomicBoolean();
         surrounding.takeWhile(use -> {
            switch (use) {
               case wooded:
                  haveWood.set(true);
                  if (haveLumb.get()) {
                     return false;
                  } else {
                     break;
                  }
               case lumberyard:
                  haveLumb.set(true);
                  if (haveWood.get()) {
                     return false;
                  } else {
                     break;
                  }
            }
            return true;
         }).forEach(ignored -> {});
         return haveLumb.get() && haveWood.get()
            ? this
            : open;
      }
   },
   ;

   private final char symbol;

   Use(char symbol) {
      this.symbol = symbol;
   }

   static Use valueOf(char symbol) {
      return Arrays.stream(values())
         .filter(u -> u.symbol() == symbol)
         .findFirst()
         .orElseThrow(() -> new IllegalArgumentException("Unknownsymbol '" + symbol + "'"));
   }

   protected Use nextUse(Use become, Stream<Use> surrounding) {
      final AtomicInteger becomeCount = new AtomicInteger();
      surrounding.takeWhile(use -> {
         if (use == become) {
            return becomeCount.incrementAndGet() < 3;
         }
         return true;
      }).forEach(ignored -> {});
      return becomeCount.get() >= 3 ? become : this;
   }

   abstract Use nextUse(Stream<Use> surroundingUses);

   char symbol() {
      return symbol;
   }
}
