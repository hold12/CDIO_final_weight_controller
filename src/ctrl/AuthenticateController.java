package ctrl;

import dao.ProductBatchDAO;
import dao.RecipeDAO;
import dao.UserDAO;
import dto.ProductBatchDTO;
import dto.RecipeDTO;
import dto.UserDTO;
import jdbclib.DALException;
import jdbclib.IConnector;
import lang.Lang;

import java.io.IOException;

public class AuthenticateController {
    private IConnector connector;
    private IWeightController weightCtrl;

    public AuthenticateController(IConnector connector, IWeightController weightCtrl) {
        this.connector = connector;
        this.weightCtrl = weightCtrl;
    }

    public int getBatch() {
        String batchIdIn = "";
        try {
            batchIdIn = weightCtrl.rm208("Batch", "Type batch id", IWeightController.KeyPadState.NUMERIC);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }
        return Integer.parseInt(batchIdIn);
    }

    public int getUser() {
        String userIdIn = "";
        try {
            userIdIn = weightCtrl.rm208("User", "Type user id", IWeightController.KeyPadState.NUMERIC);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }
        return Integer.parseInt(userIdIn);
    }

    public boolean authenticate(int userId, int batchId) {
        //Find productbatch in database
        ProductBatchDAO productBatchDAO = new ProductBatchDAO(connector);
        ProductBatchDTO productBatch;
        try {
            productBatch = productBatchDAO.getProductBatch(batchId);
            System.out.println(productBatch);
        } catch (DALException e) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            try {
                weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNoBatch"), IWeightController.KeyPadState.LOWER_CHARS);
            } catch (IOException e2) {
                System.err.println(e2.getMessage());
            }
            return false;
        }

        //Is user id on productbatch same as entered user id?
        if (productBatch.getUserId() != userId) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            try {
                weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNotAuthenticated"), IWeightController.KeyPadState.LOWER_CHARS);
            } catch (IOException e2) {
                System.err.println(e2.getMessage());
            }
            return false;
        }

        //Find user in database
        UserDAO userDAO = new UserDAO(connector);
        UserDTO user = null;
        try {
            user = userDAO.getUser(userId);
            System.out.println(user);
        } catch (DALException e) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            try {
                weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNoSuchUser"), IWeightController.KeyPadState.LOWER_CHARS);
            } catch (IOException e2) {
                System.err.println(e2.getMessage());
            }
            return false;
        }

        String userInput = "";

        // Verify Username
        String userName = user.getFirstname() + " " + user.getLastname();
        if (userName.length() > 30)
            userName = userName.substring(0, 30);

        try {
            userInput = weightCtrl.rm208(
                    "",
                    userName,
                    IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (userInput.startsWith("RM20 C")) return false;

        // Verify Batch
        RecipeDAO recipeDAO = new RecipeDAO(connector);
        RecipeDTO recipe = null;
        try {
            recipe = recipeDAO.getRecipe(productBatch.getRecipeId());
        } catch (DALException e) {
            e.printStackTrace();
        }

        //Find recipe name in database
        String recipeName = recipe.getRecipeName();
        if (recipeName.length() > 30)
            recipeName = recipeName.substring(0, 30);

        try {
            userInput = weightCtrl.rm208(
                    "",
                    recipeName,
                    IWeightController.KeyPadState.UPPER_CHARS);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }

        if (userInput.startsWith("RM20 C")) return false;

        //System.out.println("User verified information.");

        return true;
    }
}
