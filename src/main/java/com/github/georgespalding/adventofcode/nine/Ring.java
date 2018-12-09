package com.github.georgespalding.adventofcode.nine;

import static java.lang.String.format;

class Ring<T> {

   private Member head;
   private Member current;

   void add(T value) {
      final Member newCurrent = new Member(value);
      if (current == null) {
         newCurrent.previous = newCurrent;
         newCurrent.next = newCurrent;
         head = newCurrent;
      } else {
         newCurrent.insert(current, current.next);
      }
      current = newCurrent;
   }

   void next() {
      current = current.next;
   }

   void previous() {
      current = current.previous;
   }

   T remove() {
      final Member remove = current;
      current = remove.next;
      remove.remove();

      return remove.value;
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      Member each = head;
      do {
         sb.append(format("%3s", (each == current ? "*" : "") + each.value));
         each = each.next;
      } while (each != head);
      return sb.toString();
   }

   private class Member {

      private final T value;
      Member next;
      Member previous;

      private Member(T value) {this.value = value;}

      private void insert(Member previous, Member next) {
         this.next = next;
         this.next.previous = this;
         this.previous = previous;
         this.previous.next = this;
      }

      private void remove() {
         this.next.previous = this.previous;
         this.previous.next = this.next;
      }
   }
}
