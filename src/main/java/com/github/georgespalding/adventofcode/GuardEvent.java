package com.github.georgespalding.adventofcode;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GuardEvent {

   private final LocalDate date;
   private final LocalDateTime time;
   private final Event event;

   GuardEvent(
      LocalDate date,
      LocalDateTime time,
      Event event
   ) {
      this.date = date;
      this.time = time;
      this.event = event;
   }

   public LocalDate getDate() {
      return date;
   }

   public LocalDateTime getTime() {
      return time;
   }

   public Event getEvent() {
      return event;
   }

   enum Event {
      BeginShift,
      FallAsleep,
      WakeUp;
   }

}
