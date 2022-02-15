package part_I.week2;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INITIAL_SIZE = 10;
    private Item[] arr;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[INITIAL_SIZE];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item null cannot be enqueued.");
        }
        if (N >= arr.length) {
            resizeGrow();
        }
        arr[N++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot Dequeue empty queue.");
        }
        int selected = StdRandom.uniform(N);
        Item removedItem = arr[selected];
        int lastIndex = N - 1;
        arr[selected] = arr[lastIndex];
        arr[lastIndex] = null;
        N--;
        if (N <= arr.length / 4 && arr.length > INITIAL_SIZE) {
            resizeShrink();
        }
        return removedItem;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Dequeue is empty.");
        }
        return arr[StdRandom.uniform(N)];
    }

    private void resizeGrow() {
        Item[] biggerArr = (Item[]) new Object[arr.length * 2];
        int index = 0;
        for (Item e : arr) {
            if (e != null) {
                biggerArr[index++] = e;
            }
        }
        arr = biggerArr;
    }

    private void resizeShrink() {
        Item[] smallerArr = (Item[]) new Object[arr.length / 2];
        int index = 0;
        for (Item e : arr) {
            if (e != null) {
                smallerArr[index++] = e;
            }
        }
        arr = smallerArr;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] shuffled = (Item[]) new Object[N];
        private int currentSize = N;

        public RandomizedQueueIterator() {
            super();
            int index = 0;
            for (Item e : arr) {
                if (e != null) {
                    shuffled[index++] = e;
                }
                if (index == N) {
                    break;
                }
            }
            StdRandom.shuffle(shuffled);
        }

        public boolean hasNext() {
            return currentSize > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator has no more next Items");
            }
            return shuffled[--currentSize];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        System.out.println("Start");
        for (int i = 1; i < 20; i++) {
            rq.enqueue(i);
        }
        System.out.println("Size: " + rq.size());
        System.out.println("Sample: " + rq.sample());
        System.out.println("Sample: " + rq.sample());
        System.out.println("Sample: " + rq.sample());
        for (Integer i : rq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("Dequeuing: " + rq.dequeue());
        for (Integer i : rq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("Dequeuing: " + rq.dequeue());
        for (Integer i : rq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("Dequeuing: " + rq.dequeue());
        for (Integer i : rq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("Dequeuing: " + rq.dequeue());
        for (Integer i : rq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("Dequeuing: " + rq.dequeue());
        for (Integer i : rq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("Dequeuing: " + rq.dequeue());
        for (Integer i : rq) {
            System.out.print(" " + i + ",");
        }
        System.out.println();

        System.out.println("Queue is " + (rq.isEmpty() ? "" : "NOT ") + "empty");

        System.out.println("End");
    }
}
