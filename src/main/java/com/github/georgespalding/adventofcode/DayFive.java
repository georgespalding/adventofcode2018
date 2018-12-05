package com.github.georgespalding.adventofcode;

import static com.github.georgespalding.adventofcode.Util.getResource;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.nio.ByteBuffer.allocateDirect;
import static java.util.stream.IntStream.generate;
import static java.util.stream.IntStream.rangeClosed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;

public class DayFive {

   static final long load = currentTimeMillis();

   private static final int lf = (int) '\n';
   private static final int a = (int) 'a';
   private static final int A = (int) 'A';
   private static final int z = (int) 'z';
   private static final int Z = (int) 'Z';
   private static final int diff = a - A;
   private static final byte[] data;
   private static boolean printResults;

   static {
      try {
         data = Files.readAllBytes(Paths.get(getResource("5.lst")));
      } catch (Exception e) {
         throw new RuntimeException("Failed to Read Byte", e);
      }
   }

   private static IntSupplier dataStream() {
      // For big files
      /*
      try (InputStream is = Files.newInputStream(Paths.get(getResource("5.lst")))) {
         return () -> {
            try {
               return is.read();
            } catch (IOException e) {
               throw new RuntimeException("Failed to Read Byte", e);
            }
         };
      } catch (Exception e) {
         throw new RuntimeException("Failed to Read Byte", e);
      }*/

      // For small files
      final ByteArrayInputStream bais = new ByteArrayInputStream(data);
      return bais::read;
   }

   static void reduce(ByteBuffer byteBuffer, IntPredicate filter) {
      generate(dataStream())
         .takeWhile(i -> i != -1)
         .filter(filter)
         .collect(
            () -> byteBuffer,
            (bb, curr) -> {
               if (curr == lf) {
                  return;
               }
               final int pos = bb.position();
               if (pos > 0) {
                  final int prev = bb.get(pos - 1);
                  if (Math.abs(prev - curr) == diff) {
                     // zip these two
                     bb.position(pos - 1);
                     //System.out.println(": skip "+new String(new char[]{(char)prev,(char)curr}));
                     return;
                  }
               }
               //System.out.println(": append "+new String(new char[]{(char)curr}));
               bb.put((byte) curr);
            },
            ByteBuffer::put);
   }

   public static void main(String[] args) throws IOException {
      final long start = currentTimeMillis();

      final ByteBuffer buffer = allocateDirect(50001);
      reduce(buffer, (i) -> true);
      buffer.flip();
      final int ans1 = buffer.limit();

      if (printResults) {
         byte[] buff = new byte[buffer.limit()];
         buffer.get(buff);
         System.out.write(buff);
         out.println();
      }
      final long mid = currentTimeMillis();

      final String ans2 = rangeClosed(A, Z)
         .mapToObj(
            i -> {
               buffer.clear();
               reduce(buffer, c -> c != i && c != (i + diff));
               buffer.flip();
               return Pair.fromEntry(
                  new String(new char[] { (char) i }),
                  buffer.limit());
            })
         .min(Comparator.comparingInt(Pair::getVal))
         .map(Pair::toString)
         .orElse("N/A");

      final long end = currentTimeMillis();

      out.printf("Load: (%d ms)\n", start - load);
      out.printf("Ans1: %s (%d ms)\n", ans1, mid - start);
      out.printf("Ans2: %s (%d ms)\n", ans2, end - mid);
      out.printf("Total (%d ms)\n", end - start);
   }
}
