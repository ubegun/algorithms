package com.up.interview;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Aliaksei Yarotski on 2/22/17.
 */
public final class Network {

  private static final Random rnd = new Random();

  private final int capacity;
  private final Vector<Integer>[] network;

  /**
   * Creates the network with fixed capacity.
   *
   * @param capacity Network capacity;
   */
  public Network(int capacity) throws NetworkException {
    if (capacity < 1) {
      throw new NetworkException("Capacity of the network should be positive Integer and greater than 0.");
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
    if (network[pointA] == null && network[pointA] == null) {
      network[pointA] = new Vector<Integer>(2);
      network[pointA].add(0, pointA);
      network[pointA].add(0, pointB);
      network[pointB] = network[pointA];
    } else if (network[pointA] == null) {
      network[pointB].add(pointA);
      network[pointA] = network[pointB];
    } else if (network[pointB] == null) {
      network[pointA].add(pointB);
      network[pointB] = network[pointA];
    }

    Vector bigSet, smallSet;
    // compares two cliques which form the points.
    if (network[pointA].size() > network[pointB].size()) {
      bigSet = network[pointA];
      smallSet = network[pointB];
    } else {
      bigSet = network[pointB];
      smallSet = network[pointA];
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
      throw new NetworkException("The elements of the network should be positive Integer. " +
          (pointA < pointB ? "pointA = " + pointA : " pointB = " + pointB));
    }
    if (pointA >= capacity || pointB >= capacity) {
      throw new NetworkException("Index of the point is out of the network capacity." +
          (pointA > pointB ? "pointA = " + pointA : " pointB = " + pointB));
    }
    if (pointA == pointB) {
      System.out.println("point A & B are equals, It's ok but no sense.");
      return true;
    }
    if (network[pointA] == null || network[pointB] == null) {
      return false;
    }

    return network[pointA] == network[pointB];
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

  /**
   * For tests the network.
   *
   * example:
   * java Network 1000 100
   *
   * where 1000 is the network capacity and 100 is numbers of tries to connect pairs of points.
   */
  public static void main(final String... args) throws NetworkException {
    if (args.length == 2) {
      final int length = Integer.parseInt(args[0]);
      final int pairs = Integer.parseInt(args[0]);
      long duration = 0;
      boolean isPalindrome = false;
      long startTime = System.nanoTime();

      Network nw = new Network(length);

      for(int i =0; i < pairs; i++){

      }

      duration = System.nanoTime() - startTime;
      System.out.println("Is palindrome: " + isPalindrome);
      System.out.println("Calculation time: " + duration + " ns.\n");
    } else {
      System.err.print("No arguments!");
    }
  }

}
