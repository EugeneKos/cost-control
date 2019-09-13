package org.eugene.cost.exeption;

import javax.swing.*;

public class ApplicationError extends RuntimeException {
    private static final long serialVersionUID = 3529662667558935458L;

    public ApplicationError(String message) {
        showOptionPane(message);
        closeApplication();
    }

    public ApplicationError(String message, Throwable cause) {
        showOptionPane(message + " cause: "+cause.getMessage());
        closeApplication();
    }

    public ApplicationError(Throwable cause) {
        showOptionPane(cause.getMessage());
        closeApplication();
    }

    private void showOptionPane(String messageError){
        JOptionPane.showMessageDialog(null,
                messageError, "Application Error", JOptionPane.ERROR_MESSAGE);
    }

    private void closeApplication(){
        System.exit(-1);
    }
}
