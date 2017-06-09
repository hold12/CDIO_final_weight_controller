package ctrl;

import lang.Lang;

import java.io.IOException;

public class AuthenticateController {

    public boolean authenticate(IWeightController weightClient,String userId, String batchId) {
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

        String userInput = "";

        // Verify Username
        try {
            userInput = weightClient.rm208("VERIFY", "andersand", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) { System.err.println(Lang.msg("exceptionRM208")); }

        if (!userInput.equals("Y")) {
            return false;
        }

        // Verify Batch
        try {
            userInput = weightClient.rm208("VERIFY", "salt", IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) { System.err.println(Lang.msg("exceptionRM208")); }

        if (!userInput.equals("Y")) {
            return false;
        }
        //System.out.println("User verified information.");


        return true;
    }
}
