import java.util.*;

public class Report implements Runnable {
    private static final String spacer = " ------------------------------- ", nl = "\n";
    private final int[][] readings;

    private static int index = 0;

    public Report(int[][] readings) {
        this.readings = readings;
    }

    private static void report(int[] minTemps, int[] maxTemps) {
        int maxDelta = Integer.MIN_VALUE, maxDeltaIndex = -1;

        for (int i = 0; i < Rover.READINGS_PER_SENSOR - Rover.CHANGE_INT; i++) {
            int max = maxTemps[i], min = minTemps[i];
            for (int j = 1; j < Rover.CHANGE_INT; j++) {
                max = Math.max(max, maxTemps[i + j]);
                min = Math.min(min, minTemps[i + j]);
            }

            int currDelta = max - min;
            if (currDelta > maxDelta) {
                maxDelta = currDelta;
                maxDeltaIndex = i;
            }
        }

        Arrays.sort(maxTemps);
        Arrays.sort(minTemps);

        Rover.out.println(nl + spacer + "Report #" + index + spacer + nl);

        Rover.out.print("The five highest reported temperatures were:");
        for (int i = Rover.READINGS_PER_SENSOR - 1; i > Rover.READINGS_PER_SENSOR - 6; i--)
            Rover.out.print(" " + maxTemps[i]);

        Rover.out.print(nl + "The five lowest reported temperatures were:");
        for (int i = 0; i < 5; i++)
            Rover.out.print(" " + minTemps[i]);

        Rover.out.println(nl + "The largest difference in temperatures was " + maxDelta + " between " + maxDeltaIndex
                + " and " + (maxDeltaIndex + Rover.CHANGE_INT));

        Rover.out.flush();
    }

    public void run() {
        while (index++ < Rover.NUM_REPORTS) {
            try {
                Thread.sleep(Rover.REPORT_INT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int[] minTemps = new int[Rover.READINGS_PER_SENSOR];
            int[] maxTemps = new int[Rover.READINGS_PER_SENSOR];

            Arrays.fill(minTemps, Rover.HIGH + 1);
            Arrays.fill(maxTemps, Rover.LOW - 1);

            for (int i = 0; i < Rover.READINGS_PER_SENSOR; i++) {
                for (int j = 0; j < Rover.NUM_SENSORS; j++) {
                    minTemps[i] = Math.min(minTemps[i], readings[j][i]);
                    maxTemps[i] = Math.max(maxTemps[i], readings[j][i]);
                }
            }

            report(minTemps, maxTemps);
        }
    }
}
