package com.github.georgespalding.adventofcode;

import java.util.Map.Entry;
import java.util.Objects;

public class Pair<K, V> {

   final K key;
   final V val;

   public Pair(K key, V val) {
      this.key = key;
      this.val = val;
   }

   static <K, V> Pair<K, V> fromEntry(K key, V val) {
      return new Pair<>(key, val);
   }

   static <K, V> Pair<K, V> fromEntry(Entry<K, V> entry) {
      return new Pair<>(entry.getKey(), entry.getValue());
   }

   @Override
   public String toString() {
      return "{" + key + ": " + val + '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      Pair<?, ?> pair = (Pair<?, ?>) o;
      return Objects.equals(key, pair.key) &&
         Objects.equals(val, pair.val);
   }

   @Override
   public int hashCode() {
      return Objects.hash(key, val);
   }
}
