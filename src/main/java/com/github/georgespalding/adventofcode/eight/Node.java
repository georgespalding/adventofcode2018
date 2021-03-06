package com.github.georgespalding.adventofcode.eight;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.concat;
import static java.util.stream.IntStream.of;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

public class Node {

   static int ID;
   final List<Node> childNodes;
   final List<Integer> metadataEntries;
   final String id = "#" + ID++;

   Node(IntSupplier input) {
      final int numChildNodes = input.getAsInt();
      final int numMetadataEntries = input.getAsInt();
      childNodes = range(0, numChildNodes)
         .mapToObj(i -> new Node(input))
         .collect(toList());
      assert numChildNodes == childNodes.size()
         : "numChildNodes == childNodes.size(): " + numChildNodes + "==" + childNodes.size();
      metadataEntries = range(0, numMetadataEntries)
         .map(i -> input.getAsInt())
         .boxed()
         .collect(toList());
      assert numMetadataEntries == metadataEntries.size()
         : "numMetadataEntries == metadataEntries.size(): " + numMetadataEntries + "==" + metadataEntries.size();
   }

   public IntStream serialize() {
      return concat(
         of(childNodes.size(), metadataEntries.size()),
         concat(
            childNodes.stream().flatMapToInt(Node::serialize),
            metadataEntries.stream().mapToInt(md -> md)));
   }

   int sum() {
      if (childNodes.isEmpty()) {
         // If a node has no child nodes, its value is the sum of its metadata entries.
         // So, the value of node B is 10+11+12=33, and the value of node D is 99.
         return metadataEntries.stream().mapToInt(i -> i).sum();
      } else {
         // If a node does have child nodes, the metadata entries become indexes which refer to those child nodes.
         return metadataEntries.stream()
            // A metadata entry of 1 refers to the first child node, 2 to the second, 3 to the third, and so on.
            .map(i -> i - 1)
            // If a referenced child node does not exist, that reference is skipped.
            // A metadata entry of 0 does not refer to any child node.
            .filter(i -> 0 <= i && i < childNodes.size())
            // A child node can be referenced multiple time and counts each time it is referenced.
            .map(childNodes::get)
            .mapToInt(Node::sum)
            // The value of this node is the sum of the values of the child nodes referenced by the metadata entries.
            .sum();
      }
   }

   @Override
   public String toString() {
      return serialize()
         .mapToObj(String::valueOf)
         .collect(joining(" "));
   }

   <V extends Visitor> V accept(V v) {
      v.visit(this);
      childNodes.forEach(n -> n.accept(v));
      return v;
   }
}
