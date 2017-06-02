package Weight;

public interface IWeightGUI {
    boolean isConnected();
    boolean connect(String host, int port);
    void getCurrentWeight();
    void tareWeight();
    void writeToPrimaryDisplay();
    void writeToSecondaryDisplay();
    void clearPrimaryDisplay();
    void rm208();
    //    void changeButtonState() throws IOException;
    void setNewGrossWeight();
    void close();
}
