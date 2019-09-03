package org.eugene.cost.service.util;

public final class PaymentUtils {
    public static final String PAYMENT_REGEXP = "payment_[A-Za-z0-9]+";

    private PaymentUtils(){}

    public static String getPaymentFileName(String identify){
        return "payment_" + identify;
    }
}
