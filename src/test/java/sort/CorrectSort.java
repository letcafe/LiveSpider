package sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class CorrectSort {
    public static final int listSize = 100;

    public static final Random rand = new Random(47);

    public static int[] rawArray = new int[listSize];

    public static int[] sortedArray = new int[listSize];

    static {
        for (int i = 0; i < listSize; i ++) {
            rawArray[i] = rand.nextInt(1000);
        }
        sortedArray = rawArray.clone();
        Arrays.sort(sortedArray);
    }

    @Test
    public void getRawList() {
        System.out.println(Arrays.toString(rawArray));
    }

    public static boolean compareList(int[] arr) {
        if (arr.length != sortedArray.length) {
            return false;
        }
        int listSizeForCheck = arr.length;
        for (int i = 0;i < listSizeForCheck; i ++) {
            if (arr[i] != sortedArray[i]) {
                return false;
            }
        }
        return true;
    }
}
