package com.github.georgespalding.adventofcode;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class DayFour {

   static final long load = currentTimeMillis();

   //   private static String[] lines = Util.streamResource("4.lst")
   //      .map(LineParser::new)
   //      .map(LineParser::parseClaim)
   //      .toArray(Claim[]::new)
   //      ;

   public static void main(String[] args) {
      final long start = currentTimeMillis();
      final Map<Integer, List<GuardShift>> shiftsByGuardId = Util.streamResource("4.lst")
         .sorted()
         .map(LineParser::new)
         .map(LineParser::parseGuardLogEntry)
         .collect(groupingBy(GuardEvent::getDate))
         .values().stream()
         .map(GuardShift::new)
         .collect(groupingBy(GuardShift::getGuardId));

      shiftsByGuardId.values().stream().flatMap(Collection::stream)
         .forEach(out::println);
      final List<GuardShift> snooziestGuardShifts = shiftsByGuardId.values().stream()
         .map(guardShifts -> Pair.fromEntry(
            // Avg nap time per shift
            guardShifts.stream().mapToInt(GuardShift::totalNapMinutes).sum()
               / (double) guardShifts.size(),
            // GuardId
            guardShifts))
         .sorted(comparingDouble(integerDoublePair -> -integerDoublePair.getKey()))
         .peek(p -> out.printf("%5d: %+2.2f m/shift\n", p.getVal().get(0).getGuardId(), p.getKey()))
         .findFirst().get().getVal();

      int bestMinute = snooziestGuardShifts.stream()
         .map(GuardShift::getNaps)
         .flatMap(Collection::stream)
         .flatMapToInt(GuardShift.Nap::sleepMinutes)
         .boxed()
         .collect(toMap(m -> m, m->1, (a, b) -> a + b))
         .entrySet().stream()
         .sorted(comparingInt(each->-each.getValue()))
         .peek(e->out.println("Minute: "+e.getKey()+" "+ IntStream.range(0,e.getValue()).mapToObj(i->"Z").collect(joining())))
         .findFirst().get().getKey();

      final int ans1=snooziestGuardShifts.get(0).getGuardId()*bestMinute;
      final long mid = currentTimeMillis();

      final long end = currentTimeMillis();

      out.printf("Load: (%d ms)\n", start - load);
      out.printf("Ans1: %s (%d ms)\n", ans1, mid - start);
      out.printf("Ans2: %s (%d ms)\n", "TODO", end - mid);
      out.printf("Total (%d ms)\n", end - start);

      //displayClaims(Arrays.asList(claims), lowerRight, nonOverlapping);
      //printClaims(matrix);

   }

   private static void printClaims(int[][] matrix) {
      for (int x = 0; x < matrix.length; x++) {
         for (int y = 0; y < matrix[x].length; y++) {
            out.print(matrix[x][y] == 0 ? ' ' : (char) ('0' + matrix[x][y]));
         }
         out.println();
      }
   }

   private static void paint(int[][] matrix, Rectangle rect) {
      for (int x = rect.x; x < rect.x + rect.width; x++) {
         for (int y = rect.y; y < rect.y + rect.height; y++) {
            matrix[x][y]++;
         }
      }
   }

   private static void displayClaims(
      List<Claim> claims,
      Point lr,
      List<Claim> nonOverlapping
   ) {
      JFrame window = new JFrame();
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setBounds(0, 0, lr.x, lr.y);
      window.getContentPane().add(new Fabric(claims, nonOverlapping));
      window.setVisible(true);
   }

   static class Fabric extends JComponent {

      final Color[] colors = {
         Color.black,
         Color.blue,
         Color.cyan,
         Color.gray,
         Color.green,
         Color.lightGray,
         Color.magenta,
         Color.orange,
         Color.pink,
         Color.red,
         Color.yellow
      };
      private final List<Claim> claims;
      private final List<Claim> nonOverlapping;
      AtomicInteger nextColor = new AtomicInteger();
      private int delta;

      Fabric(List<Claim> claims, List<Claim> nonOverlapping) {
         this.claims = claims;
         this.nonOverlapping = nonOverlapping;
      }

      Color getNextColor() {
         return colors[nextColor.getAndIncrement() % colors.length];
      }

      public void paint(Graphics g) {
         g.setPaintMode();
         claims.stream().map(c -> c.rect).forEach(r -> {
            g.setXORMode(getNextColor());
            g.fillRect(r.x, r.y, r.width, r.height);
         });
         nonOverlapping.forEach(claim -> {
            g.setColor(Color.red);
            hilite(g, claim.rect);
            g.drawRect(claim.rect.x, claim.rect.y, claim.rect.width, claim.rect.height);
            g.setColor(Color.black);
            g.drawString(claim.id, claim.rect.x, claim.rect.y);
         });
      }

      private void hilite(Graphics g, Rectangle rect) {
         Point center = new Point((int) rect.getCenterX(), (int) rect.getCenterY());
         delta = (1 + delta) % 50;
         drawOval(g, center, 50, 50);
         drawOval(g, center, 100, 50 - delta);
         drawOval(g, center, delta, 100);
         drawOval(g, center, 100, 100);
      }

      private void drawOval(Graphics g, Point center, int w, int h) {
         g.drawOval(center.x - w / 2, center.y - h / 2, w, h);
      }
   }
}