package com.github.georgespalding.adventofcode.nine;

class Member<T> {

   T value;
   Member<T> next;
   Member<T> previous;

   Member(T value) {this.value = value;}

   void insert(Member<T> previous, Member<T> next) {
      this.next = next;
      this.next.previous = this;
      this.previous = previous;
      this.previous.next = this;
   }

   void remove() {
      this.next.previous = this.previous;
      this.previous.next = this.next;
      free();
   }

   private void free() {

   }
}
