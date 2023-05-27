package Server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Finder {
    List<User> users = new ArrayList<>();

    public boolean findSenderReceiver(String sender, String receiver, List<User> userSockets) {
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

    public boolean findUser(String username, List<User> userlList)
            throws UnknownHostException, IOException {
        for (User user : userlList) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public User getFindUser(String username, List<User> userlList)
            throws UnknownHostException, IOException {
        for (User user : userlList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void loadUser(String selfusername, String username, List<User> userlList)
            throws UnknownHostException, IOException {
        for (User selfuser : userlList) {
            if (selfuser.getUsername().equals(selfusername)) {
                selfuser.setUsername(username);
                // selfuser.cloneUser(getFindUser(username, userlList));
                System.out.println("User " + selfusername + " is now " + username);
                // userlList.removeIf(user -> user.getUsername().equals(username) &&
                // !user.isActive());
            }
        }
    }

    public User getSender() {
        return users.get(0);
    }

    public User getReceiver() {
        return users.get(1);
    }
}
