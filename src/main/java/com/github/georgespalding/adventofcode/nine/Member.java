package com.github.georgespalding.adventofcode.nine;

class Member {

   final int value;
   Member next;
   Member previous;

   Member(int value) {this.value = value;}

   void insert(Member previous, Member next) {
      this.next = next;
      this.next.previous = this;
      this.previous = previous;
      this.previous.next = this;
   }

   void remove() {
      this.next.previous = this.previous;
      this.previous.next = this.next;
   }

}
