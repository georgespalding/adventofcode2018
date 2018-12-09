package com.github.georgespalding.adventofcode.ten;

import com.github.georgespalding.adventofcode.LineParser;

import java.awt.*;

public class MovingPoint {
   private final int initialX;
   private final int initialY;
   private final int velocityX;
   private final int velocityY;

   //position=<-10569, -21315> velocity=< 1,  2>
   public MovingPoint(String line) {
      final LineParser parser=new LineParser(line);
      parser.next('<');
      initialX = parser.nextInt(',');
      initialY = parser.nextInt('>');
      parser.next('<');
      velocityX = parser.nextInt(',');
      velocityY = parser.nextInt('>');
   }

   public Point positionAt(int time){
      return new Point(initialX+velocityX*time,initialY+velocityY*time);
   }
}
