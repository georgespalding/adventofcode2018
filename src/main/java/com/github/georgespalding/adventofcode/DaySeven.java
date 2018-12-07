package com.github.georgespalding.adventofcode;

import static java.lang.System.out;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DaySeven {

   static final Map<String, Node<Node>> cache = new HashMap<>();
   static final Map<String, Node<String>> rawDag = Util.streamResource("7.lst")
      .map(DaySeven::parse)
      .collect(toList()).stream().collect(groupingBy(s -> s.substring(0, 1)))
      .entrySet().stream()
      .map(entry -> new Node<>(
         entry.getKey(),
         entry.getValue().stream()
            .peek(out::println)
            .map(s -> s.substring(1, 2)).collect(toList())))
      //.sorted(Comparator.comparing(Node::getName))
      .collect(toMap(Node::getName, identity()));

   public static void main(String[] args) {
      final List<Node<Node>> dag = rawDag.values().stream()
         .map(DaySeven::getOrCreate)
         .peek(out::println)
         .sorted(Comparator.comparing(Node::getName))
         .peek(out::println)
         .collect(toList());


   }

   static final Node<Node> getOrCreate(Node<String> node) {
      final List<Node> nodes = node.downStream.stream()
         .map(n -> getOrCreate(rawDag.computeIfAbsent(n, nn-> new Node<>(n))))
         .sorted(Comparator.comparing(Node::getName))
         .peek(x -> out.println(x.desc()))
         .collect(toList());
      return cache.computeIfAbsent(node.getName(), name->new Node<>(name, nodes));
   }

   static String parse(String line) {
      //Step E must be finished before step B can begin.
      return new String(new char[] { line.charAt(5), line.charAt(36) });
   }

   static class Node<T> {

      private final List<T> downStream;
      private final String name;

      Node(String name) {
         this(name, emptyList());
      }

      Node(String name, List<T> downStream) {
         this.name = name;
         this.downStream = downStream;
      }

      public String getName() {
         return name;
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

      public String desc() {
         return "Node: "
            + name
            + " -> " + downStream.stream().map(Object::toString).collect(joining(", "));
      }

      @Override
      public String toString() {
         return "Node{"+ name + '}';
      }
   }
}