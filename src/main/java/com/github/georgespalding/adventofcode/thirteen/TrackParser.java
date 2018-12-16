package com.github.georgespalding.adventofcode.thirteen;

import static java.util.Optional.ofNullable;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class TrackParser {

   public static Track from(char trackSymbol, int x, int y) {
      switch (trackSymbol) {
         case '|':
            return new StraightTrack(new Point(x, y), null, false);
         case '-':
            return new StraightTrack(new Point(x, y), null, true);
         case '+':
            return new Crossing(new Point(x, y), null);
         case '/':
            return new CurvedTrack(new Point(x, y), null);
         case '\\':
            return new CurvedTrack(new Point(x, y), null);
         case 'V':
         case 'v':
            return new StraightTrack(new Point(x, y), new MineCart(Direction.South), false);
         case '^':
            return new StraightTrack(new Point(x, y), new MineCart(Direction.North), false);
         case '>':
            return new StraightTrack(new Point(x, y), new MineCart(Direction.East), true);
         case '<':
            return new StraightTrack(new Point(x, y), new MineCart(Direction.West), true);
         default:
            return null;
      }

   }

   public static List<MineCart> parse(Stream<String> trackLines) {
      final List<MineCart> carts = new ArrayList<>();
      final LinkedList<Track[]> lineTrackList = new LinkedList<>();
      trackLines
         .map(String::toCharArray)
         .forEach(chars -> {
            Track[] tracks = new Track[chars.length];
            range(0, chars.length)
               .forEach(x ->
                  ofNullable(from(chars[x], x, lineTrackList.size()))
                     .ifPresent(track -> {
                        tracks[x] = track;
                        if (track.hasCart()) {
                           carts.add(track.getCart());
                        }
                        if (!lineTrackList.isEmpty()) {
                           ofNullable(lineTrackList.getLast())
                              .filter(lta -> lta.length > x)
                              .map(lta -> lta[x])
                              .ifPresent(track::north);
                        }
                        if (x > 0) {
                           ofNullable(tracks[x - 1])
                              .ifPresent(track::west);
                        }
                     }));
            lineTrackList.addLast(tracks);
         });
      return carts;
   }
}
