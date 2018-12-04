package com.github.georgespalding.adventofcode;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

public class GuardNaps {

   private final int id;
   private final List<Nap> naps;

   public GuardNaps(int id, List<Nap> naps) {
      this.id = id;
      this.naps = naps;
   }

   public static GuardNaps create(List<GuardShift> guardShifts) {
      return new GuardNaps(
         guardShifts.get(0).getGuardId(),
         guardShifts.stream()
            .map(GuardShift::getNaps)
            .flatMap(Collection::stream)
            .collect(toList()));
   }
}
