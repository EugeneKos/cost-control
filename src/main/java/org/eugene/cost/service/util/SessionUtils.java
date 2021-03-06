package org.eugene.cost.service.util;

import java.time.LocalDate;

import static org.eugene.cost.service.util.DateUtils.dateToString;

public final class SessionUtils {
    public static final String SESSION_REGEXP = "session_\\d+_\\d{1,2}\\.\\d{1,2}\\.\\d{4}_\\d{1,2}\\.\\d{1,2}\\.\\d{4}";

    private SessionUtils(){}

    public static String getSessionFileName(String limit, LocalDate beginDate, LocalDate finalDate){
        return "session_" + limit
                + "_" + dateToString(beginDate) + "_" + dateToString(finalDate);
    }

    public static String getSessionDescription(String limit, LocalDate beginDate, LocalDate finalDate){
        return String.format("%-20s%-10s%-20s%-15s%-20s%-15s%s", "Session", "Limit:",limit,
                "Begin Date:",dateToString(beginDate),
                "Final Date:",dateToString(finalDate));
    }
}
