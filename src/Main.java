import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Server.*;

public class Main {
    private static final int PORT = 25; // Specify the port number to listen on

    public static void main(String[] args) {
        int i = 1;
        int auto = 1;
        List<User> userSockets = new ArrayList<>();
        Socket autoSocket = null;
        try {
            try (// Create a ServerSocket object and bind it to the specified port
                    ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server started. Waiting...");

                while (true) {
                    // Accept client connections
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);

                    if (auto % 2 == 0) {
                        // Create the user
                        User user = new User(autoSocket, clientSocket, "User" + i / 2);
                        System.out.println(user.getUsername());
                        // add to list
                        userSockets.add(user);
                    }
                    autoSocket = clientSocket;
                    auto++;
                    // Create a new thread for each connected client
                    Thread clientThread = new Thread(new CommandHandler(clientSocket, userSockets));
                    clientThread.start();

                    i++;
                }
            }
        } catch (IOException e) {
            // Check if the exception is due to a connection reset
            if (e.getMessage().equals("Connection reset")) {
                System.out.println("A client connection was reset.");
                // Handle the reset connection here
            } else {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}
