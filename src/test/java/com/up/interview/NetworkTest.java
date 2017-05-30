package com.up.interview;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.up.interview.Network.CAPACITY_SHOULD_BE_POSITIVE_INTEGER;
import static com.up.interview.Network.ELEMENTS_SHOULD_BE_POSITIVE_INTEGER;
import static com.up.interview.Network.INDEX_OUT_OF_NETWORK_CAPACITY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by  Alex Yarotski on 2/22/17.
 */
public class NetworkTest {

  private Network network;
  private static final int CAPACITY = 100;
  private static final Random rnd = new Random();

  @Before
  public void init() throws Network.NetworkException {
    network = new Network(CAPACITY);
  }

  @Test
  public void exceptionCase_onNetworkCreation() throws Network.NetworkException {
    assertThatThrownBy(() -> new Network(-1))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(CAPACITY_SHOULD_BE_POSITIVE_INTEGER);
  }

  @Test
  public void exceptionCase_onSetOperations() throws Network.NetworkException {
    //negative cases
    assertThatThrownBy(() -> new Network(1).connect(-1, -1))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(ELEMENTS_SHOULD_BE_POSITIVE_INTEGER);
    assertThatThrownBy(() -> new Network(1).connect(-1, 0))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(ELEMENTS_SHOULD_BE_POSITIVE_INTEGER);
    assertThatThrownBy(() -> new Network(1).connect(0, -1))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(ELEMENTS_SHOULD_BE_POSITIVE_INTEGER);
    assertThatThrownBy(() -> new Network(1).query(-1, -1))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(ELEMENTS_SHOULD_BE_POSITIVE_INTEGER);
    assertThatThrownBy(() -> new Network(1).query(0, -1))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(ELEMENTS_SHOULD_BE_POSITIVE_INTEGER);
    assertThatThrownBy(() -> new Network(1).query(-1, 0))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(ELEMENTS_SHOULD_BE_POSITIVE_INTEGER);
    //out of bound
    assertThatThrownBy(() -> new Network(1).connect(1, 1))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(INDEX_OUT_OF_NETWORK_CAPACITY);
    assertThatThrownBy(() -> new Network(1).connect(0, 1))
        .isInstanceOf(Network.NetworkException.class);
    assertThatThrownBy(() -> new Network(1).connect(1, 0))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(INDEX_OUT_OF_NETWORK_CAPACITY);
    assertThatThrownBy(() -> new Network(1).query(1, 1))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(INDEX_OUT_OF_NETWORK_CAPACITY);
    assertThatThrownBy(() -> new Network(1).query(1, 0))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(INDEX_OUT_OF_NETWORK_CAPACITY);
    assertThatThrownBy(() -> new Network(1).query(0, 1))
        .isInstanceOf(Network.NetworkException.class)
        .hasMessage(INDEX_OUT_OF_NETWORK_CAPACITY);
  }

  /**
   * This case from the Task.
   *
   * @throws Network.NetworkException
   */
  @Test
  public void runInfra_devscreener_Case() throws Network.NetworkException {
    // #1 Clique
    network.connect(1, 6);
    assertThat(network.query(1, 6)).isTrue();
    network.connect(2, 1);
    assertThat(network.query(2, 1)).isTrue();
    network.connect(2, 6);
    assertThat(network.query(2, 6)).isTrue();
    network.connect(4, 2);
    assertThat(network.query(4, 2)).isTrue();

    // #2 Clique
    network.connect(5, 8);
    assertThat(network.query(5, 8)).isTrue();
    assertThat(network.query(5, 1)).isFalse();
    assertThat(network.query(8, 6)).isFalse();

    // non set
    assertThat(network.query(5, 3)).isFalse();
    assertThat(network.query(8, 7)).isFalse();
    assertThat(network.query(1, 3)).isFalse();
    assertThat(network.query(6, 7)).isFalse();
  }


  @Test
  public void runConnect_UntilOneClique() throws Network.NetworkException {
    int a, b, count = 0;
    while(!network.hasSingleClique()) {
        a = rnd.nextInt(CAPACITY);
        b = rnd.nextInt(CAPACITY);
        network.connect(a, b);
        count++;
    }
    System.out.println("Pair count: " + count);
  }

}