package ctrl;

import java.io.IOException;
import java.util.LinkedList;

public interface IWeightController {
    enum KeyPadState {
        LOWER_CHARS,UPPER_CHARS, NUMERIC;

        @Override
        public String toString() {
            switch (this) {
                case UPPER_CHARS: return "&1";
                case LOWER_CHARS: return "&2";
                case NUMERIC:     return "&3";
                default:          return "&3";
            }
        }
    }

    void connect(String host, int port) throws IOException;
    String receiveMessage() throws IOException;
    String showWeightDisplay() throws IOException;
    String rm38(int noOfButtons) throws IOException;
    String rm36(LinkedList<String> buttons) throws IOException;
    String getCurrentWeight() throws IOException;
    String tareWeight() throws IOException;
    void writeToPrimaryDisplay(String message) throws IOException;
    void writeToSecondaryDisplay(String message) throws IOException, StringIndexOutOfBoundsException;
    void clearPrimaryDisplay() throws IOException;
    String rm208(String primaryDisplay, String secondaryDisplay, KeyPadState keyPadState) throws IOException, StringIndexOutOfBoundsException;
    void setNewGrossWeight(double newWeight) throws IOException;
    void cancelCurrentOperation() throws IOException;
    void close() throws IOException;
}
