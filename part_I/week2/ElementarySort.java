package part_I.week2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

public class ElementarySort<Item extends Comparable<Item>> {

    public ElementarySort() {
    }

    private void exchange(Item[] a, int x, int y) {
        // exchange a[x] with a[y]
        Item tmp = a[x];
        a[x] = a[y];
        a[y] = tmp;
    }

    public void selectionSort(Item[] a) {
        for (int i = 0; i < a.length; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[min]) < 0) {
                    min = j;
                }
            }
            exchange(a, min, i);
        }
    }

    public void insertionSort(Item[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                if (a[j].compareTo(a[j - 1]) < 0) {
                    exchange(a, j, j - 1);
                } else {
                    break;
                }
            }
        }
    }

    public void shellSort(Item[] a) {
        int N = a.length;
        int h = 1;
        // 3x + 1 sequence
        while (h < N / 3) {
            h = 3 * h + 1;
        }

        while (h >= 1) {
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h && a[j].compareTo(a[j - h]) < 0; j -= h) {
                    exchange(a, j, j - h);
                }
            }
            h = h / 3;
        }
    }

    public boolean isSorted(Item[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i].compareTo(a[i + 1]) > 0) {
                return false;
            }
        }
        return true;
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
        sorter.selectionSort(copyArr);
        double time = stopwatch.elapsedTime();
        System.out.println("SelectionSort: sorted = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

        System.arraycopy(arr, 0, copyArr, 0, N);
        stopwatch = new Stopwatch();
        sorter.insertionSort(copyArr);
        time = stopwatch.elapsedTime();
        System.out.println("InsertionSort: sorted = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

        System.arraycopy(arr, 0, copyArr, 0, N);
        stopwatch = new Stopwatch();
        sorter.shellSort(copyArr);
        time = stopwatch.elapsedTime();
        System.out.println("ShellSort: sorted = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);

        System.arraycopy(arr, 0, copyArr, 0, N);
        stopwatch = new Stopwatch();
        Arrays.sort(copyArr);
        time = stopwatch.elapsedTime();
        System.out.println("Java.util.Arrays.sort: sorted = " + sorter.isSorted(copyArr) + ", elapsed time = " + time);
    }
}
