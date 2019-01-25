package sort;

import org.junit.Test;

public class InsertSort {

    @Test
    public void insertSort() {
        int[] rawArray = CorrectSort.rawArray;
        int len = rawArray.length;
        for (int i = 0; i < len - 1; i ++) {
            int swapIndex = i;
            for (int j = i + 1; j < len; j ++) {
                if (rawArray[j] < rawArray[swapIndex]) {
                    swapIndex = j;
                }
            }
            int temp = rawArray[i];
            rawArray[i] = rawArray[swapIndex];
            rawArray[swapIndex] = temp;
        }
        System.out.println(CorrectSort.compareList(rawArray));
    }
}
