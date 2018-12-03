package com.github.georgespalding.adventofcode;

import static java.nio.file.Files.lines;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class Util {
   static URI getResource(String resourceName) throws URISyntaxException {
      return Objects.requireNonNull(DayOne.class.getClassLoader().getResource(resourceName)).toURI();
   }

   static Stream<String> streamResource(String resourceName){
      try {
         return lines(Paths.get(getResource(resourceName)));
      } catch (Exception e) {
         throw new RuntimeException("Failed to stream data from '"+resourceName+"':", e);
      }
   }
}
