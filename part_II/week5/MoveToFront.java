package part_II.week5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    private static char[] initializeASCII() {
        char[] ascii = new char[R]; // extended ASCII
        for (int i = 0; i < R; i++) {
            ascii[i] = (char) i;
        }
        return ascii;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] ascii = initializeASCII();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            // move to front
            char prev = 0, temp = 0;
            for (int j = 0; j < R; j++) {
                // shift elements if ascii[j] != c
                temp = ascii[j];
                if (j > 0) ascii[j] = prev;
                if (temp == c) {
                    ascii[0] = c;
                    BinaryStdOut.write((char) j);
                    break;
                }
                prev = temp;
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] ascii = initializeASCII();
        while (!BinaryStdIn.isEmpty()) {
            int c = BinaryStdIn.readChar();

            char decodedChar = ascii[c];
            // read the char
            BinaryStdOut.write(decodedChar);
            // move to front
            for (int x = c; x > 0; x--) ascii[x] = ascii[x - 1];
            ascii[0] = decodedChar;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length < 1)
            throw new IllegalArgumentException("args[0] must be '-' for encoding or '+' for decoding");
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            throw new IllegalArgumentException("Invalid args ('-' or '+')");
    }
}
