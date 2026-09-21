import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Task_2 {

    static AtomicLong totalHits = new AtomicLong(0);

    static class Worker extends Thread {

        private final long iterations;

        Worker(long iterations) {
            this.iterations = iterations;
        }

        @Override
        public void run() {
            Random random = new Random();

            for (long i = 0; i < iterations; i++) {
                double x = random.nextDouble();
                double y = random.nextDouble();

                if (x * x + y * y <= 1.0) {
                    totalHits.incrementAndGet();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        long totalIterations = 50_000_000L;
        int numberOfThreads = 4;
        long iterationsPerThread = totalIterations / numberOfThreads;

        long startSingle = System.currentTimeMillis();
        long singleHits = 0;
        Random random = new Random();

        for (long i = 0; i < totalIterations; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            if (x * x + y * y <= 1.0) {
                singleHits++;
            }
        }

        long durationSingle = System.currentTimeMillis() - startSingle;
        double piSingle = 4.0 * singleHits / totalIterations;

        System.out.println("Single Thread: hits = " + singleHits + ", pi = " + piSingle + ", time = " + durationSingle + " ms");

        totalHits.set(0);
        Thread[] threads = new Thread[numberOfThreads];

        long startMulti = System.currentTimeMillis();

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Worker(iterationsPerThread);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long durationMulti = System.currentTimeMillis() - startMulti;
        double piMulti = 4.0 * totalHits.get() / totalIterations;

        System.out.println("4 Threads (Atomic): hits = " + totalHits.get() + ", pi = " + piMulti + ", time = " + durationMulti + " ms");
    }
}