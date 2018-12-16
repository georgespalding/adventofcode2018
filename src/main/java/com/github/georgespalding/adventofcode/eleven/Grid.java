package com.github.georgespalding.adventofcode.eleven;

import static com.github.georgespalding.adventofcode.Pair.fromEntry;
import static java.util.Comparator.comparingLong;
import static java.util.stream.LongStream.rangeClosed;

import com.github.georgespalding.adventofcode.Pair;

import java.util.Optional;
import java.util.stream.IntStream;

public class Grid {

   final int gridSerialNumber;
   final long[][] fuelCells;

   public Grid(int gridSerialNumber, int xDim, int yDim) {
      this.gridSerialNumber = gridSerialNumber;
      this.fuelCells = new long[xDim][yDim];
      IntStream.rangeClosed(1, yDim).forEach(y ->
         IntStream.rangeClosed(1, xDim).forEach(x ->
            fuelCells[x - 1][y - 1] = cellPower(gridSerialNumber, x, y)));
   }

   static long cellPower(int gridSerialNumber, int x, int y) {
      // Find the fuel cell's rack ID, which is its X coordinate plus 10.
      final long rackId = x + 10;
      return (((
                  // Begin with a power level of the rack ID times the Y coordinate.
                  rackId * y
                     // Increase the power level by the value of the grid serial number (your puzzle input).
                     + gridSerialNumber)
                  // Set the power level to itself multiplied by the rack ID.
                  * rackId)
                 //Keep only the hundreds digit of the power level (so 12345 becomes 3; numbers with no hundreds digit become 0).
                 % 1000) / 100
         // Subtract 5 from the power level.
         - 5;
   }

   long powerOfSquare(int x, int y, int dim) {
      return rangeClosed(y, y + dim - 1)
         .flatMap(yG ->
            rangeClosed(x, x + dim - 1)
               .map(xG ->
                  getCell((int) xG, (int) yG)))
         .sum();
   }

   Optional<Pair<String, Long>> findBestSquare() {
      return IntStream.rangeClosed(1, fuelCells.length)
         .peek(dim -> System.out.println("Dim:" + dim + " " + System.currentTimeMillis()))
         .mapToObj(this::findBestSquare)
         .filter(Optional::isPresent)
         .map(Optional::get)
         .max(comparingLong(Pair::getVal));

   }

   Optional<Pair<String, Long>> findBestSquare(int dim) {
      return IntStream.rangeClosed(1, fuelCells.length - (dim - 1))
         .mapToObj(y -> IntStream.rangeClosed(1, fuelCells.length - (dim - 1))
            .mapToObj(x -> fromEntry(dim + ": " + x + "," + y, powerOfSquare(x, y, dim))))
         .flatMap(s -> s)
         .max(comparingLong(Pair::getVal));
   }

   long getCell(int x, int y) {
      return fuelCells[x - 1][y - 1];
   }

}
