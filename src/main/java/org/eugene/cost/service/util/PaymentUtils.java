package org.eugene.cost.service.util;

import org.eugene.cost.data.OperationType;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static org.eugene.cost.service.util.DateUtils.dateToString;

public final class PaymentUtils {
    public static final String VALID_IDENTIFY = "[A-Za-z0-9\\-_]+";
    public static final String PAYMENT_REGEXP = "payment_" + VALID_IDENTIFY;

    private PaymentUtils(){}

    public static String getPaymentFileName(String identify){
        return "payment_" + identify;
    }

    public static String operationToString(LocalDate date, OperationType type, String transactionAmount, String description){
        return dateToString(date) + ": " + type + "  " + transactionAmount + " Руб.  " + description;
    }

    public static boolean isValidIdentify(String identify){
        if(StringUtils.isEmpty(identify)){
            return false;
        }
        return identify.matches(VALID_IDENTIFY);
    }
}
