import ctrl.*;
import dto.ProductBatchDTO;
import dto.UserDTO;
import jdbclib.DALException;
import jdbclib.DBConnector;
import jdbclib.DatabaseConnection;
import lang.Lang;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private IWeightController weightClient;
    private String input;
    private Scanner scn;
    private DBConnector dbConnector;

    private Main() {
        weightClient = new WeightController();
        scn = new Scanner(System.in);
        try {
            dbConnector = new DBConnector(new DatabaseConnection());
        } catch (IOException e) {
            System.err.println(".env file not found.");
        }
        try {
            dbConnector.connectToDatabase();
        } catch (ClassNotFoundException | SQLException | DALException e ) {
            e.printStackTrace();
        }

        if (!new ConnectionController().connect(weightClient, "localhost", 8000))
            System.exit(1);
    }

    public static void main(String[] args) {
        String[] locale = new String[2];
        if (args.length == 0) { locale[0] = "en";    locale[1] = "UK"; }
        else                  { locale[0] = args[0]; locale[1] = args[1]; }
        Lang.setLanguage(locale);

        Main main = new Main();
        while (true)
            main.startProgram();
    }

    private void startProgram() {
        try {
            weightClient.cancelCurrentOperation();
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionReset"));
        }

        AuthenticateController auth = new AuthenticateController(dbConnector, weightClient);
        UserDTO user;
        ProductBatchDTO productBatch;
        try {
            do {
                user = auth.getUser();
                productBatch = auth.getBatch();
            }
            while (!auth.authenticate(user.getUserId(), productBatch));
        } catch (IOException | DALException e) {
            e.printStackTrace();
            return;
        }

        BatchController batchCtrl = new BatchController(dbConnector, weightClient);
        try {
            batchCtrl.batch(productBatch);
        } catch (DALException e) {
            e.printStackTrace();
            return;
        }

        try {
            while (weightClient.rm208("", "Press OK to begin anew.", IWeightController.KeyPadState.NUMERIC).equals("RM20 C"))
                ;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
