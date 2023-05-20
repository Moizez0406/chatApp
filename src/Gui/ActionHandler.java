package Gui;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.awt.event.ActionListener;

public class ActionHandler {
    private final JTextArea myName;
    private static JTextArea chatArea = null;
    private final JTextArea userArea;
    private static JTextArea currentChat = null;
    private final JTextField messageField;
    private final Socket clientSocket;
    private String[] listUsers;

    public ActionHandler(JTextArea chatArea, JTextArea currentChat, JTextField messageField,
            JTextArea myName, JTextArea userArea, Socket autoclientSocket) throws IOException {
        ActionHandler.chatArea = chatArea;
        ActionHandler.currentChat = currentChat;
        this.messageField = messageField;
        this.myName = myName;
        this.userArea = userArea;
        this.clientSocket = new Socket(autoclientSocket.getInetAddress(), autoclientSocket.getPort());
        System.out.println("Connected to server");
        System.out.println(this.clientSocket);
    }

    public static void receiveMessages(Socket autoClientSocket) {
        Thread receiveThread = new Thread(() -> {
            UserActions.automaticallyReceive(autoClientSocket, chatArea, currentChat);
        });
        receiveThread.start();
    }

    public ActionListener sendButtonListener() {
        return e -> {
            String message = messageField.getText();
            String receiver = UserActions.getFromCurrentChat(currentChat);
            if (Objects.equals(message, "") || Objects.equals(receiver, "") ||
                    Objects.equals(receiver, "Invalid User!!")) {
                chatArea.append("Please write Something or Specify the chat!!!!\n");
            } else {
                chatArea.append("Me: " + message + "\n");
                messageField.setText("");
                UserActions.sendMessage(clientSocket, myName, receiver, message);
            }
        };
    }

    public ActionListener getUsersListener() {
        return event -> {
            listUsers = UserActions.getUserList(clientSocket, "getUsers").split("\\|");
            JPanel newUserButtonPanel = Gui.Buttons.createButtonPanel();
            JScrollPane userButtonScrollPane = new JScrollPane(newUserButtonPanel);
            UserActions.updateArea(userArea);
            Gui.Buttons.createUserButtons(listUsers, newUserButtonPanel, this);
            userArea.setLayout(new BorderLayout());
            userArea.add(userButtonScrollPane, BorderLayout.CENTER);
            userButtonScrollPane.revalidate();
            userButtonScrollPane.repaint();
        };
    }

    public void setChat(String user) {
        String currentUser = currentChat.getText();
        if (!currentUser.equals("Chatting with: " + user)) {
            chatArea.setText("");
            currentChat.setText("");
            currentChat.setText("Chatting with: " + user);
        }
    }

    public ActionListener GetHistoryListener() {
        return e -> {
            String username = myName.getText();
            String receiver = UserActions.getFromCurrentChat(currentChat);
            UserActions.getHistory(clientSocket, username, receiver);
        };
    }

    public ActionListener setNameListener() {
        return e -> {
            String username = messageField.getText();
            if (UserActions.checkToseeIfUserExists(listUsers, username)) {
                myName.setText("");
                currentChat.setText("Already taken.");
            } else {
                myName.setText(username);
                UserActions.setName(clientSocket, username);
            }
        };
    }
}