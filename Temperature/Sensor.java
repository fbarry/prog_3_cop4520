import java.util.*;

public class Sensor implements Runnable {
    private static Random rand;

    private final int[] readings;

    private int totalReadings = 0;

    public Sensor(int[] readings) {
        rand = new Random();
        this.readings = readings;
    }

    private static int readTemp() {
        return rand.nextInt(Rover.RANGE + 1) - Math.abs(Rover.LOW);
    }

    public void run() {

        while (totalReadings * Rover.READING_INT < Rover.TOTAL_TIME) {
            int index = totalReadings++ % Rover.READINGS_PER_SENSOR;
            readings[index] = readTemp();

            try {
                Thread.sleep(Rover.READING_INT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
