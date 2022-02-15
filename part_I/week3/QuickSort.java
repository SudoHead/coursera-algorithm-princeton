package part_I.week3;

import edu.princeton.cs.algs4.StdRandom;

public class QuickSort {
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        if (hi - lo == 1)
            return true;
        for (int i = lo + 1; i < hi; i++) {
            if (a[i - 1].compareTo(a[i]) > 0)
                return false;
        }
        return true;
    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    public static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j - 1);
        sort(a, j + 1, hi);
    }

    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        while (true) {
            while (less(a[++i], a[lo]))
                if (i == hi) break;

            while (less(a[lo], a[--j]))
                if (j == lo) break;

            if (i >= j) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    public static void main(String[] args) {
        int N = 10;
        Integer[] arr = new Integer[N];
        for (int i = 0; i < N; i++) {
            arr[i] = StdRandom.uniform(50);
        }
        QuickSort.sort(arr);
        System.out.println("Sorted: " + QuickSort.isSorted(arr, 0, arr.length - 1));
    }
}
