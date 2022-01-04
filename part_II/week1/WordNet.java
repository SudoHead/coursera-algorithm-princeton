package part_II.week1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

    private final SAP sap;
    private final Map<String, List<Integer>> synsetWordMap;
    private final Map<Integer, List<String>> synsetIdMap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("WordNet constructor arguments cannot be null");
        String[] hypernymsLines = new In(hypernyms).readAllLines();
        Digraph digraph = new Digraph(hypernymsLines.length);
        for (String line : hypernymsLines) {
            String[] values = line.split(",");
            int v = Integer.parseInt(values[0]);
            for (int i = 1; i < values.length; i++) {
                digraph.addEdge(v, Integer.parseInt(values[i]));
            }
        }
        if (new DirectedCycle(digraph).hasCycle())
            throw new IllegalArgumentException("Input files does not represent a DAG");

        this.sap = new SAP(digraph);
        this.synsetWordMap = new HashMap<>();
        this.synsetIdMap = new HashMap<>();
        for (String line : new In(synsets).readAllLines()) {
            String[] values = line.split(",");
            int id = Integer.parseInt(values[0]);
            for (String noun : values[1].split(" ")) {
                this.synsetWordMap.computeIfAbsent(noun, x -> new ArrayList<>());
                this.synsetWordMap.get(noun).add(id);
                this.synsetIdMap.computeIfAbsent(id, x -> new ArrayList<>());
                this.synsetIdMap.get(id).add(noun);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.synsetWordMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Word cannot be null");
        return this.synsetWordMap.containsKey(word);
    }

    private Iterable<Integer> getSynsetId(String noun) {
        return this.synsetWordMap.get(noun);
    }

    private Iterable<String> getSynsetNouns(int id) {
        return this.synsetIdMap.get(id);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("distance() args cannot be null");

        Iterable<Integer> v = this.getSynsetId(nounA);
        Iterable<Integer> w = this.getSynsetId(nounB);
        return this.sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("sap() args cannot be null");

        Iterable<Integer> v = this.getSynsetId(nounA);
        Iterable<Integer> w = this.getSynsetId(nounB);
        int ancestorId = this.sap.ancestor(v, w);
        if (ancestorId != -1)
            return String.join(" ", this.getSynsetNouns(ancestorId));
        return "";
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = WordNet.class.getResource("synsets.txt").getPath();
        String hypernyms = WordNet.class.getResource("hypernyms.txt").getPath();
        System.out.println("Synsets: " + synsets + "\n" + "hypernyms: " + hypernyms);

        WordNet wn = new WordNet(synsets, hypernyms);

        System.out.println("WordNet nouns:" + wn.nouns());

        System.out.println(wn.distance("think", "sphere"));

        System.out.println(wn.sap("think", "sphere"));


    }
}
