package com.github.georgespalding.adventofcode.nine;

import static java.lang.String.format;

class Ring<T> {

   private Member<T> head;
   private Member<T> current;

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
   }

   void next() {
      current = current.next;
   }

   void previous() {
      current = current.previous;
   }

   T remove() {
      final Member<T> remove = current;
      current = remove.next;
      remove.remove();

      return remove.value;
   }

   T removeFreeMem() {
      final Member<T> remove = current;
      current = remove.next;
      remove.remove();
      if (head!=null && stepsForwardToHead() > 46) {
         current.previous = null;
         head.previous = null;
         head = null;
      }
      if(head==null){
         current.previous=null;
      }

      //      remove.previous.next=nul;
      //      remove.next=null;
      //      remove.previous=null;

      return remove.value;
   }

   private int stepsForwardToHead() {
      Member here = current;
      int dist = 0;
      while (here != head) {
         dist++;
         here = here.next;
      }
      System.out.println("stepsForwardToHead:" + dist);
      return dist;
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

   private static class Member<T> {

      final T value;
      Member<T> next;
      Member<T> previous;

      Member(T value) {this.value = value;}

      void insert(Member<T> previous, Member<T> next) {
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
