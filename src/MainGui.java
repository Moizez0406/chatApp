import Gui.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class MainGui extends JFrame {
    private static final String SERVER_IP = "192.168.1.12"; // Server IP address
    private static final int SERVER_PORT = 25; // Server port number

    public MainGui() throws IOException {
        super("Chat Server");
        setupGUI();
    }

    private void setupGUI() throws IOException {
        // Dark theme
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("nimbusDisabledText", new Color(50, 49, 49));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusLightBackground", new Color(38, 38, 38));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("text", new Color(230, 230, 230));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Chat area
        JTextArea chatArea = Buttons.createAreas(20, 40, false);
        JTextArea userArea = Buttons.createAreas(20, 20, false, "       NOTHING TO SHOW \n");
        JTextArea currentChat = Buttons.createAreas(1, 20, false);
        JTextArea myName = Buttons.createAreas(1, 10, false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // Fields
        JTextField messageField = new JTextField(50);
        messageField.setBackground(new Color(40, 40, 40));
        messageField.setForeground(new Color(230, 230, 230));

        // Button
        Socket autoclientSocket = new Socket(SERVER_IP, SERVER_PORT);
        ActionHandler.receiveMessages(autoclientSocket);
        ActionHandler actionHandler = new ActionHandler(chatArea, currentChat, messageField,
                myName, userArea, autoclientSocket);
        JButton sendButton = Gui.Buttons.createButton("Send", actionHandler.sendButtonListener());
        JButton getUsers = Gui.Buttons.createButton("Get Users", actionHandler.getUsersListener());
        JButton getHistory = Gui.Buttons.createButton("get History", actionHandler.GetHistoryListener());
        JButton setName = Gui.Buttons.createButton("set Name", actionHandler.setNameListener());
        // Add to the gui
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Panel for the buttons
        // biuld the panel
        Buttons.builddButton(buttonPanel, getUsers, setName, getHistory, currentChat, myName);

        JPanel bottomPanel = new JPanel();
        JPanel topPanel = new JPanel();
        // set the layout
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.WEST);
        bottomPanel.add(sendButton, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        bottomPanel.setBackground(new Color(18, 30, 49));
        topPanel.setLayout(new BorderLayout());
        topPanel.add(userArea, BorderLayout.CENTER);
        c.add(bottomPanel, BorderLayout.SOUTH);
        c.add(topPanel, BorderLayout.WEST);

        // Properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new MainGui();
    }
}