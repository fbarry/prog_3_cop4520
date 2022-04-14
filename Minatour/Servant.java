import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

class Servant implements Runnable {
    private static Random rand = new Random();
    private AtomicReference<LockFreeList> chain;
    private static boolean toggle = true;

    // Shared chain
    public Servant(LockFreeList chain) {
        this.chain = new AtomicReference<>(chain);
    }

    public void run() {
        // Keep going until all notes are written
        while (Main.numNotes.get() < Main.NUM_PRESENTS) {
            // Get task
            String task = toggle ? "ADD" : "REMOVE";
            toggle ^= true;

            int currTag = LockFreeList.FAIL;

            switch (task) {

                // Add the next tag to the chain
                case "ADD":
                    try {
                        // Get the next tag from the bag
                        currTag = Main.bag.pop();
                        // Add it to the chain
                        chain.get().add(currTag);
                    } catch (Exception e) {
                        // The bag is empty - this is needed in case
                        // Something changes between the while loop check
                        // And the pop
                    }
                    break;

                // Remove the next tag in the list
                case "REMOVE":
                    // Remove returns the value
                    currTag = chain.get().remove();
                    // If it failed, don't increment numNotes
                    if (currTag != LockFreeList.FAIL)
                        Main.numNotes.incrementAndGet();
                    break;

                // The assignment doesn't suggest the servants are ever told to find.
                // I don't use it because it will just slow down the program.
                // I implemented it just in case.
                case "FIND":
                    // Get the tag to find
                    currTag = rand.nextInt(Main.NUM_PRESENTS);
                    // Check if it is contained in the chain
                    chain.get().contains(currTag);
                    break;
            }
        }
    }
}