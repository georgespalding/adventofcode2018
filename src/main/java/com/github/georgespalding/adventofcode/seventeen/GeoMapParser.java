package com.github.georgespalding.adventofcode.seventeen;

import static java.util.stream.Collectors.toList;

import com.github.georgespalding.adventofcode.LineParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GeoMapParser {

   final List<Line> lines = new ArrayList<>();

   public static GeoMap parse(Stream<String> lines) {
      final GeoMapParser p = new GeoMapParser();
      lines.forEach(p::accept);
      return p.build();
   }

   private GeoMap build() {
      List<Point> sorted = lines.stream().flatMap(Line::points)
         .sorted()
         .collect(toList());
      final int minX = sorted.stream().mapToInt(Point::getX).min().getAsInt() - 1;
      final int maxX = sorted.stream().mapToInt(Point::getX).max().getAsInt() + 1;
      final int maxY = sorted.stream().mapToInt(Point::getY).max().getAsInt() + 1;

      final int width = maxX - minX;
      final int offsetX = minX;
      final char[][] grid = new char[width][maxY];
      lines.forEach(l -> l.draw(grid, offsetX));
      return new GeoMap(grid, offsetX);
   }

   //x=495, y=2..7
   //y=7, x=495..501
   public void accept(String line) {
      final LineParser lp = new LineParser(line);
      final String xOrY = lp.next('=');
      final int pos = lp.nextInt(',');
      final String yOrX = lp.next('=');
      final int start = lp.nextInt('.');
      lp.next('.');
      final int end = lp.nextInt('<');

      if ("x".equals(xOrY)) {
         assert "y".equals(yOrX) : "Shit's fucked";
         lines.add(Line.vertical(pos, start, end));
      } else {
         assert "y".equals(xOrY) : "Shit's fucked";
         assert "x".equals(yOrX) : "Shit's fucked";
         lines.add(Line.horizontal(pos, start, end));
      }
   }
}
