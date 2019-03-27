package sort;

import java.util.Arrays;

public class BitSetDemo {

    public static void main(String[] args) {
        int[] arr = new int[]{1, 23, 12, 67, 412, 3, 56, 234, 1, 2, 3, 76};
        new BitSetDemo().mergeSort(arr,0 , arr.length - 1);
        System.out.println("[arr] = " + Arrays.toString(arr));
    }

    public void mergeSort(int[] arr, int start, int end) {
        if (arr == null || start >= end) {
            return;
        }
        int middle = (start + end) / 2;
        mergeSort(arr, start, middle);
        mergeSort(arr, middle + 1, end);
        merge(arr, start, middle, end);
    }

    private void merge(int[] arr, int start, int middle, int end) {
        int i = start;
        int j = middle + 1;
        int k = 0;
        int[] tmp = new int[end - start + 1];
        while (i <= middle && j <= end) {
            if (arr[i] > arr[j]) {
                tmp[k] = arr[j];
                j++;
            } else {
                tmp[k] = arr[i];
                i++;
            }
            k++;
        }
        while (i <= middle) {
            tmp[k] = arr[i];
            i++;
            k++;
        }
        while (j <= end) {
            tmp[k] = arr[j];
            j++;
            k++;
        }
        for (int p = 0; p < tmp.length; p++) {
            arr[start + p] = tmp[p];
        }
    }
}
