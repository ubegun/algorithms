import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Solution {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int x = -1;
    String line = null, thisLine = null;
    while (in.hasNext()){
      thisLine = in.nextLine();
      if(thisLine.trim().length() < 1){
        continue;
      }
      if(x < 0){
        x = Integer.parseInt(thisLine);
      }else{
        line = thisLine;
        break;
      }
    }
    if (x < 1 && x > 20) {
      System.out.print("NO");
      return;
    }
    String[] seq = line.split("\\s");
    if (seq.length < x - 1) {
      System.out.print("NO");
      return;
    }

    OptionalInt result = IntStream.range(0, seq.length).filter((i) -> {
      if (Integer.parseInt(seq[i]) != i + 1) {
        return true;
      }
      return false;
    }).findFirst();

    if (result.isPresent()) {
      System.out.print("NO");
      return;
    }

    System.out.print("YES");
  }
}