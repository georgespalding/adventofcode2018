package com.github.georgespalding.adventofcode.ten;

import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

import com.github.georgespalding.adventofcode.Util;
import com.github.georgespalding.adventofcode.template.Day;

import java.util.List;

public class DayTen {

   static final boolean debug = false;

   final static Day<Object, Object> dayNine = new Day<>();
   final static List<MovingPoint> movingPoints = Util.streamResource("10.lst")
      .map(MovingPoint::new)
      .collect(toList());
   public static void main(String[] args) {
      dayNine.start();

      int loT = 0;
      Point loBox = box(loT);
      int hiT = Integer.MAX_VALUE;
      Point hiBox = box(hiT);

      assert hiBox.compareTo(loBox) > 0 : "WTF:"+hiBox+".compareTo("+loBox+") == "+hiBox.compareTo(loBox)+" >0";

      while (hiBox.compareTo(loBox) != 0) {
         final boolean goLeft = hiBox.compareTo(loBox) > 0;
         final int nextT = (hiT + loT) / 2;
         final Point nextBox = box(nextT);
         if (goLeft) {
            hiBox = nextBox;
            hiT = nextT;
         } else {
            loBox = nextBox;
            loT = nextT;
         }
         if(debug) out.println("lo("+loT+"):"+loBox+", hi("+hiT+"):"+hiBox);
      }

      final int capacity = (int) (1 + (loBox.getY() + 3) * (loBox.getX() + 1));
      StringBuilder sb=new StringBuilder(capacity);
      sb.append('\n');
      final int t=loT;
      final List<Point> pointsT = movingPoints.stream()
         .map(mp -> mp.positionAt(t))
         .sorted()
         .collect(toList());
      final Point min = pointsT.get(0);
      final Point max = pointsT.get(pointsT.size() - 1);
      for (long y = min.getY()-1; y <= max.getY(); y++) {
         for (long x = min.getX(); x <= max.getX(); x++) {
            final Point coord=new Point(x,y);
            sb.append(pointsT.stream().anyMatch(coord::equals)?'#':' ');
         }
         sb.append('\n');
      }
      dayNine.partOne(sb.toString());
      dayNine.partTwo("TODO Part2");

      dayNine.output();
   }

   static Point box(int t) {
      final List<Point> pointsT = movingPoints.stream()
         .map(mp -> mp.positionAt(t))
         .sorted()
         .collect(toList());
      final Point min = pointsT.get(0);
      final Point max = pointsT.get(pointsT.size() - 1);
      return Point.box(min,max);
   }
}
