import Lang.Lang;
import Weight.IWeightController;
import Weight.WeightController;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private IWeightController weightClient;
    private String input;
    private Scanner scn;
    private boolean connected;

    private Main() {
        weightClient = new WeightController();
        scn = new Scanner(System.in);
    }

    public static void main(String[] args) {
        String[] locale = new String[2];
        if (args.length == 0) { locale[0] = "en";    locale[1] = "UK"; }
        else                  { locale[0] = args[0]; locale[1] = args[1]; }

        Lang.setLanguage(locale);
        Main main = new Main();

        main.startProgram();
    }

    private void startProgram() {
        while(true) {
            System.out.print("start> ");
            input = scn.nextLine().toLowerCase();
            if (input.startsWith("connect")) {
                if (!connect()) System.exit(1);
                break;
            } else {
                System.out.println(Lang.msg("errCmdNotFound"));
            }
        }

        menu();
    }

    private void menu() {
        System.out.print("menu> ");
        input = scn.nextLine().toLowerCase();
        if (input.startsWith("batch")) {
            batch("12", "1234");
        }
        if (input.startsWith("close") || input.startsWith("exit") || input.startsWith("quit")) {
            return;
        }

        menu();
    }

    private void batch(String userId, String batchId) {
        try {
            weightClient.cancelCurrentOperation();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if(!authenticate(userId, batchId)) return;
        String userInput = "";
        System.out.println("User authenticated");

        // Verify Username
        try {
            userInput = weightClient.rm208("VERIFY", "andersand", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) { System.err.println(Lang.msg("exceptionRM208")); }

        if (!userInput.equals("Y")) {
            batch(userId, batchId); return;
        }

        // Verify Batch
        try {
            userInput = weightClient.rm208("VERIFY", "salt", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) { System.err.println(Lang.msg("exceptionRM208")); }

        if (!userInput.equals("Y")) {
            batch(userId, batchId); return;
        }
        //System.out.println("User verified information.");

        // Unloaded
        try {
            userInput = weightClient.rm208("VERIFY", "unloaded", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) { System.err.println(Lang.msg("exceptionRM208")); }

        if (!userInput.equals("Y")) {
            batch(userId, batchId); return;
        }
        //System.out.println("Weight is unloaded.");

        // Place tara
        try {
            userInput = weightClient.rm208("Tare", "placetare", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) { System.err.println(Lang.msg("exceptionRM208")); }

        //System.out.println("Tare placed.");
        float tareWeight = 0;
        try {
            tareWeight = stof(weightClient.tareWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Weight tared as " + tareWeight + " kg.");

        try {
            userInput = weightClient.rm208("Tare", "placepowder", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) { System.err.println(Lang.msg("exceptionRM208")); }

        float netWeight = 0;
        try {
            netWeight = stof(weightClient.getCurrentWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Current weight is " + netWeight + " kg.");

        try {
            userInput = weightClient.rm208("Tare", "remove", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) { System.err.println(Lang.msg("exceptionRM208")); }

        float removedWeight = 0;
        try {
            removedWeight = stof(weightClient.getCurrentWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (removedWeight > ((-tareWeight)*1.05) && removedWeight < ((-tareWeight)*0.95)) {
            try {
                weightClient.writeToPrimaryDisplay("OK");
            } catch (IOException e) {
                System.err.print(Lang.msg("exceptionMessageDelivery"));
            }
        }
        else {
            try {
                weightClient.writeToPrimaryDisplay("TARE_ERROR");
            } catch (IOException e) {
                System.err.print(Lang.msg("exceptionMessageDelivery"));
            }
        }
    }

    private boolean authenticate(String userId, String batchId) {
        String userIdIn = "";
        String batchIdIn = "";
        try {
            userIdIn = weightClient.rm208("User", "Type user id", IWeightController.KeyPadState.NUMERIC);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (!userIdIn.equals(userId)) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            try { weightClient.writeToPrimaryDisplay("500"); weightClient.writeToSecondaryDisplay(Lang.msg("errNotAuthenticated")); }
            catch (IOException e) { System.err.println(e.getMessage()); }
            return false;
        }

        try {
            batchIdIn = weightClient.rm208("Batch", "Type batch id", IWeightController.KeyPadState.NUMERIC);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (!batchIdIn.equals(batchId)) {
            System.err.println(Lang.msg("errNoBatch"));
            try { weightClient.writeToPrimaryDisplay("404"); weightClient.writeToSecondaryDisplay(Lang.msg("errNoBatch")); }
            catch (IOException e) { System.err.println(e.getMessage()); }
            return false;
        }
        return true;
    }

    private boolean connect() {
        String[] arguments = input.split(" ");
        if (arguments.length != 3)
            System.err.println(Lang.msg("errConnectMain"));
        if (stoi(arguments[2]) == -1)
            return false;

        String host = arguments[1];
        int port = stoi(arguments[2]);

        if (connected) return true;
        System.out.println(Lang.msg("connecting") + " " + host + ":" + port + "...");

        try {
            weightClient.connect(host, port);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionConnect"));
            System.out.println(Lang.msg("exiting"));
            return false;
        }

        System.out.println(Lang.msg("connectionEstablished"));
        connected = true;
        return true;
    }

    private static int stoi(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            System.err.println(Lang.msg("errSTOI") + "!");
            return -1;
        }
    }

    private static float stof(String str) {
        try {
            str = str.replace(",",".");
            return Float.parseFloat(str);
        } catch (Exception e) {
            System.err.println(Lang.msg("errSTOF") + "!");
            return -1;
        }
    }
}
