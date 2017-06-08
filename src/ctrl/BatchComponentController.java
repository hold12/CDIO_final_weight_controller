package ctrl;

import dao.IngredientBatchDAO;
import dto.*;
import jdbclib.DALException;
import jdbclib.IConnector;
import lang.Lang;

import java.io.IOException;
import java.util.List;

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

    public boolean batchComponent(ProductBatchDTO productBatch, RecipeComponentDTO recipeComponent, IngredientDTO ingredient) throws IOException, IllegalStateException {
        String userInput;
        productBatchComponent = new ProductBatchComponentDTO(productBatch.getProductbatchId(), 0, 0, 0);

        // Find ingredient batch
        IngredientBatchDTO ingredientBatch = getIngredientBatch(ingredient, recipeComponent);

        // If no ingredient batch
        if (ingredientBatch == null) {
            rm208("", ingredient.getIngredientId() + ": " + Lang.msg("errNoIngredientBatch"), IWeightController.KeyPadState.UPPER_CHARS);
            return false;
        }

        productBatchComponent.setIngredientbatchId(ingredientBatch.getIngredientBatchId());

        // Verify ingredient
        userInput = rm208("" + ingredientBatch.getIngredientBatchId(), ingredient.getIngredientName(), IWeightController.KeyPadState.UPPER_CHARS);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        // Unloaded
        userInput = rm208("", Lang.msg("unloaded"), IWeightController.KeyPadState.UPPER_CHARS);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        // Place tara
        userInput = rm208("", Lang.msg("placeTare"), IWeightController.KeyPadState.UPPER_CHARS);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        float tareWeight = stof(weightCtrl.tareWeight());
        productBatchComponent.setTare(tareWeight);

        // Place ingredient
        userInput = rm208("" + ingredientBatch.getIngredientBatchId(), Lang.msg("place") + " " + ingredient.getIngredientName(), IWeightController.KeyPadState.UPPER_CHARS);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        float netWeight = stof(weightCtrl.getCurrentWeight());
        productBatchComponent.setNetWeight(netWeight);

        if (netWeight > recipeComponent.getNominatedNetWeight() + recipeComponent.getTolerance()
                || netWeight < recipeComponent.getNominatedNetWeight() - recipeComponent.getTolerance()) {
            weightCtrl.writeToPrimaryDisplay("TareErr");
            weightCtrl.writeToSecondaryDisplay(Lang.msg("errTareControl"));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }

        // Remove all
        userInput = rm208("", Lang.msg("removeall"), IWeightController.KeyPadState.UPPER_CHARS);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        float removedWeight = 0;
        removedWeight = stof(weightCtrl.getCurrentWeight());

        // Taring control
        if (removedWeight >= ((-tareWeight) + 0.002) && removedWeight <= ((-tareWeight) - 0.002)) {
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

    private IngredientBatchDTO getIngredientBatch(IngredientDTO ingredient, RecipeComponentDTO recipeComponent) {
        List<IngredientBatchDTO> ingredientBatches;
        IngredientBatchDTO ingredientBatch = null;

        try {
            ingredientBatches = new IngredientBatchDAO(connector).getIngredientBatchList(ingredient.getIngredientId());
        } catch (DALException e) {
            e.printStackTrace();
            return null;
        }

        for (IngredientBatchDTO i : ingredientBatches) {
            if (i.getAmount() >= recipeComponent.getNominatedNetWeight()) {
                ingredientBatch = i;
                break;
            }
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
