import ctrl.AuthenticateController;
import ctrl.BatchController;
import lang.Lang;
import ctrl.IWeightController;
import ctrl.WeightController;

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
            while (true){
                try {
                    weightClient.cancelCurrentOperation();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                new AuthenticateController().authenticate(weightClient,"12","1234");
                new BatchController(weightClient,"12","1234");

                try {
                    while(weightClient.rm208("","Press OK to begin anew.", IWeightController.KeyPadState.NUMERIC).equals("RM20 C"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (input.startsWith("close") || input.startsWith("exit") || input.startsWith("quit")) {
            return;
        }

        menu();
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
}
