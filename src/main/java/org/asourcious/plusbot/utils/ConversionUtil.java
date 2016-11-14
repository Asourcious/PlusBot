package org.asourcious.plusbot.utils;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public final class ConversionUtil {
    private ConversionUtil() {}

    public static ChronoUnit convert(TimeUnit unit) {
        switch (unit) {
            case DAYS:
                return ChronoUnit.DAYS;
            case HOURS:
                return ChronoUnit.HOURS;
            case MINUTES:
                return ChronoUnit.MINUTES;
            case SECONDS:
                return ChronoUnit.SECONDS;
            case MICROSECONDS:
                return ChronoUnit.MICROS;
            case MILLISECONDS:
                return ChronoUnit.MILLIS;
            case NANOSECONDS:
                return ChronoUnit.NANOS;
            default:
                return ChronoUnit.FOREVER;
        }
    }
}
