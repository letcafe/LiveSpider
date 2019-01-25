package DataStructures;

import org.junit.Test;

import java.util.*;

public class FindTopK {

    static int[] array = new int[10000];

    int K = 1000;

    static {
        long startTime;
        long costTime;
        startTime = System.currentTimeMillis();
        Random rand = new Random(47);
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(100000);
        }
        costTime = System.currentTimeMillis() - startTime;
        System.out.println("[Init data cost time] = " + costTime + "ms");
    }

    @Test
    public void rightResult() {
        long startTime;
        long costTime;
        startTime = System.currentTimeMillis();
        Random rand = new Random(47);

        int size = array.length;
        LinkedList<Integer> sortedList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            sortedList.add(array[i]);
        }
        sortedList.sort(Comparator.reverseOrder());
        costTime = System.currentTimeMillis() - startTime;
        System.out.println("[rightResult] = " + costTime + "ms");
    }

    @Test
    public void getTopKByAllSort() {
        long startTime;
        long costTime;
        startTime = System.currentTimeMillis();
        int size = array.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = size - 1; j > i; j--) {
                if (array[j] > array[j - 1]) {
                    int temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                }
            }
        }
        costTime = System.currentTimeMillis() - startTime;
        System.out.println("[getTopKByAllSort] = " + costTime + "ms");

    }

    @Test
    public void getTopKByArray() {
        // 开始计时
        long startTime;
        long costTime;
        startTime = System.currentTimeMillis();
        int size = array.length;
        int[] topK = new int[K];
        // 获取数组前K个元素，复杂度O(K)
        for (int i = 0; i < topK.length; i++) {
            topK[i] = array[i];
        }
        // 排序（冒泡），复杂度O(K^2)
        for (int i = 0; i < topK.length - 1; i++) {
            for (int j = topK.length - 1; j > i; j--) {
                if (topK[j] > topK[j - 1]) {
                    int temp = topK[j];
                    topK[j] = topK[j - 1];
                    topK[j - 1] = temp;
                }
            }
        }
        // 从总数组第K+1个元素开始到最后
        for (int i = topK.length; i < size; i++) {
            // 如果该元素大于第K个元素
            if (array[i] > topK[topK.length - 1]) {
                // 遍历插入的索引
                for (int j = 0; j < topK.length - 1; j++) {
                    if (topK[j] < array[i]) {
                        // 从后往前开始移位
                        for (int k = topK.length - 1; k > j; k--) {
                            topK[k] = topK[k - 1];
                        }
                        // 插入节点
                        topK[j] = array[i];
                        break;
                    }
                }
            }
        }
        // 统计运行时间
        costTime = System.currentTimeMillis() - startTime;
        System.out.println("[getTopKByArray] = " + costTime + "ms");
        List<Integer> myList = new ArrayList<>();
        for (Integer item : topK) {
            myList.add(item);
        }
        System.out.println("[List] = " + myList);

    }

    @Test
    public void getTopKByList() {
        int size = array.length;
        List<Integer> topK = new ArrayList<>();
        // 获取数组前K个元素，复杂度O(K)
        for (int i = 0; i < K; i++) {
            topK.add(array[i]);
        }
        topK.sort(Comparator.reverseOrder());
        // 从总数组第K+1个元素开始到最后
        for (int i = K; i < size; i++) {
            // 如果该元素大于第K个元素
            if (array[i] > topK.get(K - 1)) {
                // 遍历插入的索引
                for (int j = 0; j < K - 1; j++) {
                    if (topK.get(j) < array[i]) {
                        topK.add(j, array[i]);
                        topK.remove(K);
                        break;
                    }
                }
            }
        }
    }
}
