import java.util.Objects;

/**
 * Created by ubegun on 5/31/2016.
 */
public abstract class BruteForce {

    protected char[] key;
    protected char[] charSet;

    @FunctionalInterface
    public interface CharConsumer {

        void accept(char c);

        default CharConsumer andThen(CharConsumer after) {
            Objects.requireNonNull(after);
            return (char c) -> {
                accept(c);
                after.accept(c);
            };
        }
    }

    public void calc(char[] charSet, char[] key) {
        this.key = key;
        this.charSet = charSet;
        int size = key.length - 1;
        forEach(size, c -> {
            key[key.length - 1] = c;
            testString(new String(key));
        });
    }

    abstract void testString(String testKey);

    private void forEach(int index, CharConsumer seq) {
        if (index == 0) {
            for (int i = 0; i < charSet.length; i++) {
                seq.accept(charSet[i]);
            }
            return;
        }
        int thisIndex = index--;
        forEach(index, c -> {
            key[thisIndex - 1] = c;
            for (int i = 0; i < charSet.length; i++) {
                seq.accept(charSet[i]);
            }
        });
    }

}

