package bytedance;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String input = "a  bb      c d     e";
        char[] inputArr = input.toCharArray();
        int startRemoveIndex = 0;
        int counter = 0;
        for (int i = 0; i < inputArr.length - 1;) {
            if (inputArr[i] == ' ' && inputArr[i + 1] == ' ') {
                removeArrOneStep(inputArr, i);
            } else {
                i ++;
            }
        }
        System.out.println(Arrays.toString(inputArr));
    }

    private static void removeArrOneStep(char[] inputArr, int index) {
        for (int i = index; i < inputArr.length - 1; i ++) {
            inputArr[i] = inputArr[i + 1];
        }
    }
}
