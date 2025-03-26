package fib;
import java.util.*;

public class FibManager {
    private static FibManager instance = new FibManager();
    private List<FibBasedSequence> sequences = new ArrayList<>();
    private static int NUM_ELEMENTS = 10;

    private FibManager() {}

    public static FibManager getInstance() {
        return instance;
    }

    public FibBasedSequence add(int first, int second) {
        return addSequence(new FibBasedSequence(first, second));
    }

    public FibBasedSequence add(String name, int first, int second) {
        return addSequence(new FibBasedSequence(name, first, second));
    }

    private FibBasedSequence addSequence(FibBasedSequence toAdd) {
        sequences.add(toAdd);
        return toAdd;
    }

    public FibBasedSequence getByName(String name) {
        String[] split = name.split("|");
        return new FibBasedSequence(split[0], Integer.valueOf(split[1]), Integer.valueOf(split[2]));
    }

    public static void setNumElements(int update) {
        NUM_ELEMENTS = update;
    }

    public static int getNumElements() {
        return NUM_ELEMENTS;
    }

    public static String generateComparison(FibBasedSequence seq1, FibBasedSequence seq2) {
        StringBuilder build = new StringBuilder();
        int[] mem1 = seq1.getMem();
        int[] mem2 = seq2.getMem();

        for (int i = 0; i < FibManager.getNumElements(); i++) {
            build.append(Math.abs(mem2[i] - mem1[i])).append(" ");
        }

        return build.toString();
    }
} 
