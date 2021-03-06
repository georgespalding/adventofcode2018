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

   public static <K, V> Pair<K, V> fromEntry(K key, V val) {
      return new Pair<>(key, val);
   }

   public static <K, V> Pair<K, V> fromEntry(Entry<K, V> entry) {
      return new Pair<>(entry.getKey(), entry.getValue());
   }

   public K getKey() {
      return key;
   }

   public V getVal() {
      return val;
   }

   @Override
   public int hashCode() {
      return Objects.hash(key, val);
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
   public String toString() {
      return "{" + key + ": " + val + '}';
   }
}
