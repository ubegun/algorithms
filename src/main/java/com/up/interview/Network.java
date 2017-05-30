package com.up.interview;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Alex Yarotski on 2/22/17.
 */
public final class Network {

  public static final String CAPACITY_SHOULD_BE_POSITIVE_INTEGER = "Capacity of the network should be positive Integer and greater than 0.";
  public static final String ELEMENTS_SHOULD_BE_POSITIVE_INTEGER = "The elements of the network should be positive Integer.";
  public static final String INDEX_OUT_OF_NETWORK_CAPACITY = "Index of the point is out of the network capacity.";

  private final int capacity;
  private final Vector<Integer>[] network;

  /**
   * Creates the network with fixed capacity.
   *
   * @param capacity Network capacity;
   */
  public Network(int capacity) throws NetworkException {
    if (capacity < 1) {
      throw new NetworkException(CAPACITY_SHOULD_BE_POSITIVE_INTEGER);
    }
    this.capacity = capacity;
    this.network = new Vector[capacity];
  }

  /**
   * Connects A & B points to each other.
   *
   * @throws NetworkException in case inappropriate conditions.
   */
  public void connect(int pointA, int pointB) throws NetworkException {
    // nothing to do if points already connected.
    if (query(pointA, pointB)) {
      return;
    }

    // handled case, when one of the points or both didn't have any connections.
    if (network[pointA] == null && network[pointB] == null) {
      network[pointA] = new Vector<Integer>(2);
      network[pointA].add(0, pointA);
      network[pointA].add(0, pointB);
      network[pointB] = network[pointA];
      return;
    } else if (network[pointA] == null) {
      network[pointB].add(pointA);
      network[pointA] = network[pointB];
      return;
    } else if (network[pointB] == null) {
      network[pointA].add(pointB);
      network[pointB] = network[pointA];
      return;
    }

    union(network[pointA], network[pointB]);
  }

  /**
   *  United two in one the set which forms the points.
   */
  private void union(final Vector leftSet, final Vector rightSet){
    Vector smallSet, bigSet;
    if (leftSet.size() > rightSet.size()) {
      bigSet = leftSet;
      smallSet = rightSet;
    } else {
      bigSet = rightSet;
      smallSet = leftSet;
    }

    final Vector<Integer> biggerSet = bigSet;
    /**
     * Union of the clique. Note: worst case scenario when both cliques are large already.
     */
    smallSet.forEach(p -> {
      network[(int) p] = biggerSet;
    });
    biggerSet.addAll(smallSet);
  }


  /**
   * Test on the connection between the points. It returns true if the elements are connected,
   * directly or indirectly, and false if the elements are not connected.
   *
   * @throws NetworkException in case inappropriate conditions.
   */
  public boolean query(int pointA, int pointB) throws NetworkException {
    if (pointA < 0 || pointB < 0) {
      throw new NetworkException(ELEMENTS_SHOULD_BE_POSITIVE_INTEGER);
    }
    if (pointA >= capacity || pointB >= capacity) {
      throw new NetworkException(INDEX_OUT_OF_NETWORK_CAPACITY);
    }
    if (pointA == pointB) {
      //point A & B are equals, It's ok but no sense.
      return true;
    }
    if (network[pointA] == null || network[pointB] == null) {
      return false;
    }

    return network[pointA] == network[pointB];
  }


  /**
   * Returns true in only then if all elements of the network is in one clique. In other cases false.
   */
  protected boolean hasSingleClique(){
    final Vector lastClique = network[network.length-1];
    if(lastClique == null)
      return false;
    Optional result = Arrays
        .stream(network)
        .filter(v -> v!= null &&!lastClique.equals(v))
        .findFirst();
    return !result.isPresent();
  }

  /**
   * Network exceptions.
   */
  public static final class NetworkException extends Exception {

    private NetworkException(String message) {
      super(message);
    }

    private NetworkException(String message, Throwable cause) {
      super(message, cause);
    }
  }

}
