package org.asourcious.plusbot.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.OffsetDateTime;

public final class FormatUtil {
    private FormatUtil() {}

    public static String getFormattedDuration(OffsetDateTime startTime, OffsetDateTime endTime) {
        return DurationFormatUtils.formatDurationWords(Duration.between(startTime, endTime).toMillis(), true, true);
    }
}
