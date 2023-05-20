package Server;

import java.io.IOException;
import java.io.PrintWriter;

public class Message {
    private final User receiver;
    private final String names;
    private final String rnames;

    private String content;
    private PrintWriter writer;
    private PrintWriter autoWriter;

    public Message(User sender, User receiver, String content) {
        this.receiver = receiver;
        this.names = sender.getUsername() + receiver.getUsername();
        this.rnames = receiver.getUsername() + sender.getUsername();
        this.content = content;
    }

    public void sendMsg() {
        try {
            writer = new PrintWriter(receiver.getSocket().getOutputStream(), true);
            autoWriter = new PrintWriter(receiver.getAutoSocket().getOutputStream(), true);
            writer.println(content);
            autoWriter.println(content);

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
