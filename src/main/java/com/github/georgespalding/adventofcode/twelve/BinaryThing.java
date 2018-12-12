package com.github.georgespalding.adventofcode.twelve;

import static java.util.stream.IntStream.range;

import com.github.georgespalding.adventofcode.LineParser;

import java.util.stream.Stream;

public class BinaryThing {

   private static final int WIDTH = 2;
   private static final byte max = convert("#####");
   private final int margin;
   private final boolean[] masks;
   private boolean[] state;

   BinaryThing(String initialState, int margin, Stream<String> masks) {
      this(convertInitialState(initialState, margin), margin, parseMasks(masks));
   }

   private BinaryThing(boolean[] state, int margin, boolean[] masks) {
      this.state = state;
      this.margin = margin;
      this.masks = masks;
   }

   private static boolean[] convertInitialState(String initial, int margin) {
      LineParser lp = new LineParser(initial);
      lp.next(':');
      return toBoolArray(lp.next('<'), margin);
   }

   public static void main(String[] args) {
      System.out.println("#0:" + new BinaryThing("bla: #", 0, Stream.empty()).potNumSum());
      System.out.println("#100:" + new BinaryThing("bla: #", 100, Stream.empty()).potNumSum());
      System.out.println("##0:" + new BinaryThing("bla: ##", 0, Stream.empty()).potNumSum());
      System.out.println("##1000:" + new BinaryThing("bla: ##", 1000, Stream.empty()).potNumSum());
      System.out.println(convert("##.#."));
      System.out.println(convert(".#.##"));
      System.out.println(convert(".#.##"));
      System.out.println(convert("#...."));
      System.out.println(convert("##..."));
      System.out.println(convert("###.."));
      System.out.println(convert("####."));
      System.out.println(convert("#####"));
      System.out.println(convert(toBoolArray(".####", 0), 2 - WIDTH, 2 * WIDTH + 1));
   }

   private static boolean[] parseMasks(Stream<String> maskLines) {
      final boolean[] masks = new boolean[BinaryThing.max + 1];
      maskLines
         .map(LineParser::new)
         .forEach(lp -> {
            String mask = lp.next('=');
            lp.next('>');
            masks[convert(mask)] = toBoolArray(lp.next('<'), 0)[0];
         });
      return masks;
   }

   private static boolean[] toBoolArray(String string, int margin) {
      final char[] chars = string.toCharArray();
      final boolean[] booleans = new boolean[margin + chars.length + margin];
      for (int i = 0; i < chars.length; i++) {
         booleans[margin + i] = chars[i] == '#';
      }
      return booleans;
   }

   private static byte convert(String string) {
      boolean[] flags = toBoolArray(string, 0);
      return convert(flags, 0, flags.length);
   }

   private static byte convert(boolean[] flags, int start, int len) {
      assert len == 5 : "Expacted len 5 but was " + len;
      assert flags.length >= len + start : "Expacted start+len " + start + "+" + len + ">[]length " + flags.length + " but was " + len;

      // final StringBuilder sb = new StringBuilder(len);
      byte result = 0;
      for (int i = start + len - 1; i >= start; i--) {
         // if (DayTwelve.debug) System.out.println("i:" + i + " flags[" + i + "]:" + flags[i] + " result: " + result);
         if (flags[i]) {
            result |= (1 << i - start);
         }
         //sb.insert(0,flags[i] ? '#' : '.');
      }
      // if (DayTwelve.debug) System.out.printf("convert([], %d, %d):%s=%d\n", start, len, sb.toString(), result);
      return result;
   }

   void generation() {
      boolean[] nextState = new boolean[state.length];
      for (int i = WIDTH; i < state.length - WIDTH; i++) {
         // FIXME for (int i = state.length - WIDTH; i >= WIDTH+1; i--) {
         nextState[i] = masks[convert(state, i - WIDTH, 2 * WIDTH + 1)];
      }
      state = nextState;
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder(state.length);
      range(0, state.length)
         .forEach(i -> sb.append(state[i] ? '#' : "."));
      return sb.toString();
   }

   public String header() {
      final StringBuilder sb = new StringBuilder(state.length);
      sb.append("+-: ");
      range(0, state.length)
         .forEach(i -> sb.append(i - 22 >= 0 ? '+' : '-'));
      sb.append("\n10: ");
      range(0, state.length)
         .forEach(i -> sb.append(Math.abs(i - 22) / 10));
      sb.append("\n 1: ");
      range(0, state.length)
         .forEach(i -> sb.append(Math.abs(i - 22) % 10));
      return sb.toString();
   }

   long potNumSum() {
      return range(0, state.length)
         .filter(i -> state[i])
         .map(i -> i - margin)
         .sum();
   }

   long plantContainingPots() {
      return range(0, state.length)
         .filter(i -> state[i])
         .count();
   }

}
