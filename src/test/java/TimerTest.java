import com.tracelink.wv.server.server.WorldviewServer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Tests for Timer class
 */
public class TimerTest {

    private final Logger logger = LoggerFactory.getLogger(TimerTest.class);

    @Test
    public void withDelayTest() throws InterruptedException {
        printStartTime();
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "\n" +
                        "Thread's name: " + Thread.currentThread().getName());
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 1000L;
        timer.schedule(task, delay);

        Thread.sleep(1500);
    }

    @Test
    public void startAtAGivenTime() throws InterruptedException {
        printStartTime();
        List<String> oldDatabase = Arrays.asList("Harrison Ford", "Carrie Fisher", "Mark Hamill");
        List<String> newDatabase = new ArrayList<>();
        newDatabase.add("Zihao Wang");

        LocalDateTime twoSecondsLater = LocalDateTime.now().plusSeconds(1);
        Date twoSecondsLaterAsDate = Date.from(twoSecondsLater.atZone(ZoneId.systemDefault()).toInstant());

        new Timer().schedule(new DatabaseMigrationTask(oldDatabase, newDatabase), twoSecondsLaterAsDate);

        Thread.sleep(1500);
    }

    public class DatabaseMigrationTask extends TimerTask {
        private List<String> oldDatabase;
        private List<String> newDatabase;

        public DatabaseMigrationTask(List<String> oldDatabase, List<String> newDatabase) {
            this.oldDatabase = oldDatabase;
            this.newDatabase = newDatabase;
        }

        @Override
        public void run() {
            newDatabase.addAll(oldDatabase);
            System.out.println("new DB: " + newDatabase);
            printFinishTime();
        }
    }

    private void printStartTime() {
        System.out.println("Start time: " + new Date() + " - ts: " + System.currentTimeMillis());
    }
    private void printFinishTime() {
        System.out.println("Finish time: " + new Date() + " - ts: " + System.currentTimeMillis());
    }

    /**
     * ==============================================================================================
     */
    public class NewsletterTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("Email sent at: "
                    + LocalDateTime.ofInstant(Instant.ofEpochMilli(scheduledExecutionTime()),
                    ZoneId.systemDefault()));
        }
    }

    @Test
    public void fixedRateTest() throws InterruptedException {
        new Timer().scheduleAtFixedRate(new NewsletterTask(), 0, 1000);

        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
        }
    }

    @Test
    public void fixedRateWithStartTimeTest() throws InterruptedException {
        printStartTime();
        LocalDateTime oneSecondLater = LocalDateTime.now().plusSeconds(1);
        Date oneSecondLaterAsDate = Date.from(oneSecondLater.atZone(ZoneId.systemDefault()).toInstant());
        new Timer().scheduleAtFixedRate(new NewsletterTask(), oneSecondLaterAsDate, 1000);

        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
        }
    }

    @Test
    public void fixedRateWithStartTimePriorThanNowTest() throws InterruptedException {
        printStartTime();
        // Note here is -3.
        // Result is it will execute the run() from the given start time, then repeatedly run() till the time is greater
        // than now, then use normal interval.
        // I guess behind it is calculate the ts to run() and compare with now. If ts <= now(), then run().
        LocalDateTime oneSecondBefore = LocalDateTime.now().minusSeconds(3);
        Date oneSecondLaterAsDate = Date.from(oneSecondBefore.atZone(ZoneId.systemDefault()).toInstant());
        new Timer().scheduleAtFixedRate(new NewsletterTask(), oneSecondLaterAsDate, 1000);

        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
        }
    }

    @Test
    public void listZoneIds() {
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        System.out.println("available zone ids: " + availableZoneIds);

        ZoneId gmt = ZoneId.of("GMT");
        System.out.println("GMT: " + gmt);
    }

    @Test
    public void createLocalDateTime() {
        ZoneId gmt = ZoneId.of("GMT");
        LocalDate gmtDate = LocalDate.now(gmt);
        LocalTime gmtTime = LocalTime.of(12, 0);
        LocalDateTime gmtDateTime = LocalDateTime.of(gmtDate, gmtTime);
        System.out.println("Created GMT time: " + gmtDateTime);

        // America/New_York timezone now
        LocalDateTime nyTime = LocalDateTime.now();
        System.out.println("NY time: " + nyTime);
    }

    @Test
    public void compareTime() {
        ZoneId gmt = ZoneId.of("GMT");
        LocalDateTime gmtDateTime = LocalDateTime.now(gmt);
        System.out.println("GMT time now: " + gmtDateTime);

        // America/New_York timezone
        LocalDateTime oneHourLater = gmtDateTime.plusHours(1);
        System.out.println("oneHourLater: " + oneHourLater);
        System.out.println("oneHourLater is greater than now: " + oneHourLater.isAfter(gmtDateTime));
    }

    @Test
    public void changeTimeToNewTimeZone() {
        LocalDateTime oldDateTime = LocalDateTime.now();
        System.out.println("Old:");
        System.out.println(oldDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        ZoneId oldZone = ZoneId.of("America/New_York");

        ZoneId newZone = ZoneId.of("GMT");
        LocalDateTime newDateTime = oldDateTime.atZone(oldZone)
                .withZoneSameInstant(newZone)
                .toLocalDateTime();

        System.out.println("New:");
        System.out.println(newDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Test
    public void plusHoursTest() {
        LocalDateTime localCurrentDateTime = LocalDateTime.now();
        LocalDateTime initialStartDateTime = localCurrentDateTime.minusHours(5);

        // Make sure the start time is after now
        while (localCurrentDateTime.isAfter(initialStartDateTime)) {
            System.out.println("new time: " + initialStartDateTime);
            initialStartDateTime = initialStartDateTime.plusHours(1);
        }

//        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        logger.info("HOUR_OF_DAY: {}", c.get(Calendar.HOUR_OF_DAY));
    }

    /**
     * Get the timestamp of the current date and hour in GMT
     * e.g.,
     * current time is 2021-01-12 14:56:10 in America/New_York
     * return the timestamp of 2021-01-12 19:00:00 in GMT (19:00:00 in GMT is 14:00:00 in America/New_York)
     */
    @Test
    public void currentHourToTimestampTest() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Method 1: too complicated!! See method 2
        // Use GMT timezone
        ZoneId gmt = ZoneId.of("GMT");
        LocalDate localDate = LocalDate.now(gmt);
        logger.info("LocalDate: {}", localDate);

        int hr24 = c.get(Calendar.HOUR_OF_DAY);
        LocalTime localTime = LocalTime.of(hr24, 0);
        logger.info("LocalTime: {}", localTime);

        // The start tome of backup, which maybe already passed
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        logger.info("LocalDateTime: {}", localDateTime);
        Date nowInDate = Date.from(localDateTime.atZone(gmt).toInstant());
        logger.info("nowInDate: {}", nowInDate);
        long nowTimestamp = nowInDate.getTime();
        logger.info("nowTimestamp: {}", nowTimestamp);
        long currentTimeMillis = System.currentTimeMillis();
        logger.info("currentTimeMillis: {}", currentTimeMillis);
        logger.info("Diff: {}", currentTimeMillis - nowTimestamp);


        // Method 2: too simple and good!
        //set mills, seconds, minutes to 0 so we get exactly the hour
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        // This gets the time in milliseconds
        long result = c.getTime().getTime();
        logger.info("nowTimestamp from Calendar: {}", result);
    }

    @Test
    public void calendarGetCurrentHrDateTest() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        int currentHr = calendar.get(Calendar.HOUR_OF_DAY);
        int startHr = Math.max(currentHr, currentHr - 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, startHr);
        Date start = calendar.getTime();
        logger.info("Date start: {}", start);
        logger.info("Timezone: {}", calendar.getTimeZone());

        calendar = Calendar.getInstance();
        currentHr = calendar.get(Calendar.HOUR_OF_DAY);
        startHr = Math.max(currentHr, currentHr - 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, startHr);
        start = calendar.getTime();
        logger.info("Date start: {}", start);
        logger.info("Timezone: {}", calendar.getTimeZone());
    }
}
