package Gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class UserActions {
    public static String getFromCurrentChat(JTextArea currentChat) {
        int startIndex = currentChat.getText().indexOf(":") + 1;
        String receiver = currentChat.getText().substring(startIndex).trim();
        return receiver;
    }

    public static boolean checkToseeIfUserExists(String[] listUsers, String user) {
        for (String str : listUsers) {
            if (str.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public static void setName(Socket socket, String name) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("ChangeName|" + socket.getLocalPort() + "|" + name);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void sendMessage(Socket socket,JTextArea myName, String receiver, String message) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("SendMsg|1|" + myName.getText() + "|" + receiver + "|" + message);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void automaticallyReceive(Socket autoClientSocket, JTextArea chatArea, JTextArea currentChat){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(autoClientSocket.getInputStream()));
            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    String fromUser = UserActions.getFromCurrentChat(currentChat);
                    SwingUtilities.invokeLater(() -> chatArea.append(fromUser + ":  " + message + "\n"));
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static String getUserList(Socket clientSocket, String message){
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(message);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getHistory(Socket clientSocket, String username, String receiver){
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("GetHistory|"+username+"|"+receiver);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String history = reader.readLine();
            System.out.println(history);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void updateArea(JTextArea userArea) {
        userArea.removeAll();
        userArea.setText("");
        userArea.setText("User status: \n\n");
    }
}

