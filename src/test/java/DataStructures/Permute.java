package DataStructures;

import org.junit.Test;

public class Permute {

    @Test
    public void main() {
        permute("abc");
    }

    private void permute(String str) {
        permute(str.toCharArray(), 0, str.length() - 1);
    }

    private void permute(char[] str, int low, int high) {
        // 当两个指针重合时
        if (low == high) {
            for (int i = 0; i <= high; i ++) {
                System.out.print(str[i]);
            }
            System.out.println();
        } else {
            for (int i = low; i <= high; i ++) {
                // 从固定的数后第一个依次交换
                swap(str, i, low);
                permute(str, low + 1, high);
                // 在递归完成之后需要交换回来
                swap(str, low, i);
            }
        }
    }

    private void swap(char[] str, int start, int end) {
        char temp = str[start];
        str[start] = str[end];
        str[end] = temp;
    }
}
