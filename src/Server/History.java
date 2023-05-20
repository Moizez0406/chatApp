package Server;

import java.io.IOException;
import java.io.PrintWriter;

public class History {
    private String infoR = "";
    private String infoS = "";
    private final User SenderHst;
    private final User ReceiverHst;
    private final String names;
    private final String rnames;

    public History(User SenderHst, User ReceiverHst) {
        this.SenderHst = SenderHst;
        this.ReceiverHst = ReceiverHst;
        this.names = SenderHst.getUsername() + ReceiverHst.getUsername();
        this.rnames = ReceiverHst.getUsername() + SenderHst.getUsername();

    }

    public String getNames() {
        return names;
    }

    public String getRNames() {
        return rnames;
    }

    public void saveInfoS(String info) {
        this.infoS = this.infoS + info + "|";
    }

    public void saveInfoR(String info) {
        this.infoR = this.infoR + info + "|";
    }

    public void getHistory(String Username) {
        String infoToSend = null;
        String iterableInfoS[] = infoS.split("\\|");
        String iterableInfoR[] = infoR.split("\\|");
        if (SenderHst.getUsername().equals(Username)) {
            for (int i = 0; i < iterableInfoR.length; i++) {
                infoToSend = infoToSend + iterableInfoS[i] + iterableInfoR[i];
            }
        } else {
            for (int i = 0; i < iterableInfoR.length; i++) {
                infoToSend = infoToSend + iterableInfoR[i] + iterableInfoS[i];
            }
            try {
                PrintWriter writer = new PrintWriter(SenderHst.getSocket().getOutputStream(), true);
                writer.println(infoS);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getHistoryS() {
        try {
            PrintWriter writer = new PrintWriter(SenderHst.getSocket().getOutputStream(), true);
            writer.println(infoS);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHistoryR() {
        try {
            PrintWriter writer = new PrintWriter(ReceiverHst.getSocket().getOutputStream(), true);
            writer.println(infoR);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
