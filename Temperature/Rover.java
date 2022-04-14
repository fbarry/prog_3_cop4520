import java.io.*;

public class Rover {

    static final int LOW = -100, HIGH = 70, RANGE = HIGH - LOW, NUM_SENSORS = 8,
            READING_INT = 50, REPORT_INT = READING_INT * 60, CHANGE_INT = 10,
            READINGS_PER_SENSOR = REPORT_INT / READING_INT, NUM_REPORTS = 5, TOTAL_TIME = REPORT_INT * NUM_REPORTS;

    static PrintWriter out;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        out = new PrintWriter(new File("reports.txt"));

        int[][] readings = new int[NUM_SENSORS][READINGS_PER_SENSOR];

        Thread[] sensors = new Thread[NUM_SENSORS];
        for (int i = 0; i < NUM_SENSORS; i++) {
            sensors[i] = new Thread(new Sensor(readings[i]));
            sensors[i].start();
        }

        Thread report = new Thread(new Report(readings));
        report.start();
    }
}
