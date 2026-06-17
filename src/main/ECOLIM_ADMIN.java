package main;

import vista.LoginAdmin;

public class ECOLIM_ADMIN {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new LoginAdmin().setVisible(true);
        });
    }
}