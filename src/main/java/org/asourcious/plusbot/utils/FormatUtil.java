package org.asourcious.plusbot.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

public final class FormatUtil {
    private FormatUtil() {}

    public static String getFirstArgument(String message) {
        return message.split("\\s+")[0];
    }

    public static String getFormatted(Object[] ary) {
        if (ary.length == 0)
            return "None";

        String raw = Arrays.toString(ary);
        return raw.substring(1, raw.length() - 1);
    }

    public static String getFormatted(Collection collection) {
        if (collection.isEmpty())
            return "None";

        String raw = collection.toString();
        return raw.substring(1, raw.length() - 1);
    }

    public static String getFormattedTime(OffsetDateTime time) {
        return time.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    public static String getFormattedDuration(OffsetDateTime startTime, OffsetDateTime endTime) {
        return DurationFormatUtils.formatDurationWords(Duration.between(startTime, endTime).toMillis(), true, true);
    }
}
