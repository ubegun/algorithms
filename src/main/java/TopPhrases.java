import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Parsing file and approximating bottom border for the top of most frequent phrases in the same iteration.
 *
 * T=O(n)
 *
 * Created by <br> on 2/9/17.
 */
public final class TopPhrases {

  public static int stackLength;
  private final Path fileAbsolutePath;
  // S = O(2*n)
  private final ArrayList<Phrase> topPhrases;
  private Counter counter;

  //TODO: remove
  int topCounter = 0;

  public TopPhrases(int stackLength, Path fileAbsolutePath) {
    this.stackLength = stackLength;
    this.fileAbsolutePath = fileAbsolutePath;
    topPhrases = new ArrayList<Phrase>();
    counter = new Counter();
  }


  public List<Phrase> parse() {
    try (Stream<String> stream = Files.lines(fileAbsolutePath)) {
      stream.map(line -> line.split("\\|"))
          .flatMap(Arrays::stream)
          .forEach(p -> {
                Phrase phrase = counter.getFromCache(p);
                if (phrase == null) {
                  counter.putToCache(p, new Phrase(p));
                } else {
                  phrase.incrementCount();
                  // calculates a minimum frequency from a top of the list.
                  counter.normalize(phrase);
                }
              }
          );
    } catch (IOException e) {
      e.printStackTrace();
    }

    /**
     * T = O(n)
     * S = O(2*n)
     */
    System.out.println("Min rate: " + counter.getMinTopRate() + " total P:" + counter.getCache().size());
    counter.getCache().forEach((k, v) -> {
          if (v.getCount() >= counter.getMinTopRate()) {
            topCounter++;
            topPhrases.add(v);
          }
        }
    );
    System.out.println("Approximated result " + topCounter);

    topCounter = 0;
    Collections.sort(topPhrases);
    for (Phrase p : topPhrases) {
      if(topCounter++ == stackLength){
        break;
      }
      System.out.println("#" + topCounter + " " + p.toString());
    }
    System.out.println(counter.toString());

    return topPhrases;
  }

  public static final class Counter {
    private int topCount = 0;
    private int minTopRate = 1;
    private int nextMin = Integer.MAX_VALUE;

    private int hiddenCross = 0;
    private int border = TopPhrases.stackLength;
    private int seriaId = 0;

    // S = O(3*n)
    private final HashMap<String, Phrase> cache = new HashMap<>();


    public synchronized void normalize(Phrase phrase) {
      int nextCount = phrase.getCount();
      if (minTopRate < nextCount) {
        if (phrase.getSeresId() != seriaId) {
          topCount = topCount + 1;
          phrase.setSeresId(seriaId);
        }
        //min in group
        if (nextMin > nextCount) {
          nextMin = nextCount;
        }
        //num of min
        if (minTopRate + 1 == nextCount) {
          hiddenCross++;
        }
        if (topCount >= border) {
          seriaId++;
          if (hiddenCross > 0) {
            minTopRate++;
          } else {
            minTopRate = nextMin;
          }
          nextMin = Integer.MAX_VALUE;
          topCount = 0;
          hiddenCross = 0;
        }
      }
    }



    public Phrase getFromCache(String phrase) {
      return cache.get(phrase);
    }

    public Map<String, Phrase> getCache() {
      return cache;
    }

    public synchronized void putToCache(String key, Phrase phrase) {
      cache.put(key, phrase);
    }

    public int getMinTopRate() {
      return minTopRate;
    }

    @Override
    public String toString() {
      return "Counter{" +
          "topCount=" + topCount +
          ", minTopRate=" + minTopRate +
          ", nextMin=" + nextMin +
          ", hiddenCross=" + hiddenCross +
          ", border=" + border +
          ", seresId=" + seriaId +
          '}';
    }
  }

  public static class Phrase implements Comparable<Phrase> {
    private int seresId = -1;
    private final AtomicInteger count;
    private final String phrase;

    public Phrase(String phrase) {
      this.phrase = phrase;
      this.count = new AtomicInteger(1);
    }

    public String getPhrase() {
      return phrase;
    }

    public int getSeresId() {
      return seresId;
    }

    public void setSeresId(int seresId) {
      this.seresId = seresId;
    }

    public int incrementCount() {
      return count.incrementAndGet();
    }

    public int getCount() {
      return count.get();
    }

    @Override
    public int compareTo(Phrase o) {
      return Integer.compare(o.count.get(), this.count.get());
    }

    public static int compare(Phrase x, Phrase y) {
      return x.count.get() - y.count.get();
    }

    @Override
    public String toString() {
      return "Phrase{" +
          "seresId=" + seresId +
          ", count=" + count.get() +
          ", phrase='" + phrase + '\'' +
          '}';
    }
  }

  public static void main(final String... args) {
    if (args.length > 0 && args[0] != null) {
      int stackLength = Integer.parseInt(args[0]);
      Path fileAbsolutePath = Paths.get(args[1]);

      TopPhrases tp = new TopPhrases(stackLength, fileAbsolutePath);
      long duration = 0;
      boolean isPalindrome = false;
      long startTime = System.currentTimeMillis();
      tp.parse();
      duration = System.currentTimeMillis() - startTime;
      System.out.println("Calculation time: " + duration + " ms.\n");
    } else {
      System.err.print("No arguments!");
    }
  }


}
