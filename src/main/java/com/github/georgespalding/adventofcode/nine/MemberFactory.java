package com.github.georgespalding.adventofcode.nine;

public class MemberFactory {

   private final Member[] buffer;

   public MemberFactory(int size) {
      this.buffer = new Member[size+1];
      for (int i = 0; i < buffer.length; i++) {
         buffer[i]=new Member(i);
      }
   }

   Member create(int val){
      return buffer[val];
   }

}
