package Gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Buttons {
    public static JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setBackground(new Color(51, 51, 51));
        button.setForeground(new Color(230, 230, 230));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }
}