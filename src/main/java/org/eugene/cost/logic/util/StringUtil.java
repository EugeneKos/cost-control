package org.eugene.cost.logic.util;

public final class StringUtil {
    public static String deleteSpace(String input){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i) != ' '){
                stringBuilder.append(input.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    public static boolean checkSequence(String input){
        if(input.equals("")){
            return false;
        } else if(!input.matches("[0-9]+")){
            return input.matches("\\d+[.]\\d{1,2}");
        }
        return true;
    }
}
