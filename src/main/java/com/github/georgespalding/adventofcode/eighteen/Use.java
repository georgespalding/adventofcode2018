package com.github.georgespalding.adventofcode.eighteen;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public enum Use {
   open('.') {
      protected Use nextUse(Map<Use, Integer> surroundingUseHistogram) {
         return surroundingUseHistogram.getOrDefault(wooded, 0) >= 3
            ? wooded
            : this;
      }
   },
   wooded('|') {
      protected Use nextUse(Map<Use, Integer> surroundingUseHistogram) {
         return surroundingUseHistogram.getOrDefault(lumberyard, 0) >= 3
            ? lumberyard
            : this;
      }
   },
   lumberyard('#') {
      protected Use nextUse(Map<Use, Integer> surroundingUseHistogram) {
         return surroundingUseHistogram.getOrDefault(lumberyard, 0) >= 1
            && surroundingUseHistogram.getOrDefault(wooded, 0) >= 1
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

   EnumMap<Use, Integer> usageHistogram(Stream<Use> uses) {
      return uses.collect(toMap(identity(), u -> 1, (a, b) -> a + b, () -> new EnumMap<>(Use.class)));
   }

   protected abstract Use nextUse(Map<Use, Integer> surroundingUseHistogram);

   Use nextUse(Stream<Use> surroundingUses) {
      return nextUse(usageHistogram(surroundingUses));
   }

   char symbol() {
      return symbol;
   }
}
