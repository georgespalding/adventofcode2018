package com.github.georgespalding.adventofcode;

import static java.util.stream.IntStream.rangeClosed;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

class Nap {

   final int begin, end;

   Nap(int begin, int end) {
      this.begin = begin;
      this.end = end;
   }

   static Nap create(LocalDateTime begin, LocalDateTime wokeUp) {
      return new Nap(begin.getMinute(), wokeUp.getMinute() - 1);
   }

   int duration() {
      return end-begin;
   }

   IntStream sleepMinutes() {
      return rangeClosed(begin,end);
   }

}
