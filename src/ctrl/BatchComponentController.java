package ctrl;

import dao.IngredientBatchDAO;
import dao.ProductBatchDAO;
import dto.*;
import jdbclib.DALException;
import jdbclib.IConnector;
import lang.Lang;

import java.io.IOException;

/**
 * Created by freya on 06-06-2017.
 */
public class BatchComponentController {
    private IConnector connector;
    private IWeightController weightCtrl;
    private ProductBatchComponentDTO productBatchComponent;

    public BatchComponentController(IConnector connector, IWeightController weightController) {
        this.connector = connector;
        this.weightCtrl = weightController;
    }

    public ProductBatchComponentDTO getProductBatchComponent() {
        return productBatchComponent;
    }

    public boolean batchComponent(ProductBatchDTO productBatch, RecipeComponentDTO recipeComponent, IngredientDTO ingredient) throws IOException, IllegalStateException, DALException {
        String userInput;
        productBatchComponent = new ProductBatchComponentDTO(productBatch.getProductbatchId(), 0, 0, 0);

        // Unloaded
        userInput = rm208("", Lang.msg("unloaded"), IWeightController.KeyPadState.NUMERIC);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");
        weightCtrl.tareWeight();

        //Update productbatch status = 1
        productBatch.setStatus(1);
        new ProductBatchDAO(connector).updateProductBatch(productBatch);

        // Place tara
        userInput = rm208("", Lang.msg("placeTare"), IWeightController.KeyPadState.NUMERIC);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        float tareWeight = stof(weightCtrl.tareWeight());
        productBatchComponent.setTare(tareWeight);

        // Confirm ingredient
        String ingredientName = ingredient.getIngredientName();
        ingredientName = ingredientName.substring(0,1).toUpperCase() + ingredientName.substring(1,ingredientName.length());

        userInput = rm208("",  ingredientName + " " + Lang.msg("isnext"), IWeightController.KeyPadState.NUMERIC);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        // Enter ingredient batch
        IngredientBatchDTO ingredientBatch;
        do {
            ingredientBatch = getIngredientBatch(recipeComponent);
        } while (ingredientBatch == null);
        productBatchComponent.setIngredientbatchId(ingredientBatch.getIngredientBatchId());

        // Place ingredient
        userInput = rm208("" + ingredientBatch.getIngredientBatchId(), Lang.msg("place") + " " + ingredient.getIngredientName(), IWeightController.KeyPadState.NUMERIC);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        float netWeight = stof(weightCtrl.getCurrentWeight());
        productBatchComponent.setNetWeight(netWeight);

        if (netWeight > recipeComponent.getNominatedNetWeight() + recipeComponent.getTolerance()
                || netWeight < recipeComponent.getNominatedNetWeight() - recipeComponent.getTolerance()) {
            weightCtrl.writeToSecondaryDisplay(Lang.msg("errWeight"));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }

        // Remove all
        userInput = rm208("", Lang.msg("removeall"), IWeightController.KeyPadState.NUMERIC);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        float removedWeight = stof(weightCtrl.getCurrentWeight());

        // Taring control
        if (removedWeight >= ((-tareWeight) - 0.002) && removedWeight <= ((-tareWeight) + 0.002)) {
            weightCtrl.writeToPrimaryDisplay("OK");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            weightCtrl.writeToPrimaryDisplay("TareErr");
            weightCtrl.writeToSecondaryDisplay(Lang.msg("errTareControl"));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }

        return true;
    }

    private IngredientBatchDTO getIngredientBatch(RecipeComponentDTO recipeComponent) throws IOException {
        String userInput;
        IngredientBatchDTO ingredientBatch = null;
        String ingredientBatchId = rm208(Lang.msg("id"), Lang.msg("enterIngredientBatchId"), IWeightController.KeyPadState.NUMERIC);
        try {
            ingredientBatch = new IngredientBatchDAO(connector).getIngredientBatch(Integer.parseInt(ingredientBatchId));
        } catch (DALException | NumberFormatException e) {
            weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNoIngredientBatch"), IWeightController.KeyPadState.NUMERIC);
        }

        if (ingredientBatch.getIngredientId() != recipeComponent.getIngredientId()){
            ingredientBatch = null;
            userInput = rm208("", Lang.msg("errNoIngredientBatchForIngredient"), IWeightController.KeyPadState.NUMERIC);
            if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");
        } else if (ingredientBatch.getAmount() < recipeComponent.getNominatedNetWeight()){
            ingredientBatch = null;
            userInput = rm208("", Lang.msg("errStockNotSufficient"), IWeightController.KeyPadState.NUMERIC);
            if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");
        }

        return ingredientBatch;
    }

    private String rm208(String primary, String secondary, IWeightController.KeyPadState keyPadState) throws IOException {
        return weightCtrl.rm208(substring7(primary), substring30(secondary), keyPadState);
    }

    private String substring7(String string) {
        if (string.length() > 7) {
            return string.substring(0, 7);
        } else {
            return string;
        }
    }

    private String substring30(String string) {
        if (string.length() > 30) {
            return string.substring(0, 30);
        } else {
            return string;
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
