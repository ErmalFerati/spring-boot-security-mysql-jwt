package com.ermalferati.security.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class DateUtil {

    private DateUtil() {
    }

    public static Date getDateAfter(long value, ChronoUnit unit) {
        Instant calculatedInstantValue = new Date().toInstant().plus(value, unit);
        return Date.from(calculatedInstantValue);
    }
}
