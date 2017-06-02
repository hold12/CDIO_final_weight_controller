package Weight;

import Lang.Lang;
import java.io.IOException;
import java.util.Scanner;

public class WeightGUI implements IWeightGUI {
    private IWeightController weight;
    private boolean connected = false;

    public WeightGUI(IWeightController weight) {
        this.weight = weight;
    }

    public boolean isConnected() { return connected; }

    @Override
    public boolean connect(String host, int port) {
        if (connected) return true;
        System.out.println(Lang.msg("connecting") + " " + host + ":" + port + "...");

        try {
            weight.connect(host, port);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionConnect"));
            System.out.println(Lang.msg("exiting"));
            return false;
        }

        System.out.println(Lang.msg("connectionEstablished"));
        connected = true;
        return true;
    }

    @Override
    public void getCurrentWeight() {
        String currentWeight;
        try {
            currentWeight = weight.getCurrentWeight();
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionGetWeight") + " " + Lang.msg("exceptionKeyState"));
            return;
        }
        System.out.println(Lang.msg("currentWeight") + " " + currentWeight);
    }

    @Override
    public void tareWeight() {
        try {
            weight.tareWeight();
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionTare") + " " + Lang.msg("exceptionKeyState") + e.getMessage());
        }
        System.out.println(Lang.msg("tared"));
    }

    @Override
    public void writeToPrimaryDisplay() {
        Scanner scn = new Scanner(System.in);
        String message;
        while(true) {
            System.out.print(Lang.msg("typeMsg") + ": ");
            message = scn.nextLine();
            if (message.length() <= 0 || message.length() > 7)
                System.err.println(Lang.msg("errStr07"));
            else
                break;
        }

        try {
            weight.writeToPrimaryDisplay(message);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionMessageDelivery"));
            return;
        }
        finally{
        	scn.close();
        }
        System.out.println(Lang.msg("msgDelivered"));
    }

    @Override
    public void writeToSecondaryDisplay() {
        Scanner scn = new Scanner(System.in);
        String message;
        while(true) {
            System.out.print("Type message: ");
            message = scn.nextLine();
            if (message.length() <= 0 || message.length() > 30)
                System.err.println(Lang.msg("errStr030"));
            else
                break;
        }

        try {
            weight.writeToSecondaryDisplay(message);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionMessageDelivery"));
            return;
        }
        finally{
        	scn.close();
        }
        System.out.println(Lang.msg("msgDelivered"));
    }

    @Override
    public void clearPrimaryDisplay() {
        try {
            weight.clearPrimaryDisplay();
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionClear"));
            return;
        }
        System.out.println(Lang.msg("displayCleared"));
    }

    @Override
    public void rm208() {
        String primaryDisplay;
        String secondaryDisplay;
        IWeightController.KeyPadState keyPadState;
        Scanner scn = new Scanner(System.in);
        while(true) {
            System.out.print(Lang.msg("msgPriDisplay") + ": ");
            primaryDisplay = scn.nextLine();
            if (primaryDisplay.length() <= 0 || primaryDisplay.length() > 7)
                System.err.println(Lang.msg("errStr07"));
            else
                break;
        }
        while(true) {
            System.out.print(Lang.msg("msgSecDisplay") + ": ");
            secondaryDisplay = scn.nextLine();
            if (secondaryDisplay.length() <= 0 || secondaryDisplay.length() > 30)
                System.err.println(Lang.msg("errStr030"));
            else
                break;
        }

        int keyPadChoice;
        while(true) {
            System.out.println(Lang.msg("selectKeyPadType") + ": ");
            System.out.println("[1] " + Lang.msg("keyPadLower"));
            System.out.println("[2] " + Lang.msg("keyPadUpper"));
            System.out.println("[3] " + Lang.msg("keyPadNumeric"));
            String choiceStr = scn.nextLine();
            try { keyPadChoice = Integer.parseInt(choiceStr); break;}
            catch(Exception e) { System.err.println(Lang.msg("errSTOI") + " " + Lang.msg("between") + " 1 " + Lang.msg("and") + " 3!"); }
        }


        switch (keyPadChoice) {
            case 1: keyPadState = IWeightController.KeyPadState.LOWER_CHARS; break;
            case 2: keyPadState = IWeightController.KeyPadState.UPPER_CHARS; break;
            case 3: keyPadState = IWeightController.KeyPadState.NUMERIC; break;
            default: keyPadState = null;
        }

        String returnedMsg;
        try {
            returnedMsg = weight.rm208(primaryDisplay, secondaryDisplay, keyPadState);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
            return;
        }
        finally{
        	scn.close();
        }
        System.out.println(Lang.msg("userEntered") + ": " + returnedMsg);
    }

    @Override
    public void setNewGrossWeight() {
        double newWeight;
        Scanner scn = new Scanner(System.in);

        while(true) {
            System.out.println(Lang.msg("selectKeyPadType") + ": ");
            String choiceStr = scn.nextLine();
            try { newWeight = Integer.parseInt(choiceStr); break;}
            catch(Exception e) { System.err.println(Lang.msg("errSTOD") + "!"); }
        }

        try {
            weight.setNewGrossWeight(newWeight);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionSetWeight"));
        }
        finally{
        	scn.close();
        }
    }

    @Override
    public void close() {
        try {
            weight.close();
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionClose"));
        }
        System.out.println(Lang.msg("exiting"));
        System.exit(0);
    }
}
