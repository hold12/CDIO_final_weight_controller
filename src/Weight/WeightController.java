package Weight;

import SimpleTCP.Client.*;
import java.io.IOException;

public class WeightController implements IWeightController {
    private ITCPClient tcp;

    public WeightController() {
        this.tcp = new TCPClient();
    }

    @Override
    public void connect(String host, int port) throws IOException {
        try {
            tcp.connect(host, port);
            String rec = tcp.receive(); // Receive initial "I4 A" after power-on
            System.out.println(rec); //TODO: Remove debug (see above)
        } catch (IOException e) {
            throw new IOException("Failed to connect to " + host + " on port " + port + ": " + e.getMessage());
        }
    }

    // S (Receive stable weight)
    @Override
    public String getCurrentWeight() throws IOException {
        // S S      0.900 kg
        String receivedMessage;
        try {
            tcp.send("S");
            receivedMessage = tcp.receive();
        } catch (IOException e) {
            throw new IOException("Failed to retrieve the current weight: " + e.getMessage());
        }
        receivedMessage = receivedMessage.replace(" kg", "");
        return receivedMessage.substring(9);
    }

    // T (Tare)
    @Override
    public String tareWeight() throws IOException {
        String newTare;
        try {
            tcp.send("T");
            newTare = tcp.receive();
            if (!newTare.startsWith("T S")) {
                throw new IOException(("Failed to tare the weight. Did not receive T S."));
            }
        } catch (IOException e) {
            throw new IOException("Failed to tare the weight: " + e.getMessage());
        }
        newTare = newTare.replace("T S", "");
        newTare = newTare.replace("\"", "");
        newTare = newTare.trim();
        return newTare;
    }

    @Override
    public void cancelCurrentOperation() throws IOException {
        try {
            tcp.send("@");
            String tcpRecCan = tcp.receive(); // Receive initial "I4 A" after reset
            System.out.println(tcpRecCan); //TODO: Remove debug
            if (!tcpRecCan.startsWith("I4 A")) {
                throw new IOException("Did not receive expected message after resetting.");
            }
        } catch (IOException e) {
            throw new IOException("Failed to cancel the current operation: " + e.getMessage());
        }
    }

    // D (Write in the primary display of the weight)
    @Override
    public void writeToPrimaryDisplay(String message) throws IOException {
        try {
            tcp.send("D \"" + message + "\"");
            if (!tcp.receive().equals("D A"))
                throw new IOException("Something went wrong when displaying message on the weight.");
        } catch (IOException e) {
            throw new IOException("Failed to send the message: " + e.getMessage());
        }
    }

    // DW (Clear the primary display)
    @Override
    public void clearPrimaryDisplay() throws IOException {
        try {
            tcp.send("DW");
            if (!tcp.receive().equals("DW A"))
                throw new IOException("Something went wrong when clearing main display.");
        } catch (IOException e) {
            throw new IOException("Failed to clear primary display: " + e.getMessage());
        }
    }

    @Override
    public void writeToSecondaryDisplay(String message) throws IOException, StringIndexOutOfBoundsException {
        if (message.length() > 30)
            throw new StringIndexOutOfBoundsException("The message must be within 30 characters long.");
        try {
            tcp.send("P111 \"" + message + "\"");
            if (!tcp.receive().equals("P111 A"))
                throw new IOException("Something went wrong when writing to the secondary display.");
        } catch (IOException e) {
            throw new IOException("Failed to write to the secondary dispaly: " + e.getMessage());
        }
    }

    @Override
    public String rm208(String primaryDisplay, String secondaryDisplay, KeyPadState keyPadState) throws IOException, StringIndexOutOfBoundsException {
        String userMessage;
        if (secondaryDisplay.length() > 30)
            throw new StringIndexOutOfBoundsException("The message to the secondary display must be within 30 characters long.");
        try {
            String tmp = "RM20 8 \"" + secondaryDisplay + "\" \"\" \"" + keyPadState + "\"";
            System.out.println("Start :" + tmp + ": slut");
            tcp.send(tmp);
            String tcpRecRm = tcp.receive();
            System.out.println(tcpRecRm);
            if (!tcpRecRm.equals("RM20 B"))
                throw new IOException("Something went wrong when receiving RM20 B message from the weight.");
            userMessage = tcp.receive();
            System.out.println(userMessage);
            userMessage = userMessage.replace("RM20 A \"", "");
            userMessage = userMessage.replace("\"", "");
        } catch (IOException e) {
            throw new IOException("Could not execute RM20 8: " + e.getMessage());
        }
        return userMessage;
    }

    @Override
    public void setNewGrossWeight(double newWeight) throws IOException {
        try {
            tcp.send("B " + newWeight);
            // TODO: Server does not send anything back, but should it not do so??
        } catch (IOException e) {
            throw new IOException("Could send set new gross value: " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        try {
            tcp.send("Q");
            tcp.close();
        } catch (IOException e) {
            throw new IOException("Could not close the connection: " + e.getMessage());
        }
    }
}
