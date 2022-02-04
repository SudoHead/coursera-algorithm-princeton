package part_II.week5;

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private final CircularSuffix[] suffixes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("CircularSuffixArray constructor argument cannot be null.");
        int n = s.length();
        suffixes = new CircularSuffix[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new CircularSuffix(s, i);
        }
//        CircularSuffix[] og = Arrays.copyOf(suffixes, n);
        Arrays.sort(suffixes);
//        StdOut.println("Input string: \"" + s + "\"");
//        StdOut.printf("%3s\t%" + n + "s\t%" + n + "s\tindex[i]\n", "i", "OG", "Sort");
//        for (int i = 0; i < n; i++) StdOut.printf("%3d\t%s\t%s\t%3d\n", i, og[i], suffixes[i], suffixes[i].index);
    }

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private final String text;
        private final int index;

        private CircularSuffix(String text, int index) {
            this.text = text;
            this.index = index;
        }

        private int length() {
            return text.length();
        }

        private char charAt(int i) {
            return text.charAt((index + i) % this.length()); // circular
        }

        public int compareTo(CircularSuffix that) {
            if (this == that) return 0;  // optimization
            int n = Math.min(this.length(), that.length());
            for (int i = 0; i < n; i++) {
                if (this.charAt(i) < that.charAt(i)) return -1;
                if (this.charAt(i) > that.charAt(i)) return +1;
            }
            return this.length() - that.length();
        }

        public String toString() {
            return text.substring(index) + text.substring(0, index);
        }
    }

    // length of s
    public int length() {
        return suffixes.length;
    }

    // index of the ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= suffixes.length) throw new IllegalArgumentException("index of out of range 0 - n-1.");
        return suffixes[i].index;
    }

    public static void main(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("The program must receive a string as argument.");
        CircularSuffixArray csa = new CircularSuffixArray(args[0]);

        StdOut.println("idx\t\tCSA");
        for (int i = 0; i < csa.length(); i++) {
            StdOut.printf("%3d\t\t%3d\n", i, csa.index(i));
        }
    }
}
