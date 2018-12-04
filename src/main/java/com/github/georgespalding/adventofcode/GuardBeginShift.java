package com.github.georgespalding.adventofcode;

import static com.github.georgespalding.adventofcode.GuardEvent.Event.BeginShift;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GuardBeginShift extends GuardEvent {

   private final int guardId;

   GuardBeginShift(
      LocalDate date,
      LocalDateTime time,
      int guardId
   ) {
      super(date, time, BeginShift);
      this.guardId = guardId;
   }

   public int getGuardId() {
      return guardId;
   }
}
