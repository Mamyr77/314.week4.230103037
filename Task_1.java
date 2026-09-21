import java.util.Random;

public class Task_1 {

    static long totalHits = 0;

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
                    totalHits++;
                }
            }
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        long totalIterations = 50_000_000L;
        int numberOfThreads = 4;

        long iterationsPerThread =
                totalIterations / numberOfThreads;

        for (int run = 1; run <= 5; run++) {

            totalHits = 0;

            Thread[] threads =
                    new Thread[numberOfThreads];

            for (int i = 0; i < numberOfThreads; i++) {

                threads[i] =
                        new Worker(iterationsPerThread);

                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            double pi =
                    4.0 * totalHits / totalIterations;

            System.out.println(
                    "Run " + run +
                    ": hits = " + totalHits +
                    ", pi = " + pi
            );
        }
    }
}