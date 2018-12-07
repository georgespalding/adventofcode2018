package com.github.georgespalding.adventofcode;

import static java.lang.System.out;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class DaySeven {

   private static final Map<String, List<String>> rawDag =
      Util.streamResource("7.lst")
         .map(DaySeven::parse)
         .collect(groupingBy(s -> s.substring(0, 1)))
      .entrySet().stream()
         .collect(toMap(
            Entry::getKey,
            e -> e.getValue().stream()
               .map(s -> s.substring(1, 2))
               .sorted()
               .collect(toList())));

   private static String parse(String line) {
      //Step E must be finished before step B can begin.
      return new String(new char[] { line.charAt(5), line.charAt(36) });
   }

   public static void main(String[] args) {
      Map<String, Node> index = new HashMap<>();
      rawDag.forEach((nn, dss) -> {
         final Node node = index.computeIfAbsent(nn, Node::new);
         dss.forEach(ds -> {
            final Node dNode = index.computeIfAbsent(ds, Node::new);
            node.getDownStream().add(dNode);
            dNode.getUpStream().add(node);
         });
      });
      final List<Node> remaining = index.values().stream().sorted().collect(toList());
      final List<Node> done = new ArrayList<>();
      do {
         remaining.stream()
            .filter(n -> n.notBlocked(done)).sorted()
            .findFirst()
            .ifPresent(firstUnBlocked -> {
               done.add(firstUnBlocked);
               remaining.remove(firstUnBlocked);
            });
      } while (!remaining.isEmpty());
      out.println(done);
      out.println(done.stream().map(Node::getName).collect(joining()));
   }

   static class Node implements Comparable<Node>{

      private final List<Node> downStream = new ArrayList<>();
      private final List<Node> upStream = new ArrayList<>();
      private final String name;

      Node(String name) {
         this.name = name;
      }

      String getName() {
         return name;
      }

      List<Node> getDownStream() {
         return downStream;
      }

      List<Node> getUpStream() {
         return upStream;
      }

      boolean notBlocked(List<Node> done) {
         return done.containsAll(upStream);
      }

      @Override
      public int hashCode() {
         return Objects.hash(name);
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) {
            return true;
         }
         if (o == null || getClass() != o.getClass()) {
            return false;
         }
         Node node = (Node) o;
         return name.equals(node.name);
      }

      @Override
      public String toString() {
         return "{" + name + ": " + downStream.stream().map(Node::getName).collect(joining()) + "}";
      }

      @Override
      public int compareTo(Node o) {
         return name.compareTo(o.name);
      }
   }
}