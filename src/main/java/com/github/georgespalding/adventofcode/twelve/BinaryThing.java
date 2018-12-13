package com.github.georgespalding.adventofcode.twelve;

import static java.math.BigInteger.valueOf;
import static java.util.stream.IntStream.range;

import com.github.georgespalding.adventofcode.LineParser;

import java.math.BigInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BinaryThing {

   private static final int WIDTH = 2;
   private static final byte max = convert("#####");
   private static final int PADDING = 2 * WIDTH + 1;
   private final boolean[] masks;
   // Rename to offset
   private int indexOfPotZero;
   private boolean[] state;

   BinaryThing(String initialState, Stream<String> masks) {
      this(convertInitialState(initialState, PADDING), PADDING, parseMasks(masks));
   }

   private BinaryThing(boolean[] state, int indexOfPotZero, boolean[] masks) {
      this.state = state;
      this.indexOfPotZero = indexOfPotZero;
      this.masks = masks;
   }

   private static boolean[] convertInitialState(String initial, int padding) {
      LineParser lp = new LineParser(initial);
      lp.next(':');
      return toBoolArray(lp.next('<'), padding);
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

   private static boolean[] toBoolArray(String string, int padding) {
      final char[] chars = string.toCharArray();
      final boolean[] booleans = new boolean[padding + chars.length + padding];
      for (int i = 0; i < chars.length; i++) {
         booleans[padding + i] = chars[i] == '#';
      }
      return booleans;
   }

   private static byte convert(String string) {
      boolean[] flags = toBoolArray(string, 0);
      return convert(flags, 0, flags.length);
   }

   private static byte convert(boolean[] flags, int start, int len) {
      assert len == 5 : "Expected len 5 but was " + len;
      assert flags.length >= len + start : "Expected start+len " + start + "+" + len + ">[]length " + flags.length + " but was " + len;

      //       final StringBuilder sb = new StringBuilder(len);
      byte result = 0;
      //      if (DayTwelve.debug) System.out.println("start: " + start + " len: " + len + " end:" + start+len+" i:["+start+","+(start + len - 1)+"]");
      for (int i = start + len - 1; i >= start; i--) {
         //          if (DayTwelve.debug) System.out.println("i:" + i + " flags[" + i + "]:" + flags[i] + " result: " + result);
         if (flags[i]) {
            result |= (1 << i - start);
         }
         //         sb.insert(0,flags[i] ? '#' : '.');
      }
      //       if (DayTwelve.debug) System.out.printf("convert([], %d, %d):%s=%d\n", start, len, sb.toString(), result);
      return result;
   }

   void generation() {
      final int indexOfFirstPlant = IntStream.range(0, state.length)
         .filter(i -> state[i])
         .findFirst()
         .getAsInt();
      final int indexOfLastPlant = IntStream.rangeClosed(1, state.length).map(i -> state.length - i)
         .filter(i -> state[i])
         .findFirst()
         .getAsInt();
      final int delta = PADDING - indexOfFirstPlant;
      boolean[] nextState = new boolean[PADDING + indexOfLastPlant + 1 - indexOfFirstPlant + PADDING];
      for (int i = indexOfFirstPlant - WIDTH; i < indexOfLastPlant + WIDTH+1; i++) {
         if (masks[convert(state, i - WIDTH, PADDING)]) {
            nextState[i + delta] = true;
         }
      }
      state = nextState;
      indexOfPotZero += delta;
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder(state.length);
      range(0, indexOfPotZero).forEach(i -> sb.append("x"));

      range(0, state.length)
         .forEach(i -> sb.append(state[i] ? '#' : "."));
      sb.append(" offset: ").append(indexOfPotZero).append(", sum:" + potNumSum());
      return sb.toString();
   }

   public String header() {
      final StringBuilder l1 = new StringBuilder(state.length).append("+/-:");
      final StringBuilder l2 = new StringBuilder(state.length).append("10: ");
      final StringBuilder l3 = new StringBuilder(state.length).append(" 1: ");
      range(0, indexOfPotZero).forEach(i -> {
         l1.append("x");
         l2.append("x");
         l3.append("x");
      });

      range(0, state.length)
         .forEach(i -> {
            l1.append(i - indexOfPotZero >= 0 ? '+' : '-');
            l2.append(Math.abs(i - indexOfPotZero) / 10);
            l3.append(Math.abs(i - indexOfPotZero) % 10);
         });
      return l1.append('\n')
         .append(l2).append('\n')
         .append(l3).toString();
   }

   long potNumSum() {
      return range(0, state.length)
         .filter(i -> state[i])
         .map(i -> i - indexOfPotZero)
         //         .peek(i-> System.out.println((i-offset) +" offset:"+offset))
         .sum();
   }

   BigInteger hyperJumpAt100() {
      final BigInteger fiveBill = valueOf(50_000_000_000L);
      BigInteger remainingSteps = fiveBill.subtract(valueOf(100));
      final long plantCount = range(0, state.length)
         .filter(i -> state[i])
         .count();

      long valAt100 = potNumSum();
      return valueOf(valAt100)
         .add(valueOf(plantCount)
            .multiply(remainingSteps));
   }

}
