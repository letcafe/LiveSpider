package com.letcafe.http;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class MultiThreadsTest {

    static final int worker = 5;

    static final int taskSize = 10000;

    List<Integer> multiThreadList = new CopyOnWriteArrayList<>();

    CountDownLatch latch;

    class MultiThreadAdder implements Runnable{
        private int id;
        private int start;
        private int end;

        public MultiThreadAdder(int id, int start, int end) {
            this.id = id;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            System.out.printf("[Worker %d] from %d to %d\n", id, start, end);
            for (int i = start; i < end; i ++) {
                multiThreadList.add(i);
            }
            latch.countDown();
        }
    }

//    @Test
    public void MultiThreadInsert() throws InterruptedException {
        latch = new CountDownLatch(worker);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < worker; i ++) {
            service.execute(new MultiThreadAdder(i, i * taskSize, (i + 1) * taskSize));
        }
        long startTime = System.currentTimeMillis();
        latch.await();
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("MultiThreadAdder shut down. CostTime= " + costTime + "us");
        System.out.println("[multiThreadList.size()] = " + multiThreadList.size());
        service.shutdown();
    }

    @Test
    public void SingleThreadTest() throws InterruptedException {
        latch = new CountDownLatch(1);
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new MultiThreadAdder(1, 0, worker * taskSize));
        long startTime = System.currentTimeMillis();
        latch.await();
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.println("SingleThreadTest shut down. CostTime= " + costTime + "us");
        System.out.println("[SingleThreadTest.size()] = " + multiThreadList.size());
    }
}
