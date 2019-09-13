package org.eugene.cost.ui.common;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.swing.*;

public final class UIUtils {
    private static Logger LOGGER = Logger.getLogger(UIUtils.class);

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

    public static void showOptionPane(String message, String title, MessageType messageType){
        int jMessageType;

        switch (messageType){
            case ERROR:
                jMessageType = JOptionPane.ERROR_MESSAGE;
                break;
            case WARNING:
                jMessageType = JOptionPane.WARNING_MESSAGE;
                break;
            case QUESTION:
                jMessageType = JOptionPane.QUESTION_MESSAGE;
                break;
            case INFORMATION:
                jMessageType = JOptionPane.INFORMATION_MESSAGE;
                break;
                default:
                    LOGGER.error("Incorrect message type: " + message);
                    throw new IllegalArgumentException("Incorrect message type: " + message);
        }

        JOptionPane.showMessageDialog(null, message, title, jMessageType);
    }
}
