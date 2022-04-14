import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    // Static ints
    static final int NUM_SERVANTS = 4, NUM_PRESENTS = 500_000;

    // Shared resources
    static ConcurrentLinkedDeque<Integer> bag = new ConcurrentLinkedDeque<>();
    static AtomicInteger numNotes = new AtomicInteger(0);

    // Main Thread
    public static void main(String[] args) throws InterruptedException {
        // Start making the bag
        System.out.println("Making Bag...");

        List<Integer> tempBag = new ArrayList<>();
        for (int i = 0; i < NUM_PRESENTS; i++)
            tempBag.add(i);
        Collections.shuffle(tempBag);

        bag.addAll(tempBag);

        // Init the chain
        LockFreeList chain = new LockFreeList();

        // Start making the servants work
        System.out.println("Working Servants...");

        long start = System.currentTimeMillis();

        // One thread for each servant
        Thread[] servants = new Thread[NUM_SERVANTS];
        for (int i = 0; i < NUM_SERVANTS; i++) {
            servants[i] = new Thread(new Servant(chain));
            servants[i].start();
        }

        // Wait for them to stop
        for (int i = 0; i < NUM_SERVANTS; i++)
            servants[i].join();

        // Print output
        System.out.println("\n" + NUM_PRESENTS + " took " + (System.currentTimeMillis() - start) + " ms.\n");
    }
}
