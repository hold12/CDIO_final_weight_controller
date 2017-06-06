package ctrl;

import jdbclib.IConnector;
import lang.Lang;

import java.io.IOException;

public class BatchController {

    public BatchController(IWeightController weightClient, int userId, int batchId) {

        String userInput = "";

        // Unloaded
        try {
            userInput = weightClient.rm208("VERIFY", "unloaded", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (!userInput.equals("Y")) {
            new BatchController(weightClient, userId, batchId);
        }
        //System.out.println("Weight is unloaded.");

        // Place tara
        try {
            weightClient.rm208("", "Place tare", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        //System.out.println("Tare placed.");
        float tareWeight = 0;
        try {
            tareWeight = stof(weightClient.tareWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Weight tared as " + tareWeight + " kg.");

        try {
            weightClient.rm208("", "Place powder", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        float netWeight = 0;
        try {
            netWeight = stof(weightClient.getCurrentWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Current weight is " + netWeight + " kg.");

        try {
            weightClient.rm208("", "Remove all", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        float removedWeight = 0;
        try {
            removedWeight = stof(weightClient.getCurrentWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*System.out.println(removedWeight);
        System.out.println(((-tareWeight)*1.05));
        System.out.println(((-tareWeight)*0.95));*/
        if (removedWeight >= ((-tareWeight) * 1.05) && removedWeight <= ((-tareWeight) * 0.95)) {
            try {
                weightClient.writeToPrimaryDisplay("OK");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.err.print(Lang.msg("exceptionMessageDelivery"));
            }
        } else {
            try {
                weightClient.writeToPrimaryDisplay("TareErr");
                weightClient.writeToSecondaryDisplay("Taring not approved. Try again");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.err.print(Lang.msg("exceptionMessageDelivery"));
            }
        }
    }

    private static float stof(String str) {
        try {
            str = str.replace(",", ".");
            return Float.parseFloat(str);
        } catch (Exception e) {
            System.err.println(Lang.msg("errSTOF") + "!");
            return -1;
        }
    }

}
