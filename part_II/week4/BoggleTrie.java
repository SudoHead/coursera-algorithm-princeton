package part_II.week4;

import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class BoggleTrie implements Iterable<String> {
    private static final int R = 26;  // 26-way trie
    private static final char R_OFFSET = 'A';

    private Node root;
    private int n;

    // R-way trie node
    public static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
        private String word = "";

        public Node getNext(char c) {
            return next[c - R_OFFSET];
        }

        public boolean isString() {
            return this.isString;
        }

        public String getWord() {
            return word;
        }

        public String toString() {
            return new StringBuilder(word).append(isString ? ": y" : ": n").toString();
        }
    }

    public BoggleTrie() {
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    public Node get(String key) {
        return get(root, key, 0);
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - R_OFFSET], key, d + 1);
    }

    public void add(String key) {
        if (key == null) throw new IllegalArgumentException("argument to add() is null");
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isString) n++;
            x.isString = true;
            x.word = key;
        } else {
            char c = key.charAt(d);
            x.next[c - R_OFFSET] = add(x.next[c - R_OFFSET], key, d + 1);
        }
        return x;
    }

    public int size() {
        return n;
    }

    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.isString) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            int letter = c + R_OFFSET;
            prefix.append((char) letter);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

}
