package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
    private Socket socket;
    private Socket autoSocket;
    private String history = "";
    private String username;
    private boolean active = true;

    public User(Socket autoSocket, Socket socket, String username) {
        this.autoSocket = autoSocket;
        this.socket = socket;
        this.username = username;
    }

    public User(String username) {
        this.autoSocket = null;
        this.socket = null;
        this.username = username;
    }

    public Socket getAutoSocket() {
        return this.autoSocket;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public String getUsername() {
        return this.username;
    }

    public void setIsActive(boolean b) {
        this.active = b;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSokcet(Socket socket) {
        this.socket = socket;
    }

    public void setAutoSokcet(Socket socket) {
        this.autoSocket = socket;
    }

    public void saveHistory(String history) {
        this.history = this.history + history + "|";
    }

    public void getHistory() {
        try {
            PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true);
            writer.println(history);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}