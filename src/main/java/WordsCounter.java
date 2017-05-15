import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Given a text file on local file system, write a java program which counts unique words in the file. The file may contain
 * multiple lines. The words in a line may be split by whitespace, comma or dot (“.”) There must be unit tests written at
 * least for the method doing all the math.
 *
 * Program input params:
 * 1.path to the file
 * 2. integer number N which indicates the amount of words to output
 *
 * Expected output: “word – count”  pairs ordered by count in descending order. Exactly N pairs printed out if the amount of
 * unique words is greater or equal to N; all pairs printed out otherwise. if there are more than one word with the same
 * frequency, the words must be ordered alphabetically within corresponding group.
 *
 * E.g.: When I run
 * $ java wordCount lyrics.txt 5
 * The expected output should be:
 * merry=16
 * christmas=8
 * their=8
 * to=8
 * good=6
 *
 * Created by Aliaksei Yarotski.
 */
public final class WordsCounter {

  private final Path fileAbsolutePath;
  private final HashMap<String, WordCounter> cache;
  private WordCounter firstElement;
  private WordCounter lastElement;

  public WordsCounter(Path fileAbsolutePath) {
    this.fileAbsolutePath = fileAbsolutePath;
    this.cache = new HashMap();
  }

  private boolean balancing(Node higherNode, WordCounter balancingWord) {
    if (higherNode == null) {
      firstElement = balancingWord;
      return false;
    }
    int c = higherNode.compareTo(balancingWord);
    if (c > 0) {
      return false;
    }
    if (c < 0) {
      balancingWord.setHigherNode(higherNode.getHigherNode());
      if (balancingWord.getHigherNode() != null) {
        balancingWord.getHigherNode().setLowerNode(balancingWord);  //set ref. to this from top
      }
      higherNode.setLowerNode(balancingWord.getLowerNode());
      if (balancingWord.getLowerNode() != null) { // in case the last node
        balancingWord.getLowerNode().setHigherNode(higherNode);
      }
      balancingWord.setLowerNode(higherNode);
      higherNode.setHigherNode(balancingWord);
      //bend out
      if (balancingWord.getBend() != null) {
        balancingWord.getBend().setHigherNode(balancingWord);
        balancingWord.setBend(null);
      }
      return balancing(balancingWord.getHigherNode(), balancingWord);
    }
    // if sum are equals
    if (higherNode instanceof Bend) {
      balancingWord.setBend((Bend) higherNode);
    } else {
      WordCounter higherWord = (WordCounter) higherNode;
      Bend thisBend = higherWord.getBend();
      if (thisBend == null) {
        thisBend = new Bend(higherWord);
        higherWord.setBend(thisBend);
      }
      balancingWord.setBend(thisBend);
    }
    //income to bend
    balancingWord.setLowerNode(balancingWord.getLowerNode());
    return true;
  }

  public static abstract class Node implements Comparable<Node> {

    private Node higherNode;
    private Node lowerNode;
    private AtomicInteger counter;

    public Node(Node higherNode, AtomicInteger counter) {
      this.higherNode = higherNode;
      this.counter = counter;
      if (higherNode != null) { // set back ref.
        higherNode.setLowerNode(this);
      }
    }

    public Node(Node higherNode, Node lowerNode, AtomicInteger counter) {
      this.higherNode = higherNode;
      this.lowerNode = lowerNode;
      this.counter = counter;
    }

    public void incCounter() {
      this.counter.incrementAndGet();
    }

    public int getCount() {
      return counter.intValue();
    }

    public Node getHigherNode() {
      return higherNode;
    }

    public void setHigherNode(Node higherNode) {
      this.higherNode = higherNode;
    }

    public Node getLowerNode() {
      return lowerNode;
    }

    public void setLowerNode(Node lowerNode) {
      this.lowerNode = lowerNode;
    }

    @Override
    public int compareTo(Node other) {
      return Integer.compare(this.getCount(), other.getCount());
    }

  }

  public static class Bend extends Node {

    public Bend(WordCounter topCounter) {
      super(topCounter.getHigherNode(), topCounter.getLowerNode(), new AtomicInteger(topCounter.getCount()));
    }
  }

  public static final class WordCounter extends Node {

    private String word;
    protected Bend bend;

    public WordCounter(String word, WordCounter higherCounter) {
      super(higherCounter, new AtomicInteger(1));
      this.word = word;
    }

    public Bend getBend() {
      return bend;
    }

    public void setBend(Bend bend) {
      this.bend = bend;
    }

    public String getWord() {
      return word;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof WordCounter)) return false;

      WordCounter that = (WordCounter) o;

      return word.equals(that.word);

    }

    @Override
    public int hashCode() {
      return word.hashCode();
    }

    @Override
    public String toString() {
      return word + "=" + super.getCount();
    }
  }

  /**
   * Parallel calculation, using java 8 streaming feature
   */
  public WordCounter parse() {
    try (Stream<String> stream = Files.lines(fileAbsolutePath)) {
      stream.map(line -> line.split("[\\n\\s\\.\\,]"))
          .flatMap(Arrays::stream)
          .forEach(p -> {
                String word = p.trim().toLowerCase();
                if (word.length() == 0) {
                  return;
                }
                WordCounter counter = cache.get(word);
                if (counter == null) {
                  WordCounter thisWord = null;
                  if (firstElement == null) { //parsing first word
                    thisWord = new WordCounter(word, null);
                    firstElement = thisWord;
                  } else {
                    thisWord = new WordCounter(word, lastElement);
                    balancing(lastElement, thisWord);
                  }
                  lastElement = thisWord;
                  cache.put(word, thisWord);
                } else {
                  counter.incCounter();
                  if (lastElement.equals(counter)) {
                    lastElement = (WordCounter) counter.getHigherNode();
                  }
                  balancing(counter.getHigherNode(), counter);
                }
              }
          );
    } catch (IOException e) {
      e.printStackTrace();
    }

    return firstElement;
  }

  public static void main(final String... args) {
    if (args.length > 0 && args[0] != null) {
      Path fileAbsolutePath = Paths.get(args[0]);

      int stackLength = Integer.parseInt(args[1]);

      WordsCounter tp = new WordsCounter(fileAbsolutePath);
      long duration = 0;
      long startTime = System.currentTimeMillis();
      WordCounter thisElement = tp.parse();
      duration = System.currentTimeMillis() - startTime;
      System.out.println("Calculation time: " + duration + " ms.\n");
      for (int i = 0; i < stackLength; i++) {
        if (thisElement == null) {
          System.out.println("------------------");
          return;
        }
        System.out.println(thisElement.toString());
        thisElement = (WordCounter) thisElement.getLowerNode();
      }
    } else {
      System.err.print("No arguments!");
    }
  }


}
