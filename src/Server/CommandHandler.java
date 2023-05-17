package Server;
import java.net.SocketException;
import java.util.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CommandHandler implements Runnable{
    private final Socket clientSocket;
    List<User> userSockets;
    List<History> historyList = new ArrayList<>();
    public final String session;
    private PrintWriter writer;
    
    public CommandHandler(Socket clientSocket,List<User> userSockets) {
        this.clientSocket = clientSocket;
        this.userSockets = userSockets;
        this.session = "new one";
    }
    
    public void process (String[] command){
        switch (command[0]) {
            case "SendMsg":
                String SearchSender = command[2];
                String SearchReceiver = command[3];
                // Extract the user with the provided username
                Optional<User> fndSender = userSockets.stream()
                        .filter(user -> user.getUsername().equals(SearchSender))
                        .findFirst();

                Optional<User> fndReceiver = userSockets.stream()
                        .filter(user -> user.getUsername().equals(SearchReceiver))
                        .findFirst();

                if (fndSender.isPresent() && fndReceiver.isPresent()) {
                    Message message = new Message(fndSender.get(),fndReceiver.get(),command[4]);
                    boolean check = false;
                    int index = 0;
                    for(History in:historyList){
                        if (Objects.equals(message.getNames(), in.getNames()) ||
                                Objects.equals(message.getRNames(), in.getRNames())) {
                            check = true;
                            index++;
                            break;
                        }
                    }

                    if(check){
                        historyList.get(index).saveInfoS("me: "+command[4]);
                        historyList.get(index).saveInfoR(fndSender.get().getUsername()+": "+command[4]);
                    }else{
                        historyList.add(new History(fndSender.get(),fndReceiver.get()));
                        historyList.get(historyList.size()-1).saveInfoS("me: "+command[4]);
                        historyList.get(historyList.size()-1).saveInfoR(fndSender.get().getUsername()+": "+command[4]);
                    }
                    message.sendMsg();
                    break;
                } else {
                    System.out.println("User not found.");
                }

            case "getUsers":
                StringBuilder response = new StringBuilder();
                for(User user: userSockets){
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
                //GetHistory|User|User
                String userNames = command[1]+command[2];
                Optional<History> history = historyList.stream()
                        .filter(hst -> hst.getNames().equals(userNames) || hst.getRNames().equals(userNames))
                        .findFirst();

                if (history.isPresent()) {
                    history.get().getHistory(command[1]);
                    break;
                } else {
                    System.out.println("User not found.");
                }
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
            System.out.println("Client connection reset: " + e.getMessage() +" --> " +  clientSocket);
            // Handle the reset connection here
        } catch (IOException e) {
            System.out.println("An error occurred in the CommandHandler: " + e.getMessage());
        } finally {
            // Perform any necessary cleanup or handling after the loop ends
            Iterator<User> iterator = userSockets.iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if (clientSocket.equals(user.getSocket())) {
                    iterator.remove(); // Remove the current user from the list
                }
            }
        }
    }
}
