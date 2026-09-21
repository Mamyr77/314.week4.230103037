import java.util.concurrent.ThreadLocalRandom;

public class Task_3 {

    public static void main(String[] args) throws InterruptedException {

        long totalIterations = 100_000_000L;
        int[] threadCounts = {1, 2, 4, 8, 16, 32};

        runBenchmark(1, totalIterations); 

        System.out.printf("%-10s | %-12s | %-20s | %-12s%n", "Threads(T)", "Runtime(ms)", "Speedup (T1/TN)", "Efficiency");
        System.out.println("------------------------------------------------------------------");

        long baselineTime = 0;

        for (int threads : threadCounts) {
            long duration = runBenchmark(threads, totalIterations);

            if (threads == 1) {
                baselineTime = duration;
            }

            double speedup = (double) baselineTime / duration;
            double efficiency = (speedup / threads) * 100.0;

            System.out.printf("%-10d | %-12d | %-20.2fx | %-11.1f%%%n", 
                    threads, duration, speedup, efficiency);
        }
    }

    private static long runBenchmark(int threads, long totalIterations) throws InterruptedException {
        long iterationsPerThread = totalIterations / threads;
        long[] localHits = new long[threads];
        Thread[] workerThreads = new Thread[threads];

        long startTime = System.currentTimeMillis();

        for (int t = 0; t < threads; t++) {
            final int threadIdx = t;
            workerThreads[t] = new Thread(() -> {
                ThreadLocalRandom rng = ThreadLocalRandom.current();
                long count = 0;
                for (long i = 0; i < iterationsPerThread; i++) {
                    double x = rng.nextDouble();
                    double y = rng.nextDouble();
                    if (x * x + y * y <= 1.0) {
                        count++;
                    }
                }
                localHits[threadIdx] = count;
            });
            workerThreads[t].start();
        }

        for (int t = 0; t < threads; t++) {
            workerThreads[t].join();
        }

        return System.currentTimeMillis() - startTime;
    }
}