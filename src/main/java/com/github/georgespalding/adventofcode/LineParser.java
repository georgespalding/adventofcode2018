package com.github.georgespalding.adventofcode;

import java.awt.*;

class LineParser {

   final String line;
   int pos = 0;

   LineParser(String line) {this.line = line;}

   String next(char delim) {
      int delimPos = line.indexOf(delim, pos);
      if (delimPos == -1) {
         delimPos = line.length();
      }
      try {
         return line.substring(pos, delimPos).trim();
      } finally {
         pos = delimPos + 1;
      }
   }

   int nextInt(char delim) {
      return Integer.parseInt(next(delim));
   }

   Claim parse() {
      return new Claim(
         next('@'),
         new Rectangle(
            nextInt(','),
            nextInt(':'),
            nextInt('x'),
            nextInt('<')));
   }
}
