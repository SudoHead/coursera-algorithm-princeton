package part_II.week3.strings;

public class KeyIndexedCounting {
    public static void sort(int[] a, int R) {
        int n = a.length;
        int[] counts = new int[R + 1];
        int[] aux = new int[n];

        // 1st pass - count frequencies
        for (int i = 0; i < n; i++) {
            counts[a[i] + 1]++;
        }

        // 2nd pass - compute cumulates so we have order
        for (int r = 0; r < R; r++) {
            counts[r + 1] += counts[r];
        }

        // 3rd pass - move items
        for (int i = 0; i < n; i++) {
            aux[counts[a[i]]++] = a[i];
        }

        // 4rd pass - copy array back
        for (int i = 0; i < n; i++) {
            a[i] = aux[i];
        }
    }

    public static void main(String[] args) {
        int[] a = {9, 5, 2, 9, 2, 3, 4, 8};
        for (int i = 0; i < a.length; i++)
            System.out.printf("%d ", a[i]);
        System.out.println("Sorted:");
        KeyIndexedCounting.sort(a, 10);
        for (int i = 0; i < a.length; i++)
            System.out.printf("%d ", a[i]);
    }
}
