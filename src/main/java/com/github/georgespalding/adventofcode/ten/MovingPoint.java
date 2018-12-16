package com.github.georgespalding.adventofcode.ten;

import com.github.georgespalding.adventofcode.LineParser;

class MovingPoint {

   private final Point initial;
   private final Point velocity;

   //position=<-10569, -21315> velocity=< 1,  2>
   MovingPoint(String line) {
      final LineParser parser = new LineParser(line);
      initial = new Point(parser);
      velocity = new Point(parser);
   }

   Point positionAt(int time) {
      return initial.add(velocity.times(time));
   }
}
