package com.github.georgespalding.adventofcode;

import static com.github.georgespalding.adventofcode.GuardEvent.Event.FallAsleep;
import static com.github.georgespalding.adventofcode.GuardEvent.Event.WakeUp;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class LineParser {

   final String line;
   int pos = 0;

   LineParser(String line) {this.line = line;}

   String next(char delim) {
      int delimPos = line.indexOf(delim, pos);
      if (delimPos == -1) {
         delimPos = line.length();
      }
      try {
         return line.substring(pos, delimPos).trim();
      } finally {
         pos = delimPos + 1;
      }
   }

   int nextInt(char delim) {
      return Integer.parseInt(next(delim));
   }

   Claim parseClaim() {
      return new Claim(
         next('@'),
         new Rectangle(
            nextInt(','),
            nextInt(':'),
            nextInt('x'),
            nextInt('<')));
   }

   GuardEvent parseGuardLogEntry() {
      next('[');
      LocalDateTime timestamp = LocalDateTime.parse(next(' ') + "T" + next(']'));
      LocalDate datestamp = timestamp.truncatedTo(ChronoUnit.HOURS).plusHours(1).toLocalDate();
      next(' ');
      String remainder = next('#');
      switch (remainder) {
         case "falls asleep":
            return new GuardEvent(datestamp, timestamp, FallAsleep);
         case "wakes up":
            return new GuardEvent(datestamp, timestamp, WakeUp);
         case "Guard":
            return new GuardBeginShift(datestamp, timestamp, nextInt(' '));
      }
      throw new IllegalArgumentException("Could not parse '"+line+"'");
   }
}
