package com.github.georgespalding.adventofcode;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

public class DaySeven {

   static final boolean debug = false;
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
      final long load = currentTimeMillis();

      Map<String, Node> index = new HashMap<>();
      rawDag.forEach((nn, dss) -> {
         final Node node = index.computeIfAbsent(nn, Node::new);
         dss.forEach(ds -> {
            final Node dNode = index.computeIfAbsent(ds, Node::new);
            node.getDownStream().add(dNode);
            dNode.getUpStream().add(node);
         });
      });

      final long start = currentTimeMillis();

      final List<Node> remaining = index.values().stream().sorted().collect(toList());
      final List<Node> done = new ArrayList<>();
      final Clock clock = new Clock();
      final List<Worker> workers = range(0, 5)
         .mapToObj(i -> new Worker(i, clock))
         .collect(toList());

      // 1. While workers are idle, search for unblocked nodes until
      // 1.a no more workers available
      // 1.b no more unblocked nodes exist
      // 2. Advance time until the first busy worker is done, processing any scheduled events
      do {// While work remains
         final Optional<Node> nextUnblocked = unblocked(done, remaining)
            .findFirst();
         nextUnblocked
            .ifPresent(unblocked -> {// Found free node
               remaining.remove(unblocked);
               // if worker available
               Worker nextAvailable = workers.stream().min(Comparator.comparingInt(Worker::getAvailableAfter)).get();
               if (!nextAvailable.idle()) {
                  clock.advanceTo(nextAvailable.availableAfter, workers, done);
               }
               nextAvailable.accept(unblocked, () -> done.add(unblocked));
            });
         if (nextUnblocked.isEmpty()) {
            // There is nothing to do.
            final OptionalInt tick = workers.stream()
               .filter(Worker::busy)
               .mapToInt(Worker::getAvailableAfter)
               .min();
            if (tick.isPresent()) {
               clock.advanceTo(tick.getAsInt(), workers, done);
            }
         }
      } while (!remaining.isEmpty());
      final long mid = currentTimeMillis();
      final long end = currentTimeMillis();

      final int finishTime = workers.stream().mapToInt(Worker::getAvailableAfter).max().getAsInt();
      clock.advanceTo(finishTime, workers, done);

      out.printf("Load: (%d ms)\n", start - load);
      out.printf("Ans1: %s (%d ms)\n", done.stream().map(Node::getName).collect(joining()), mid - start);
      out.printf("Ans2: Takes %s s to complete (%d ms)\n", finishTime, end - mid);
      out.printf("Total (%d ms)\n", end - start);
   }

   private static Stream<Node> unblocked(List<Node> done, List<Node> remaining) {
      return remaining.stream()
         .filter(n -> n.notBlocked(done))
         .sorted();
   }

   private static class Clock {

      LinkedList<Pair<Integer, Runnable>> events = new LinkedList<>();
      int time;

      void advanceTo(int time, List<Worker> workers, List<Node> done) {
         range(this.time, time).forEach(t -> renderTick(t, workers, done));
         this.time = time;
         if (debug) {
            err.println(time + "s - Tock");
         }
         events.sort(Comparator.comparingInt(Pair::getKey));
         while (!events.isEmpty() && events.getFirst().getKey() <= time) {
            final Pair<Integer, Runnable> task = events.removeFirst();
            task.getVal().run();
            if (debug) {
               err.println(task.getKey() + "-" + time + "s - Ran task");
            }
         }
      }

      private void renderTick(int sec, List<Worker> workers, List<Node> done) {
         if (debug) {
            out.println(String.format("   %3d        ", sec)
               + workers.stream().map(worker -> worker.currentTask).collect(joining("        ")) + "   "
               + done.stream().map(n -> n.name).collect(toList()));
         }
      }

      void event(int when, Runnable task) {
         events.add(Pair.fromEntry(when, task));
      }

      int get() {
         return time;
      }
   }

   static class Worker {

      final int id;
      final Clock clock;
      int availableAfter;
      String currentTask = ".";

      Worker(int id, Clock clock) {
         this.id = id;
         this.clock = clock;
      }

      boolean idle() {
         return clock.get() >= availableAfter;
      }

      boolean busy() {
         return !idle();
      }

      void accept(Node job, Runnable post) {
         assert idle() : "accepted job while busy";
         availableAfter = clock.get() + job.executionTime();
         currentTask = job.name;
         if (debug) {
            err.println(clock.get() + "s - worker#" + id + " accept job " + job.name + " (" + job.executionTime() + ") available from " + availableAfter);
         }
         clock.event(availableAfter, () -> {
            currentTask = ".";
            post.run();
            if (debug) {
               err.println(clock.get() + "s - worker#" + id + " completed job " + job.name);
            }
         });
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
         return 60 +
            1 + name.toCharArray()[0] - 'A';
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