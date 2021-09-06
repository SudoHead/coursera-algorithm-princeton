package week3;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import week2.ElementarySort;

import java.util.Arrays;

public class MergeSort <Item>{
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        if (hi - lo == 1)
            return true;
        for (int i = lo + 1; i < hi; i++) {
            if (a[i-1].compareTo(a[i]) > 0)
                return false;
        }
        return true;
    }

    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        assert isSorted(a, lo, mid);
        assert isSorted(a, mid + 1, hi);

        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        int k = lo;
        int j = mid + 1;
        int i = lo;
        while (k <= hi) {
            if (i > mid)
                a[k++] = aux[j++];
            else if (j > hi)
                a[k++] = aux[i++];
            else if (aux[j].compareTo(aux[i]) < 0)
                a[k++] = aux[j++];
            else
                a[k++] = aux[i++];
        }

        assert isSorted(a, lo, hi);
    }

    public static void sortTopDown(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo)
            return;
        int mid = lo + (hi - lo) / 2;
        sortTopDown(a, aux, lo, mid);
        sortTopDown(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    public static void sortBottomUp(Comparable[] a, Comparable[] aux) {
        for (int sz = 1; sz < a.length; sz += sz) {
            int offset = sz * 2;
            for (int lo = 0; lo < a.length - sz; lo += offset) {
                int hi = lo + offset - 1;
                int mid = lo + sz - 1;
                merge(a, aux, lo, mid, Math.min(hi, a.length - 1));
            }
        }
    }

    public static void sortExample() {
        Character[] a = "MERGESORTEXAMPLE".chars().mapToObj(c -> (char)c).toArray(Character[]::new);

        for (char c:a)
            System.out.print(c);
        System.out.println();

        MergeSort.sortBottomUp(a, new Comparable[a.length]);

        for (char c:a)
            System.out.print(c);
        System.out.println();

        System.out.println(MergeSort.isSorted(a, 0, a.length - 1));
    }

    public static void main(String[] args) {
        if (args.length != 1)
            return;
        int N = Integer.parseInt(args[0]);
        Integer[] arr = new Integer[N];
        for (int i = 0; i < N; i++) {
            arr[i] = StdRandom.uniform(1_000_000);
        }
        ElementarySort<Integer> sorter = new ElementarySort<Integer>();

        Integer[] copyArr = new Integer[N];
        System.arraycopy(arr, 0, copyArr, 0, N);
        Stopwatch stopwatch = new Stopwatch();
        double time;
//        sorter.selectionSort(copyArr);
//        time = stopwatch.elapsedTime();
//        System.out.println("SelectionSort: sorted = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

//        System.arraycopy(arr, 0, copyArr, 0, N);
//        stopwatch = new Stopwatch();
//        sorter.insertionSort(copyArr);
//        time = stopwatch.elapsedTime();
//        System.out.println("InsertionSort: sorted = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

//        System.arraycopy(arr, 0, copyArr, 0, N);
//        stopwatch = new Stopwatch();
//        sorter.shellSort(copyArr);
//        time = stopwatch.elapsedTime();
//        System.out.println("ShellSort: sorted = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

        System.arraycopy(arr, 0, copyArr, 0, N);
        stopwatch = new Stopwatch();
        Arrays.sort(copyArr);
        time = stopwatch.elapsedTime();
        System.out.println("Java.util.Arrays.sort: sorted = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

        System.arraycopy(arr, 0, copyArr, 0, N);
        stopwatch = new Stopwatch();
        MergeSort.sortTopDown(copyArr, new Comparable[arr.length], 0, arr.length - 1);
        time = stopwatch.elapsedTime();
        System.out.println("Mergesort top-down = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

        System.arraycopy(arr, 0, copyArr, 0, N);
        stopwatch = new Stopwatch();
        MergeSort.sortBottomUp(copyArr, new Comparable[arr.length]);
        time = stopwatch.elapsedTime();
        System.out.println("Mergesort bottom-up = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

        System.arraycopy(arr, 0, copyArr, 0, N);
        stopwatch = new Stopwatch();
        QuickSort.sort(copyArr);
        time = stopwatch.elapsedTime();
        System.out.println("Quicksort = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);
    }
}
