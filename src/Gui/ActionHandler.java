package Gui;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.net.Socket;
import java.util.Objects;
import java.awt.event.ActionListener;

public class ActionHandler {
    private Socket clientSocket = null;
    private Socket autoClientSocket;
    private final JTextArea chatArea;
    private final JTextArea userArea;
    private final JTextArea currentChat;
    private final JTextField messageField;
    private final JTextArea myName;
    private final String SERVER_IP; // Server IP address
    private final int SERVER_PORT; // Server port number
    private String[] listUsers;
    public ActionHandler(JTextArea chatArea, JTextArea currentChat, JTextField messageField,
                         JTextArea myName, JTextArea userArea, String SERVER_IP, int SERVER_PORT) {
        this.chatArea = chatArea;
        this.currentChat = currentChat;
        this.messageField = messageField;
        this.myName = myName;
        this.userArea = userArea;
        this.SERVER_IP = SERVER_IP;
        this.SERVER_PORT = SERVER_PORT;
    }
    public void connectAutoResponse() {
        try {
            autoClientSocket = new Socket(SERVER_IP, SERVER_PORT);
            receiveMessages();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
    public void receiveMessages() {
        Thread receiveThread = new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(autoClientSocket.getInputStream()));
                while (true) {
                    String message = reader.readLine();
                    if (message != null) {
                        int startIndex = currentChat.getText().indexOf(":") + 1;
                        String userResponse = currentChat.getText().substring(startIndex).trim();
                        // Display the received message in the chat area
                        SwingUtilities.invokeLater(() -> chatArea.append(userResponse + ":  " + message + "\n"));
                    }
                }
            } catch (IOException error) {
                error.printStackTrace();
            }
        });
        receiveThread.start();
    }
    public void connect() throws IOException {
        clientSocket = new Socket(SERVER_IP, SERVER_PORT);
        System.out.println("Connected to server.");
        System.out.println(clientSocket);

    }
    public ActionListener sendButtonListener() {
        return e -> {
            String message = messageField.getText();
            int startIndex = currentChat.getText().indexOf(":") + 1;
            String receiver = currentChat.getText().substring(startIndex).trim();
            if (Objects.equals(message, "") || Objects.equals(receiver, "") ||
                    Objects.equals(receiver, "Invalid User!!")) {
                chatArea.append("Please write Something or Specify the chat!!!!\n");
            } else {
                chatArea.append("Me: " + message + "\n");
                messageField.setText("");
                try {
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    writer.println("SendMsg|1|" + myName.getText() + "|" + receiver + "|" + message);
                    System.out.println("SendMsg|1|" + myName.getText() + "|" + receiver + "|" + message);
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        };
    }
    public ActionListener getUsersListener() {
        return event -> {
            String message = "getUsers";
            String tmpListUsers;
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println(message);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                tmpListUsers = reader.readLine();
                System.out.println(tmpListUsers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            listUsers = tmpListUsers.split("\\|");
            userArea.removeAll();
            JPanel newUserButtonPanel = new JPanel();
            newUserButtonPanel.setLayout(new BoxLayout(newUserButtonPanel, BoxLayout.PAGE_AXIS));
            newUserButtonPanel.setBackground(new Color(40, 40, 40));

            userArea.setText("");
            userArea.setText("User status: \n\n");
            for (String str : listUsers) {
                System.out.println(str);
                JButton userButton = Buttons.createButton(str,e -> setChat(str));
                newUserButtonPanel.add(userButton);
                newUserButtonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            JScrollPane userButtonScrollPane = new JScrollPane(newUserButtonPanel);

            userArea.setLayout(new BorderLayout());
            userArea.add(userButtonScrollPane, BorderLayout.CENTER);

            userButtonScrollPane.revalidate();
            userButtonScrollPane.repaint();
        };
    }
    public void setChat(String user) {
        String currentUser = currentChat.getText();
        System.out.println(user);
        System.out.println(currentUser);
        if(!currentUser.equals("Chatting with: "+ user)) {
            chatArea.setText("");
            currentChat.setText("");
            currentChat.setText("Chatting with: " + user);
        }
    }
    public ActionListener GetHistoryListener() {
        return e -> {
            String username = myName.getText();
            int startIndex = currentChat.getText().indexOf(":") + 1;
            String receiver = currentChat.getText().substring(startIndex).trim();
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println("GetHistory|"+username+"|"+receiver);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String history = reader.readLine();
                System.out.println(history);
            } catch (IOException error) {
                error.printStackTrace();
            }
        };
    }
    public ActionListener setNameListener() {
        return e -> {
            String username = messageField.getText();
            for (String str : listUsers) {
                if (str.equals(username)) {
                    myName.setText("");
                    currentChat.setText("Already taken.");
                } else {
                    myName.setText(username);
                }
            }
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println("ChangeName|" + clientSocket.getLocalPort() + "|" + username);
            } catch (IOException error) {
                error.printStackTrace();
            }
        };
    }
}
