package org.eugene.cost.service.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class SessionUtils {
    public static final String SESSION_REGEXP = "session_\\d+_\\d{1,2}\\.\\d{1,2}\\.\\d{4}_\\d{1,2}\\.\\d{1,2}\\.\\d{4}";

    private SessionUtils(){}

    public static String getSessionFileName(String limit, LocalDate beginDate, LocalDate finalDate){
        return "session_" + limit
                + "_" + dateToString(beginDate) + "_" + dateToString(finalDate);
    }

    private static String dateToString(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
