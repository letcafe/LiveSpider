package sort;

import org.junit.Test;

public class BubbleSort {

    @Test
    public void bubbleSort() {
        int[] rawArray = CorrectSort.rawArray;
        int len = rawArray.length;
        for (int i = 0; i < len - 1; i ++) {
            for (int j = 0; j < len - 1 - i; j ++) {
                if (rawArray[j] > rawArray[j + 1]) {
                    int temp = rawArray[j];
                    rawArray[j] = rawArray[j + 1];
                    rawArray[j + 1] = temp;
                }
            }
        }
        System.out.println(CorrectSort.compareList(rawArray));
    }
}
