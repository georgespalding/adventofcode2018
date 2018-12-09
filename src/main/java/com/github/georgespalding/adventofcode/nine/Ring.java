package com.github.georgespalding.adventofcode.nine;

import static java.lang.String.format;

class Ring<T> {

   private Member<T> head;
   private Member<T> current;
   private int size;

   void add(T value) {
      final Member<T> newCurrent = new Member<>(value);
      if (current == null) {
         newCurrent.previous = newCurrent;
         newCurrent.next = newCurrent;
         head = newCurrent;
      } else {
         newCurrent.insert(current, current.next);
      }
      current = newCurrent;
      size++;
   }

   boolean next() {
      current = current.next;
      return current == head;
   }

   boolean previous() {
      current = current.previous;
      return current == head;
   }

   T remove() {
      final Member<T> remove = current;
      current = remove.next;
      remove.remove();

      return remove.value;
   }

   public int size() {
      return size;
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

}
