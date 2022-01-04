package part_II.week1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        // Using semantic relatedness to detect which noun is the least related (outcast) to other nouns
        String outcast = "";
        int maxDist = 0;
        for (String x : nouns) {
            int di = 0;
            for (String y : nouns) {
                if (!x.equals(y))
                    di += this.wordnet.distance(x, y);
            }
            if (di > maxDist) {
                maxDist = di;
                outcast = x;
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
