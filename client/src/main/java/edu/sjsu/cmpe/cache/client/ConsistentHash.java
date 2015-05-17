package edu.sjsu.cmpe.cache.client;

import java.util.SortedMap;
import java.util.TreeMap;


public class ConsistentHash {

  private final HashFunction hashFunction;
  private final int numberOfReplicas;
  private final SortedMap<Integer, String> circle =
    new TreeMap<Integer, String>();

  public ConsistentHash(HashFunction hashFunction,
    int numberOfReplicas,String[] nodes) {

    this.hashFunction = hashFunction;
    this.numberOfReplicas = numberOfReplicas;

    for (String node : nodes) {
      add(node);
    }
  }

  public void add(String node) {
    for (int i = 0; i < numberOfReplicas; i++) {
      circle.put(hashFunction.hash(node.toString() + i),
        node);
    }
  }

  public void remove(String node) {
    for (int i = 0; i < numberOfReplicas; i++) {
      circle.remove(hashFunction.hash(node.toString() + i));
    }
  }

  public String get(int key) {
    if (circle.isEmpty()) {
      return null;
    }
    int hash = hashFunction.hash(String.valueOf(key));
    if (!circle.containsKey(hash)) {
      SortedMap<Integer, String> tailMap =
        circle.tailMap(hash);
      hash = tailMap.isEmpty() ?
             circle.firstKey() : tailMap.firstKey();
    }
    return circle.get(hash);
  } 

}
