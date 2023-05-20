package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Finder {
    List<User> users = new ArrayList<>();

    public boolean findSenderReceiver(String sender, String receiver, List<User> userSockets) {
        // Extract the user with the provided username
        Optional<User> fndSender = userSockets.stream()
                .filter(user -> user.getUsername().equals(sender))
                .findFirst();

        Optional<User> fndReceiver = userSockets.stream()
                .filter(user -> user.getUsername().equals(receiver))
                .findFirst();

        if (fndSender.isPresent() && fndReceiver.isPresent()) {
            users.add(fndSender.get());
            users.add(fndReceiver.get());
        } else {
            System.out.println("User not found.");
            return false;
        }
        return true;
    }

    public User getSender() {
        return users.get(0);
    }

    public User getReceiver() {
        return users.get(1);
    }
}
