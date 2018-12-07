package com.github.georgespalding.adventofcode;

import static java.lang.System.out;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DaySeven {

   static final Map<String, List<String>> rawDag =
      Util.streamResource("7-s.lst")
         .map(DaySeven::parse)
         .collect(groupingBy(s -> s.substring(0, 1)))
      .entrySet().stream()
         .collect(toMap(
            Entry::getKey,
            e -> e.getValue().stream()
               //.peek(out::println)
               .map(s -> s.substring(1, 2))
               .sorted()
               .collect(toList())));

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
      Set<Node> downstreams = new HashSet<>(index.values());
      List<String> rootsInLayerOrder=new ArrayList<>();
      while(!downstreams.isEmpty()){
         final Set<Node> prevDownstream = downstreams;
         downstreams=downstreams(downstreams);
         final List<Node> roots = roots(prevDownstream, downstreams);
         final String sroots = roots.stream().map(Node::getName).collect(joining());
         rootsInLayerOrder.add(sroots);
         out.print(sroots);
         out.print(",");
      }
      out.println();

      out.println(
         rootsInLayerOrder.stream().mapToInt(String::length).sum()
      +": "+ String.join("", rootsInLayerOrder));
      out.println(index.values().size());
   }

   private static Set<Node> downstreams(Collection<Node> nodes) {
      return nodes.stream()
         .map(Node::getDownStream)
         .flatMap(Collection::stream)
         .collect(toSet());
   }

   private static List<Node> roots(Collection<Node> nodes, Collection<Node> downstreams) {
      return nodes.stream()
         .filter(k -> !downstreams.contains(k))
         .sorted()
         .collect(toList());
   }

   private static String parse(String line) {
      //Step E must be finished before step B can begin.
      return new String(new char[] { line.charAt(5), line.charAt(36) });
   }

   static class Node implements Comparable<Node>{

      private final List<Node> downStream = new ArrayList<>();
      private final List<Node> upStream = new ArrayList<>();
      private final String name;

      Node(String name) {
         this.name = name;
      }

      public String getName() {
         return name;
      }

      public List<Node> getDownStream() {
         return downStream;
      }

      public List<Node> getUpStream() {
         return upStream;
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

      @Override
      public int compareTo(Node o) {
         return name.compareTo(o.name);
      }
   }
}