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

    public ProductBatchDTO getBatch() throws IOException, DALException {
        String batchId = weightCtrl.rm208(Lang.msg("id"), Lang.msg("enterBatchId"), IWeightController.KeyPadState.NUMERIC);
        String userInput;

        // Find productbatch in database
        ProductBatchDAO productBatchDAO = new ProductBatchDAO(connector);
        ProductBatchDTO productBatch;
        try {
            productBatch = productBatchDAO.getProductBatch(Integer.parseInt(batchId));
            System.out.println(productBatch);
        } catch (DALException e) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNoBatch"), IWeightController.KeyPadState.LOWER_CHARS);
            throw new IllegalStateException(Lang.msg("errNoBatch"));
        }

        // Verify Batch recipe name
        RecipeDTO recipe = new RecipeDAO(connector).getRecipe(productBatch.getRecipeId());

        // Find recipe name in database
        String recipeName = recipe.getRecipeName();
        if (recipeName.length() > 30)
            recipeName = recipeName.substring(0, 30);

        userInput = weightCtrl.rm208("", recipeName, IWeightController.KeyPadState.UPPER_CHARS);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException(Lang.msg("errNotAuthenticated"));

        return productBatch;
    }

    public UserDTO getUser() throws IOException, IllegalStateException {
        String userId = weightCtrl.rm208(Lang.msg("id"), Lang.msg("enterUserId"), IWeightController.KeyPadState.NUMERIC);

        // Find user in database
        UserDAO userDAO = new UserDAO(connector);
        UserDTO user;
        try {
            user = userDAO.getUser(Integer.parseInt(userId));
            System.out.println(user);
        } catch (DALException e) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNoSuchUser"), IWeightController.KeyPadState.LOWER_CHARS);
            throw new IllegalStateException(Lang.msg("errNoSuchUser"));
        }

        String userInput;

        // Verify Username
        String userName = user.getFirstname() + " " + user.getLastname();
        if (userName.length() > 30)
            userName = userName.substring(0, 30);

        userInput = weightCtrl.rm208("", userName, IWeightController.KeyPadState.UPPER_CHARS);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException(Lang.msg("errNotAuthenticated"));

        return user;
    }

    public boolean authenticate(int userId, ProductBatchDTO productBatch) throws IOException, IllegalStateException {
        // Is productbatch already done?
        if (productBatch.getStatus() == 2) {
            weightCtrl.rm208(Lang.msg("err"), Lang.msg("errBatchStatus"), IWeightController.KeyPadState.LOWER_CHARS);
            return false;
        }

        // Is user id on productbatch same as entered user id?
        if (productBatch.getUserId() != userId) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNotAuthenticated"), IWeightController.KeyPadState.LOWER_CHARS);
            return false;
        }

        return true;
    }
}
