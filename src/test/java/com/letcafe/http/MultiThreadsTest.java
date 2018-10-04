package com.letcafe.http;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadsTest {

    private static int capacity = 30 * 10000;

    private static int addCount = 0;

    static long endTimestamp = 0;

    static List[] lists = new List[10];

    static int threadNumber = Runtime.getRuntime().availableProcessors();

    static List<MultiThreadsTest> testList = new ArrayList<>(capacity);

    static {
        MultiThreadsTest multiThreadsTest = new MultiThreadsTest();
        for (int i = 0; i < capacity; i++) {
            testList.add(multiThreadsTest);
        }
        System.out.println("[testList.size()] = " + testList.size() + "," + capacity / 10);
        for (int i = 0; i < 10; i ++) {
            lists[i] = testList.subList(capacity / 10 * i, capacity / 10 * (i + 1));
            System.out.println(lists[i].size() + "\\" + lists[i].get(0));
        }
    }

//    @Test
    public void multiThreads() throws InterruptedException {
        int threadNumber = Runtime.getRuntime().availableProcessors();
        MultiThreadsForHuYaLiveInsertAccelerate[] threads = new MultiThreadsForHuYaLiveInsertAccelerate[threadNumber];
        System.out.println(testList.size());
        // initialize thread array
        for (int i = 0; i < threadNumber; i++) {
            threads[i] = new MultiThreadsForHuYaLiveInsertAccelerate(i);
            threads[i].setName("Thread-" + i);
        }
        // start to count
        long startTimestamp = System.currentTimeMillis();
        // start threads
        for (int i = 0; i < threadNumber; i++) {
            threads[i].start();
        }
        Thread.sleep(5 * 1000);
        System.out.println("addCount = " + addCount);
        System.out.println("run time = " + (endTimestamp - startTimestamp) + " ms");
    }

    @Test
    public void singleThread() throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();
        for (int i = 0; i < testList.size(); i++) {
//            System.out.println(i);
            addCount++;
            if (addCount == capacity) {
                endTimestamp = System.currentTimeMillis();
            }
        }
        System.out.println("addCount = " + addCount);
        System.out.println("run time = " + (endTimestamp - startTimestamp) + " ms");
    }

    class MultiThreadsForHuYaLiveInsertAccelerate extends Thread {
        int startIndex;
        int endIndex;
        int roundNumber;
        int threadCount;

        public MultiThreadsForHuYaLiveInsertAccelerate(int roundNumber) {
            this.roundNumber = roundNumber;
            this.startIndex = capacity / threadNumber * roundNumber;
            this.endIndex = (capacity / threadNumber) * (roundNumber + 1) - 1;
        }

        @Override
        public void run() {
            synchronized (testList) {
                for (int i = startIndex; i <= endIndex; i++) {
                    addCount++;
                    if (addCount == capacity) {
                        endTimestamp = System.currentTimeMillis();
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "MultiThreadsForHuYaLiveInsertAccelerate{" +
                    "startIndex=" + startIndex +
                    ", endIndex=" + endIndex +
                    ", roundNumber=" + roundNumber +
                    '}';
        }
    }
}
