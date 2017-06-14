package ctrl;

import SimpleTCP.Client.*;

import java.io.IOException;
import java.util.LinkedList;

public class WeightController implements IWeightController {
    private ITCPClient tcp;

    public WeightController() {
        this.tcp = new TCPClient();
    }

    @Override
    public void connect(String host, int port) throws IOException {
        try {
            tcp.connect(host, port);
        } catch (IOException e) {
            throw new IOException("Failed to connect to " + host + " on port " + port + ": " + e.getMessage());
        }

        try {
            receiveMessage(); // Receive initial "I4 A" after power-on
        } catch (IOException e) {
            throw new IOException("Failed to receive initial I4 A message: " + e.getMessage());
        }
    }

    private void sendMessage(String msg) throws IOException {
        tcp.send(msg);
        System.out.println("Sent:\t\t" + msg);
    }

    public String receiveMessage() throws IOException {
        String rcv = tcp.receive();
        System.out.println("Received:\t" + rcv);
        return rcv;
    }

    // RM36 (Prepare softbuttons)
    public String rm36(LinkedList<String> buttons) throws IOException {
        String receivedMessage;
        StringBuilder sendMessage = new StringBuilder("RM36 " + buttons.size());
        
        for (String button : buttons){
        	sendMessage.append(" \"").append(button).append("\"");
        }
        
        try {
            sendMessage(sendMessage.toString());
            receivedMessage = receiveMessage();
        } catch (IOException e) {
            throw new IOException("Failed to prepare softbuttons: " + e.getMessage());
        }
        return receivedMessage;
    }

    // RM38 (Show softbuttons)
    public String rm38(int noOfButtons) throws IOException {
        String receivedMessage;
        try {
            sendMessage("RM38 " + noOfButtons);
            receivedMessage = receiveMessage();
        } catch (IOException e) {
            throw new IOException("Failed to show softbuttons: " + e.getMessage());
        }
        return receivedMessage;
    }
    
    // DW (Show weight display)
    public String showWeightDisplay() throws IOException {
        String receivedMessage;
        try {
            sendMessage("DW");
            receivedMessage = receiveMessage();
        } catch (IOException e) {
            throw new IOException("Failed to change display to weight display: " + e.getMessage());
        }
        return receivedMessage;
    }
    
    // S (Receive stable weight)
    @Override
    public String getCurrentWeight() throws IOException {
        // S S      0.900 kg
        String receivedMessage;
        try {
            sendMessage("S");
            receivedMessage = receiveMessage();
        } catch (IOException e) {
            throw new IOException("Failed to retrieve the current weight: " + e.getMessage());
        }
        receivedMessage = receivedMessage.replace(" kg", "");
        receivedMessage = receivedMessage.substring(7);
        return receivedMessage.trim();
    }

    // T (Tare)
    @Override
    public String tareWeight() throws IOException {
        String newTare;
        try {
            sendMessage("T");
            newTare = receiveMessage();
            if (!newTare.startsWith("T S")) {
                throw new IOException(("Failed to tare the weight. Did not receive T S."));
            }
        } catch (IOException e) {
            throw new IOException("Failed to tare the weight: " + e.getMessage());
        }
        newTare = newTare.replace(" kg", "");
        return newTare.substring(9);
    }

    @Override
    public void cancelCurrentOperation() throws IOException {
        try {
            sendMessage("@");
            String tcpRecCan = receiveMessage(); // Receive initial "I4 A" after reset
            if (!tcpRecCan.startsWith("I4 A")) {
                throw new IOException("Did not receive expected message after resetting.");
            }
        } catch (IOException e) {
            throw new IOException("Failed to cancel the current operation: " + e.getMessage());
        }
    }

    // D (Write in the primary display of the weight)
    @Override
    public void writeToPrimaryDisplay(String message) throws IOException, StringIndexOutOfBoundsException {
        if (message.length() > 7)
            throw new StringIndexOutOfBoundsException("The message to the primary display must be within 7 characters long.");
        try {
            sendMessage("D \"" + message + "\"");
            if (!receiveMessage().equals("D A"))
                throw new IOException("Something went wrong while displaying message on the weight.");
        } catch (IOException e) {
            throw new IOException("Failed to send the message: " + e.getMessage());
        }
    }

    // DW (Clear the primary display)
    @Override
    public void clearPrimaryDisplay() throws IOException {
        try {
            sendMessage("DW");
            if (!receiveMessage().equals("DW A"))
                throw new IOException("Something went wrong while clearing main display.");
        } catch (IOException e) {
            throw new IOException("Failed to clear primary display: " + e.getMessage());
        }
    }

    @Override
    public void writeToSecondaryDisplay(String message) throws IOException, StringIndexOutOfBoundsException {
        if (message.length() > 30)
            throw new StringIndexOutOfBoundsException("The message to the secondary display must be within 30 characters long.");
        try {
            sendMessage("P111 \"" + message + "\"");
            if (!receiveMessage().equals("P111 A"))
                throw new IOException("Something went wrong while writing to the secondary display.");
        } catch (IOException e) {
            throw new IOException("Failed to write to the secondary display: " + e.getMessage());
        }
    }

    @Override
    public String rm208(String primaryDisplay, String secondaryDisplay, KeyPadState keyPadState) throws IOException, StringIndexOutOfBoundsException {
        String userMessage;
        if (primaryDisplay.length() > 7)
            throw new StringIndexOutOfBoundsException("The message to the primary display must be within 7 characters long.");
        if (secondaryDisplay.length() > 30)
            throw new StringIndexOutOfBoundsException("The message to the secondary display must be within 30 characters long.");
        try {
            sendMessage("RM20 8 \"" + secondaryDisplay + "\" \"" + primaryDisplay + "\" \"" + keyPadState + "\"");
            String tcpRecRm = receiveMessage();
            if (!tcpRecRm.equals("RM20 B"))
                throw new IOException("Something went wrong while receiving RM20 B message from the weight.");
            userMessage = receiveMessage();
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
            sendMessage("B " + newWeight);
            // TODO: Server does not send anything back, but should it not do so??
        } catch (IOException e) {
            throw new IOException("Could send set new gross value: " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        try {
            sendMessage("Q");
            tcp.close();
        } catch (IOException e) {
            throw new IOException("Could not close the connection: " + e.getMessage());
        }
    }
}
