package com.github.georgespalding.adventofcode.twenty;

import static com.github.georgespalding.adventofcode.Util.getResource;

import com.github.georgespalding.adventofcode.template.Day;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.IntSupplier;

public class Day20 {

   static final boolean debug = true;
   private static final byte[] data;

   static {
      try {
         data = Files.readAllBytes(Paths.get(getResource("20/20.lst")));
      } catch (Exception e) {
         throw new RuntimeException("Failed to Read Byte", e);
      }
   }

   static volatile int pos=0;
   private static IntSupplier dataStream() {
      final ByteArrayInputStream bais = new ByteArrayInputStream(data);
      pos=0;
      return () -> {pos++;return bais.read();};
   }

   public static void main(String[] args) {
      final Day<Object, Object> day = new Day<>();
      final Grid grid = new Grid(100, dataStream());
      day.start();

      day.partOne(grid.shortestToFurthestRoom());
      System.out.println(grid);
      day.partTwo(grid.roomsAtLeastXDoorsAway(1000));

      day.output();
   }
}
