import Server.*;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int PORT = 1444; // Specify the port number to listen on
    private static final String FILE_PATH = "D:/Programacion/Java/chatApp/resources/Users.txt";

    public static void main(String[] args) {
        int i = 1;
        int auto = 1;
        List<User> userSockets = new ArrayList<>();
        Socket autoSocket = null;

        File file = new File(FILE_PATH);
        registerShutdownHook(file);

        try {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server started. Waiting...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);

                    if (auto % 2 == 0) {
                        User user = new User(autoSocket, clientSocket, "User" + i / 2);
                        System.out.println(user.getUsername());
                        userSockets.add(user);
                    }
                    autoSocket = clientSocket;
                    auto++;

                    Thread clientThread = new Thread(new CommandHandler(clientSocket, userSockets));
                    clientThread.start();

                    i++;
                }
            }
        } catch (IOException e) {
            if (e.getMessage().equals("Connection reset")) {
                System.out.println("A client connection was reset.");
            } else {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void registerShutdownHook(final File file) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("File deleted on shutdown.");
                    } else {
                        System.out.println("Failed to delete the file on shutdown.");
                    }
                }
            }
        });
    }
}
