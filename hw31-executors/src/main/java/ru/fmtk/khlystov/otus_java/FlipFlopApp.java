package ru.fmtk.khlystov.otus_java;

import java.util.concurrent.atomic.AtomicInteger;

public class FlipFlopApp {
    public static void main(String[] args) {
        // 1, 2 - what thread can step forward
        final AtomicInteger latch = new AtomicInteger(1);
        Thread t1 = new Thread(getWorker(1, latch));
        Thread t2 = new Thread(getWorker(2, latch));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Runnable getWorker(final int threadNumber, final AtomicInteger latch) {
        final String prefix = threadNumber == 1 ? "" : "\t";
        final String suffix = threadNumber == 1 ? "\n" : "";
        final int[] value = new int[1];
        return () -> {
            for (int idx = 1; idx <= 10; ++idx) {
                value[0] = idx;
                runWithLatch(threadNumber, latch, () -> System.out.print(prefix + value[0] + "\n"));
            }
            for (int idx = 9; idx >= 1; --idx) {
                value[0] = idx;
                runWithLatch(threadNumber, latch, () -> System.out.print(prefix + value[0] + "\n"));
            }
        };
    }

    private static void runWithLatch(final int threadNumber, final AtomicInteger latch, Runnable work) {
        final int nextThreadNumber = threadNumber == 1 ? 2 : 1;
        while (latch.get() != threadNumber){
            Thread.onSpinWait();
        }
        work.run();
        latch.compareAndSet(threadNumber, nextThreadNumber);
    }
}
