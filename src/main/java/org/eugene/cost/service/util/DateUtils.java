package org.eugene.cost.service.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtils {
    private DateUtils(){}

    public static String dateToString(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
