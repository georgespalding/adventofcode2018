package com.github.georgespalding.adventofcode;

import static com.github.georgespalding.adventofcode.GuardEvent.Event.BeginShift;
import static com.github.georgespalding.adventofcode.GuardEvent.Event.FallAsleep;
import static com.github.georgespalding.adventofcode.GuardEvent.Event.WakeUp;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GuardShift {

   private final int guardId;
   private final LocalDate date;
   private final List<Nap> naps;


   GuardShift(GuardEvent guardEvent, List<GuardEvent> guardEvents) {
      assertType(guardEvent, BeginShift);
      guardId = ((GuardBeginShift) guardEvent).getGuardId();
      date = guardEvent.getDate();

      final Iterator<GuardEvent> iterator = guardEvents.iterator();

      final Stream.Builder<Nap> napBuilder = Stream.builder();
      while (iterator.hasNext()) {
         GuardEvent asleep = iterator.next();
         assertType(asleep, FallAsleep);

         assert iterator.hasNext() : "Expected sleep period";
         GuardEvent wakeup = iterator.next();
         assertType(wakeup, WakeUp);
         napBuilder.add(Nap.create(asleep.getTime(), wakeup.getTime()));
      }
      naps = napBuilder.build().collect(toList());
   }

   public GuardShift(List<GuardEvent> gles) {
      this(gles.get(0), gles.subList(1, gles.size()));
   }

   private void assertType(GuardEvent guardEvent, GuardEvent.Event expected) {
      assert guardEvent.getEvent() == expected : "Expected " + expected + ", got " + guardEvent.getEvent();
   }

   public int getGuardId() {
      return guardId;
   }

   public List<Nap> getNaps() {
      return naps;
   }

   public int totalNapMinutes(){
      return naps.stream().mapToInt(Nap::duration).sum();
   }

   @Override
   public String toString() {
      return String.format("GuardShift #%05d %s: %s",guardId,date, renderNaps());
   }

   public String renderNaps() {
      final StringBuilder rendering = new StringBuilder(60);
      naps.forEach(nap -> {
         IntStream.range(rendering.length(), nap.begin).forEach(i -> rendering.append(' '));
         rangeClosed(rendering.length(), nap.end).forEach(i -> rendering.append('Z'));
      });
      IntStream.range(rendering.length(), 60).forEach(i -> rendering.append(' '));
      return rendering.toString();
   }

}
