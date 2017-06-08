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

    public int getBatch(IWeightController weightClient) {
        String batchIdIn = "";
        try {
            batchIdIn = weightClient.rm208("Batch", "Type batch id", IWeightController.KeyPadState.NUMERIC);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }
        return Integer.parseInt(batchIdIn);
    }

    public int getUser(IWeightController weightClient) {
        String userIdIn = "";
        try {
            userIdIn = weightClient.rm208("User", "Type user id", IWeightController.KeyPadState.NUMERIC);
        } catch (IOException e) {
            System.err.println(Lang.msg("exceptionRM208"));
        }
        return Integer.parseInt(userIdIn);
    }

    public boolean authenticate(IConnector connector, IWeightController weightClient, int userId, int batchId) {
        //Find productbatch in database
        ProductBatchDAO productBatchDAO = new ProductBatchDAO(connector);
        ProductBatchDTO productBatch = null;
        try {
            productBatch = productBatchDAO.getProductBatch(batchId);
            System.out.println(productBatch);
        } catch (DALException e) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            try {
                weightClient.rm208(Lang.msg("err"), Lang.msg("errNoBatch"), IWeightController.KeyPadState.LOWER_CHARS);
            } catch (IOException e2) {
                System.err.println(e2.getMessage());
            }
            return false;
        }

        //Is user id on productbatch same as entered user id?
        if(productBatch.getUserId() != userId){
            System.err.println(Lang.msg("errNotAuthenticated"));
            try {
                weightClient.rm208(Lang.msg("err"), Lang.msg("errNotAuthenticated"), IWeightController.KeyPadState.LOWER_CHARS);
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
                weightClient.rm208(Lang.msg("err"), Lang.msg("errNoSuchUser"), IWeightController.KeyPadState.LOWER_CHARS);
            } catch (IOException e2) {
                System.err.println(e2.getMessage());
            }
            return false;
        }

        String userInput = "";

        // Verify Username
        String userName = user.getFirstname() + " " + user.getLastname();
        if (userName.length() > 30)
            userName = userName.substring(0,30);

        try {
            userInput = weightClient.rm208(
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

        String recipeName = recipe.getRecipeName();
        if (recipeName.length() > 30)
            recipeName = recipeName.substring(0,30);

        try {
            userInput = weightClient.rm208(
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
