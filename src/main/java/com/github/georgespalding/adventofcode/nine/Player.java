package com.github.georgespalding.adventofcode.nine;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Player {

   final int id ;
   final List<List<Integer>> wonMarbles = new LinkedList<>();

   public Player(int id) {this.id = id;}

   void acceptMarbles(List<Integer> marbles) {
      // TODO most likely an empty list
      wonMarbles.add(marbles);
   }

   int score() {
      return wonMarbles.stream()
         .flatMap(Collection::stream)
         .mapToInt(i -> i).sum();
   }

   @Override
   public String toString() {
      return "Player#"+id+": "+score();
   }
}
