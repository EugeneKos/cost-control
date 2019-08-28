package org.eugene.cost.ui.common;

import org.springframework.util.StringUtils;

public final class UIUtils {
    public static final String RUB = " Руб.";

    public static final String RED_COLOR = "-fx-background-color: #ee4040";
    public static final String GREEN_COLOR = "-fx-background-color: #1fda1c";

    private UIUtils() {}

    public static String deleteSpace(String input){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i) != ' '){
                stringBuilder.append(input.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    public static boolean isContainsNumbers(String input){
        if(input.equals("")){
            return false;
        } else if(!input.matches("[0-9]+")){
            return input.matches("\\d+[.]\\d{1,2}");
        }
        return true;
    }

    public static boolean isNull(String... strings){
        boolean nonNull = true;
        for (String string : strings){
            nonNull &= !StringUtils.isEmpty(string);
        }
        return !nonNull;
    }
}
