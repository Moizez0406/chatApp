package Server;

import java.io.IOException;
import java.io.PrintWriter;

public class Message {
    private final User receiver;
    private final User sender;
    private final String names = "?";
    private final String rnames;

    private String content;
    private PrintWriter writer;
    private PrintWriter autoWriter;

    public Message(User sender, User receiver, String content) {
        this.receiver = receiver;
        this.sender = sender;
        this.rnames = receiver.getUsername() + sender.getUsername();
        this.content = content;
    }

    public void sendMsg() {
        try {
            if (receiver.isActive() == false) {
                writer = new PrintWriter(sender.getSocket().getOutputStream(), true);
                writer.println("User is not active");
                System.out.println("User is not active");
                return;
            } else {
                writer = new PrintWriter(receiver.getSocket().getOutputStream(), true);
                autoWriter = new PrintWriter(receiver.getAutoSocket().getOutputStream(), true);
                writer.println(content);
                autoWriter.println(content);
                System.out.println("Message sent");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNames() {
        return names;
    }

    public String getRNames() {
        return rnames;
    }

}
