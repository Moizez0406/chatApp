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

    public static JPanel createButtonPanel() {
        JPanel newUserButtonPanel = new JPanel();
        newUserButtonPanel.setLayout(new BoxLayout(newUserButtonPanel, BoxLayout.PAGE_AXIS));
        newUserButtonPanel.setBackground(new Color(40, 40, 40));
        return newUserButtonPanel;
    }

    public static void createUserButtons(String[] listUsers, JPanel newUserButtonPanel, ActionHandler actionHandler) {
        for (String str : listUsers) {
            JButton userButton = Buttons.createButton(str, e -> actionHandler.setChat(str));
            newUserButtonPanel.add(userButton);
            newUserButtonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }
}