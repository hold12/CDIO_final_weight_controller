import ctrl.*;
import dto.ProductBatchDTO;
import dto.UserDTO;
import jdbclib.DALException;
import jdbclib.DBConnector;
import jdbclib.DatabaseConnection;
import lang.Lang;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    private IWeightController weightClient;
    private DBConnector dbConnector;

    private Main(String ip, int port) {
        weightClient = new WeightController();

        try {
            dbConnector = new DBConnector(new DatabaseConnection());
        } catch (IOException e) {
            System.err.println(".env file not found.");
        }

        try {
            dbConnector.connectToDatabase();
        } catch (ClassNotFoundException | SQLException | DALException e) {
            e.printStackTrace();
        }

        if (!new ConnectionController().connect(weightClient, ip, port))
            System.exit(1);
    }

    public static void main(String[] args) {
        String[] locale = new String[2];
        String ip = null;
        int port = 0;

        if (args.length == 0) {
            locale[0] = "en";
            locale[1] = "UK";
            ip = "localhost";
            port = 8000;
        } else if (args.length == 6)
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--address")) {
                    ip = args[i + 1];
                    port = Integer.parseInt(args[i + 2]);
                    if (i == 0) i += 2;
                } else if (args[i].equals("--locale")) {
                    locale[0] = args[i + 1];
                    locale[1] = args[i + 2];
                    if (i == 0) i += 2;
                } else {
                    System.out.println("Please start the application with valid or no arguments.");
                    System.out.println("Usage: java -jar <APPLICATION_JAR>.jar --locale <language> <country> (ex. en US) --address <ip> <port>");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(1);
                }
            }
        else {
            System.out.println("Please start the application with valid or no arguments.");
            System.out.println("Usage: java -jar <APPLICATION_JAR>.jar --locale <language> <country> (ex. en US) --address <ip> <port>");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(1);
        }

        Lang.setLanguage(locale);

        Main main = new Main(ip, port);
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
        UserDTO user = null;
        ProductBatchDTO productBatch = null;
        try {
            do {
                boolean success;
                do {
                    try {
                        user = auth.getUser();
                        success = true;
                    } catch (IllegalStateException e) {
                        success = false;
                    }
                } while (!success);
                do {
                    try {
                        productBatch = auth.getBatch();
                        success = true;
                    } catch (IllegalStateException e) {
                        success = false;
                    }
                } while (!success);
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
            while (weightClient.rm208("", "Press OK to begin anew.", IWeightController.KeyPadState.NUMERIC).equals("RM20 C"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
