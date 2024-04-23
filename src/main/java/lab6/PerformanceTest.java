package lab6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class PerformanceTest {
    private static final int NUM_THREADS_READERS = 30;
    private static final int NUM_THREADS_WRITERS = 100;
    private static final int NUM_OPERATIONS = 1000;
    private static final int OPERATION_COST = 0; // Adjust this value based on the expected cost of an operation

    public static void main(String[] args) {
        FineGrainedLockList fineGrainedLockList = new FineGrainedLockList();
        MonolithicLockLinkedList monolithicLockList = new MonolithicLockLinkedList();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS_WRITERS);

        long startTimeFineGrained = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS_WRITERS; i++) {
            executor.execute(() -> {
                for (int j = 0; j < NUM_OPERATIONS; j++) {
                    // Adjust the operation cost based on the expected cost
                    Node node = new Node("value" + j);
                    fineGrainedLockList.add(node, OPERATION_COST);
                }
            });
        }

        ExecutorService executor2 = Executors.newFixedThreadPool(NUM_THREADS_READERS);

        for(int i = 0; i < NUM_THREADS_READERS; i++) {
        	executor2.execute(() -> {
        		for(int j = 0; j < NUM_OPERATIONS; j++) {
        			Node node = new Node("value" + j);
        			fineGrainedLockList.contains(node);
        		}
        	});
        }
        executor.shutdown();

        executor2.shutdown();

        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTimeFineGrained = System.currentTimeMillis();
        long fineGrainedDuration = endTimeFineGrained - startTimeFineGrained;

        System.out.println("Fine-grained lock duration: " + fineGrainedDuration + " ms");

        // Repeat the process for the monolithic lock implementation

//        executor = Executors.newFixedThreadPool(NUM_THREADS);
//
//        long startTimeMonolithic = System.currentTimeMillis();
//
//        for (int i = 0; i < NUM_THREADS; i++) {
//            executor.execute(() -> {
//                for (int j = 0; j < NUM_OPERATIONS; j++) {
//                    // Adjust the operation cost based on the expected cost
//                    MonolithicLockNode node = new MonolithicLockNode("value" + j);
//                    monolithicLockList.add(node, OPERATION_COST);
//                    monolithicLockList.contains(node);
//                }
//            });
//        }
//
//        executor.shutdown();
//        try {
//            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        long endTimeMonolithic = System.currentTimeMillis();
//        long monolithicDuration = endTimeMonolithic - startTimeMonolithic;
//
//        System.out.println("Monolithic lock duration: " + monolithicDuration + " ms");
    }
}
