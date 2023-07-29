package ua.rozipp.core.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static long now() {
        return System.currentTimeMillis();
    }

    public static long nowSec() {
        return (long) Math.floor(now() * 0.001);
    }

    public static long nowMin() {
        return (long) Math.floor(nowSec() / 60);
    }

    public static long toTicks(long seconds) {
        return 20 * seconds;
    }

    public static long getTicksUntil(Date next) {
        Calendar c = Calendar.getInstance();
        Date now = c.getTime();

        long seconds = Math.abs((now.getTime() - next.getTime()) / 1000);

        return seconds * 20;
    }

    public static long minuteToMilisec(int minute) {
        return minute * 60000L;
    }
}
