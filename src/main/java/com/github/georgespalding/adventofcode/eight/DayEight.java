package com.github.georgespalding.adventofcode.eight;

import static com.github.georgespalding.adventofcode.Util.getResource;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.nio.file.Files.newInputStream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.of;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.function.IntSupplier;

public class DayEight {

   private static final boolean debug = false;

   public static void main(String[] args) {
      final long load = currentTimeMillis();
      // For big files
      try (InputStream is = new BufferedInputStream(newInputStream(Paths.get(getResource("8.lst"))))) {

         Node root = new Node(streamNumbers(is));
         //         out.println(root);

         final long start = currentTimeMillis();
         long ans1 = root.accept(new MetadataSumVisitor()).sum;
         final long mid = currentTimeMillis();
         int ans2 = root.sum();
         final long end = currentTimeMillis();

         out.printf("Load: (%d ms)\n", start - load);
         out.printf("Ans1: %s (%d ms)\n", ans1, mid - start);
         out.printf("Ans2: %s (%d ms)\n", ans2, end - mid);
         out.printf("Total (%d ms)\n", end - start);

      } catch (Exception e) {
         throw new RuntimeException("Failed to Read Byte", e);
      }
   }

   private static IntSupplier streamNumbers(InputStream is) {
      // For big files
      return () -> {
         final StringBuilder sb = new StringBuilder();
         try {
            int read;
            while ((read = is.read()) != -1 && read != ' ') {
               sb.append((char) read);
            }

            return Integer.parseInt(sb.toString());
         } catch (IOException e) {
            System.err.println("SB: " + sb);
            throw new RuntimeException("Failed to Read Byte", e);
         }
      };
   }

   static class MetadataSumVisitor implements Visitor {

      long sum = 0;

      @Override
      public void visit(Node node) {
         final int sum = node.metadataEntries.stream().mapToInt(i -> i).sum();
         this.sum += sum;
         if (debug) {
            out.println("visit " + node.id + " own sum:" + sum + " total:" + this.sum);
         }
      }
   }

}
