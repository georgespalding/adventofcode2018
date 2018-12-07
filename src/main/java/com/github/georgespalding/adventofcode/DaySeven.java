package com.github.georgespalding.adventofcode;

import static java.lang.System.out;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DaySeven {

   private static final Map<String, List<String>> rawDag =
      Util.streamResource("7-s.lst")
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
      {
         {

            // Allocate work
         }
         // Advance time until first worker becomes free
      }
      final List<Node> remaining = index.values().stream().sorted().collect(toList());
      final List<Node> done = new ArrayList<>();
      final List<Worker> workers = range(0, 2)
         .mapToObj(Worker::new)
         .collect(toList());
      AtomicInteger clock = new AtomicInteger();
      do {// While work remains
         final List<Node> unbloked = unblocked(done, remaining).collect(toList());
            unbloked.forEach(firstUnBlocked -> {// Find free node
               done.add(firstUnBlocked);
               remaining.remove(firstUnBlocked);
               // if worker available
               Worker nextAvailable = workers.stream().min(Comparator.comparingInt(Worker::getAvailableAfter)).get();
               if (!nextAvailable.idle(clock.get())) {
                  clock.set(nextAvailable.availableAfter);
               }
               nextAvailable.accept(firstUnBlocked, clock.get());
            });
         clock.set(workers.stream().filter(w->w.busy(clock.get())).mapToInt(Worker::getAvailableAfter).min().getAsInt());

      } while (!remaining.isEmpty());
      out.println(done);
      out.println(done.stream().map(Node::getName).collect(joining()));
      out.println("Took: " + workers.stream().mapToInt(Worker::getAvailableAfter).max() + "s");
   }

   private static Stream<Node> unblocked(List<Node> done, List<Node> remaining) {
      return remaining.stream()
         .filter(n -> n.notBlocked(done))
         .sorted();
   }

   static class Worker {

      final int id;
      int availableAfter;

      Worker(int id) {
         this.id = id;
      }

      boolean idle(int now) {
         return now >= availableAfter;
      }

      boolean busy(int now){
         return !idle(now);
      }
      void accept(Node job, int now) {
         assert idle(now) : "accepted job while busy";
         availableAfter = now + job.executionTime();
         out.println(id + ": " + now + " accept job " + job.name + " (" + job.executionTime() + ") available again at " + availableAfter);
      }

      public int getId() {
         return id;
      }

      int getAvailableAfter() {
         return availableAfter;
      }
   }

   static class Node implements Comparable<Node> {

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

      int executionTime() {
         return //60+
            1 +name.toCharArray()[0] - 'A';
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