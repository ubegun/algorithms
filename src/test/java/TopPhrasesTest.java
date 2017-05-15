import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by <br> on 2/9/17.
 */
public class TopPhrasesTest {

  private static final Path readFile = Paths.get("/", "home", "work", "projects", "sample-text", "King_Stephen_The_Dark_Tower.txt");
  private static final Path writeFile = Paths.get("/", "home", "work", "projects", "sample-text", "KSTDT.txt");
  private static final Random rnd = new Random();

  private int count = 0;
  int limit = rnd.nextInt(2) + 1; // Small group
  int phraseCounting = 0;

  @Test
  public void prepareFile() {


    for(int rr = 0 ; rr < 10000; rr++) {
      try (Stream<String> stream = Files.lines(readFile, StandardCharsets.ISO_8859_1)) {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(writeFile, StandardOpenOption.APPEND, StandardOpenOption.CREATE))) {
          final StringBuffer newLine = new StringBuffer();
          stream.forEach(s -> {
            for (String word : s.split("\\s")) {
              String thisWord = word.trim().replaceAll("[^\\w]", "");
              if (thisWord.length() > 0) {
                newLine.append(thisWord).append(" ");
                limit--;
              }
              if (limit == 0) {
                newLine.append("|");
                limit = rnd.nextInt(2) + 1;
                phraseCounting++;
              }
              if (phraseCounting == 50) {
                try {
                  pw.println(newLine.toString());
                } catch (Exception e) {
                  e.printStackTrace();
                }
                newLine.setLength(0);
                phraseCounting = 0;
              }
            }
          });
        } catch (IOException e) {
          e.printStackTrace();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }



}
