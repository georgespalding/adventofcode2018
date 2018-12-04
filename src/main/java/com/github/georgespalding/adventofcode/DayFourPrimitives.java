package com.github.georgespalding.adventofcode;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import org.eclipse.collections.api.tuple.primitive.IntObjectPair;
import org.eclipse.collections.impl.tuple.primitive.PrimitiveTuples;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("Duplicates")
public class DayFourPrimitives {

   static final long load = currentTimeMillis();

   public static void main(String[] args) {
      List<IntObjectPair<List<Nap>>> napsByGuardId = Util.streamResource("4.lst")
         .sorted()
         .map(LineParser::new)
         .map(LineParser::parseGuardLogEntry)
         .collect(groupingBy(GuardEvent::getDate))
         .values().stream()
         .map(GuardShift::new)
         .collect(groupingBy(GuardShift::getGuardId))
         .entrySet()
         .stream()
         .map(each -> PrimitiveTuples.pair(
            each.getKey(),
            each.getValue().stream()
               .map(GuardShift::getNaps)
               .flatMap(Collection::stream)
               .collect(toList())))
         .collect(toList());
      final long start = currentTimeMillis();

      final IntObjectPair<List<Nap>> napsOfSnooziestGuard = napsByGuardId.stream()
         //.peek(p -> out.printf("#%5d: %4d m\n", p.getKey(), p.getVal().stream().mapToInt(Nap::duration).sum()))
         .min(comparingInt(p -> -p.getTwo().stream().mapToInt(Nap::duration).sum()))
         .orElse(null);

      final int bestMinute = napsOfSnooziestGuard.getTwo().stream()
         .flatMapToInt(Nap::sleepMinutes)
         .boxed()
         .collect(toMap(m -> m, m -> 1, (a, b) -> a + b))
         .entrySet().stream()
         //.peek(e -> out.printf("#%5d Minute: %2d %s",napsOfSnooziestGuard.key,e.getKey(),"Z".repeat(e.getValue())))
         .min(comparingInt(each -> -each.getValue()))
         .map(Entry::getKey).orElse(-1);

      final int ans1 = napsOfSnooziestGuard.getOne() * bestMinute;
      final long mid = currentTimeMillis();

      //List<Pair<Integer, List<Nap>>>
      final Pair<Integer, Integer> ans2 = napsByGuardId.stream().map(p -> Pair.fromEntry(
         p.getOne(),
         p.getTwo().stream()
            .flatMapToInt(Nap::sleepMinutes)
            .boxed()
            .collect(toMap(m -> m, m -> 1, (a, b) -> a + b))
            .entrySet()
            .stream()
            //.peek(e -> out.printf("#%5d Minute: %2d %s",p.key,e.getKey(),"Z".repeat(e.getValue())))
            .min(comparingInt(each -> -each.getValue()))
            .map(Entry::getKey).orElse(0)))
         .findFirst().get();

      final long end = currentTimeMillis();

      out.printf("Load: (%d ms)\n", start - load);
      out.printf("Ans1: %s (%d ms)\n", ans1, mid - start);
      out.printf("Ans2: %s (%d ms)\n", ans2.getKey() * ans2.getVal(), end - mid);
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