package com.github.georgespalding.adventofcode.nine;

import static java.lang.String.format;

class Ring {

   private final MemberFactory factory;
   private Member head;
   private Member current;

   Ring(int finalSize) {this.factory = new MemberFactory(finalSize);}

   void add(int value) {
      final Member newCurrent = factory.create(value);
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

   int remove() {
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

}
