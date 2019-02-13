package org.eugene.cost.logic.util;

import java.math.BigDecimal;

public final class Calculate {
    private Calculate(){}

    public static String plus(String numOne, String numTwo){
        BigDecimal bigDecimalOne = new BigDecimal(numOne);
        BigDecimal bigDecimalTwo = new BigDecimal(numTwo);
        BigDecimal result = bigDecimalOne.add(bigDecimalTwo);
        return result.toString();
    }

    public static String minus(String numOne, String numTwo){
        BigDecimal bigDecimalOne = new BigDecimal(numOne);
        BigDecimal bigDecimalTwo = new BigDecimal(numTwo);
        BigDecimal result = bigDecimalOne.subtract(bigDecimalTwo);
        return result.toString();
    }

    public static String multiply(String num, String multiplier){
        BigDecimal bigDecimalOne = new BigDecimal(num);
        BigDecimal bigDecimalTwo = new BigDecimal(multiplier);
        BigDecimal result = bigDecimalOne.multiply(bigDecimalTwo);
        return result.toString();
    }

    public static String divide(String num, String divider){
        BigDecimal bigDecimalOne = new BigDecimal(num);
        BigDecimal bigDecimalTwo = new BigDecimal(divider);
        BigDecimal result = bigDecimalOne.divide(bigDecimalTwo,2,BigDecimal.ROUND_HALF_DOWN);
        return result.toString();
    }
}
