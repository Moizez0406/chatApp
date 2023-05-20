package Server;

import java.net.SocketException;
import java.util.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CommandHandler implements Runnable {
    private final Socket clientSocket;
    List<User> userSockets;
    List<History> historyList = new ArrayList<>();
    public final String session;
    private PrintWriter writer;

    public CommandHandler(Socket clientSocket, List<User> userSockets) {
        this.clientSocket = clientSocket;
        this.userSockets = userSockets;
        this.session = "new one";
    }

    public void process(String[] command) {
        switch (command[0]) {
            case "SendMsg":
                Finder myFind = new Finder();
                if (myFind.findSenderReceiver(command[2], command[3], userSockets)) {
                    Message message = new Message(myFind.getSender(), myFind.getReceiver(), command[4]);
                    message.sendMsg();
                }

            case "getUsers":
                StringBuilder response = new StringBuilder();
                for (User user : userSockets) {
                    response.append(user.getUsername()).append("|");
                }

                writer.println(response);
                System.out.println(response);
                break;

            case "ChangeName":
                int givenPort = Integer.parseInt(command[1]);
                for (User user : userSockets) {
                    if (user.getSocket().getPort() == givenPort) {
                        user.setUsername(command[2]);
                    }
                }
                break;

            case "GetHistory":
                // GetHistory|User|User
                break;

            case "SaveChat":
                System.out.println("Save ...");
                break;

            default:
                System.out.println("Invalid Command");
        }
    }

    @Override
    public void run() {
        historyList.add(new History(new User("null"), new User(null)));
        try {
            // Initialize the input and output streams for the client socket
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Start processing client messages
            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println(clientSocket);
                // Process the client message as needed
                System.out.println("Received from client: " + clientMessage);
                String[] splitStrings = clientMessage.split("\\|");

                // Send a response back to the client
                process(splitStrings);
            }
        } catch (SocketException e) {
            // logger.error("Client connection reset: " + e.getMessage() + " --> " +
            // clientSocket);
            // Handle the reset connection here
        } catch (IOException e) {
            // logger.error("An error occurred in the CommandHandler: " + e.getMessage());
        } finally {
            // ... other code ...

            // logger.info("Client disconnected: " + clientSocket);
        }
    }
}
