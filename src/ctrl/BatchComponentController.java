package ctrl;

import dto.ProductBatchComponentDTO;
import jdbclib.IConnector;
import lang.Lang;
import java.io.IOException;

/**
 * Created by freya on 06-06-2017.
 */
public class BatchComponentController {
    private IConnector connector;
    private IWeightController weightCtrl;

    public BatchComponentController(IConnector connector, IWeightController weightController) {
        this.connector = connector;
        this.weightCtrl = weightController;
    }

    public boolean doWeighing(ProductBatchComponentDTO component){
        String userInput = "";

        // Unloaded
        try {
            userInput = weightCtrl.rm208("", "unloaded", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (userInput.startsWith("RM20 C")) return false;

        // Place tara
        try {
            userInput = weightCtrl.rm208("", "Place tare", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (userInput.startsWith("RM20 C")) return false;

        float tareWeight = 0;
        try {
            tareWeight = stof(weightCtrl.tareWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            userInput = weightCtrl.rm208("", "Place powder", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (userInput.startsWith("RM20 C")) return false;

        float netWeight = 0;
        try {
            netWeight = stof(weightCtrl.getCurrentWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            userInput = weightCtrl.rm208("", "Remove all", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (userInput.startsWith("RM20 C")) return false;

        float removedWeight = 0;
        try {
            removedWeight = stof(weightCtrl.getCurrentWeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (removedWeight >= ((-tareWeight) * 1.05) && removedWeight <= ((-tareWeight) * 0.95)) {
            try {
                weightCtrl.writeToPrimaryDisplay("OK");
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
                weightCtrl.writeToPrimaryDisplay("TareErr");
                weightCtrl.writeToSecondaryDisplay("Taring not approved. Try again");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            } catch (IOException e) {
                System.err.print(Lang.msg("exceptionMessageDelivery"));
            }
        }

        return true;
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
