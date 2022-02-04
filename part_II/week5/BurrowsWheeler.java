package part_II.week5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.List;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            sb.append(c);
        }
        CircularSuffixArray csa = new CircularSuffixArray(sb.toString());
        // first = index of the original suffix[0]
        int first = 0;
        int n = csa.length();
        char[] t = new char[csa.length()];
        // get t[], last column of the sorted suffixed matrix
        StringBuilder orginalSuffixT = new StringBuilder(sb.substring(n - 1)).append(sb.substring(0, n - 1));
        for (int i = 0; i < n; i++) {
            int sortedIdx = csa.index(i);
            if (sortedIdx == 0) first = i;
            t[i] = orginalSuffixT.charAt(sortedIdx);
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(t[i]);
        }
        BinaryStdOut.close(); // CRITICAL to close (see p.813 or the book)
    }

    // apply Burrow-Wheeler inverse transform,
    // reading from the standard input and writing to standard output
    public static void inverseTransform() {
        List<Queue<Integer>> charPos = new ArrayList<>(R);
        for (int i = 0; i < R; i++) charPos.add(new Queue<>());
        int first = 0;
        int n = 0;
        while (!BinaryStdIn.isEmpty()) {
            if (n == 0) {
                first = BinaryStdIn.readInt();
                n++;
                continue;
            }
            char c = BinaryStdIn.readChar();
            charPos.get(c).enqueue(n - 1);
            n++;
        }
        // use charPos (size R) to create sorted t
        int[] st = new int[n - 1];
        int j = 0;
        for (int i = 0; i < charPos.size(); i++) {
            if (!charPos.get(i).isEmpty()) {
                for (int x = 0; x < charPos.get(i).size(); x++) st[j++] = i;
            }
        }

        int[] next = new int[st.length];

        for (int i = 0; i < st.length; i++) {
            // find c in t and assign next[i] = j, where t[j] == c
            next[i] = charPos.get((char) st[i]).dequeue();
        }
        int i = 0;
        int curr = first;
        while (i++ < st.length) {
            BinaryStdOut.write((char) st[curr]);
            curr = next[curr];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrow-Wheeler transform
    // if args[0] is "+", apply Burrow-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length < 1)
            throw new IllegalArgumentException("args[0] must be '-' for encoding or '+' for decoding");
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
        else
            throw new IllegalArgumentException("Invalid args ('-' or '+')");
    }
}
