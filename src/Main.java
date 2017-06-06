import ctrl.*;
import lang.Lang;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private IWeightController weightClient;
    private String input;
    private Scanner scn;

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
            String[] arguments = input.split(" ");

            if (arguments.length != 3)
                System.err.println(Lang.msg("errConnectMain"));

            if (input.startsWith("connect")) {
                if (!new ConnectionController().connect(weightClient,arguments[1],stoi(arguments[2])))
                    System.exit(1);
                break;
            } else {
                System.err.println(Lang.msg("errCmdNotFound"));
            }
        }

        while (true){
            try {
                weightClient.cancelCurrentOperation();
            } catch (IOException e) {
                System.err.println(Lang.msg("exceptionReset"));
            }

            new AuthenticateController().authenticate(weightClient,"12","1234");
            new BatchController(weightClient, "12", "1234");

            try {
                while(weightClient.rm208("","Press OK to begin anew.", IWeightController.KeyPadState.NUMERIC).equals("RM20 C"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
