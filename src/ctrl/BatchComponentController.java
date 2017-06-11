package ctrl;

import dao.IngredientBatchDAO;
import dao.ProductBatchDAO;
import dto.*;
import jdbclib.DALException;
import jdbclib.IConnector;
import lang.Lang;

import java.io.IOException;
import java.util.LinkedList;

public class BatchComponentController {
    private final IConnector connector;
    private final IWeightController weightCtrl;
    private ProductBatchDTO productBatch;
    private RecipeComponentDTO recipeComponent;
    private IngredientDTO ingredient;
    private ProductBatchComponentDTO productBatchComponent;
    private float tareWeight, removedWeight;

    public BatchComponentController(IConnector connector, IWeightController weightController) {
        this.connector = connector;
        this.weightCtrl = weightController;
    }

    public ProductBatchComponentDTO getProductBatchComponent() {
        return productBatchComponent;
    }

    public boolean batchComponent(ProductBatchDTO productBatch, RecipeComponentDTO recipeComponent, IngredientDTO ingredient) throws IOException, IllegalStateException, DALException {
        productBatchComponent = new ProductBatchComponentDTO(productBatch.getProductbatchId(), 0, 0, 0);
        this.productBatch = productBatch;
        this.recipeComponent = recipeComponent;
        this.ingredient = ingredient;

        isWeightUnloaded();
        setProductBatchStatusInProgress();
        tareWeight();
        confirmIngredient();
        getIngredientBatch();
        getWeight();
        removeAll();
        return tareControl();
    }

    private void isWeightUnloaded() throws IOException{
        // Unloaded
        String userInput = rm208("", Lang.msg("unloaded"), IWeightController.KeyPadState.NUMERIC);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");
        weightCtrl.tareWeight();
    }

    private void setProductBatchStatusInProgress() throws DALException {
        productBatch.setStatus(1);
        new ProductBatchDAO(connector).updateProductBatch(productBatch);
    }

    private void tareWeight() throws IOException{
        // Place tara
        weightCtrl.showWeightDisplay();
        weightCtrl.writeToSecondaryDisplay(Lang.msg("placeTare"));

        LinkedList<String> buttons = new LinkedList<>();
        buttons.add("");
        buttons.add("");
        buttons.add("");
        buttons.add("");
        buttons.add(Lang.msg("ok"));
        weightCtrl.rm36(buttons);
        weightCtrl.rm38(buttons.size());
        weightCtrl.receiveMessage(); //Wait for user to push a button

        float tareWeight = stof(weightCtrl.tareWeight());
        productBatchComponent.setTare(tareWeight);
        this.tareWeight = tareWeight;
    }

    private void confirmIngredient() throws IOException{
        // Confirm ingredient
        String ingredientName = ingredient.getIngredientName();
        ingredientName = ingredientName.substring(0,1).toUpperCase() + ingredientName.substring(1,ingredientName.length());

        String userInput = rm208("",  ingredientName + " " + Lang.msg("isnext"), IWeightController.KeyPadState.NUMERIC);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");
    }

    private void getIngredientBatch() throws IOException {
        String userInput;
        IngredientBatchDTO ingredientBatch;
        String ingredientBatchId = rm208(Lang.msg("id"), Lang.msg("enterIngredientBatchId"), IWeightController.KeyPadState.NUMERIC);
        try {
            ingredientBatch = new IngredientBatchDAO(connector).getIngredientBatch(Integer.parseInt(ingredientBatchId));

            if (ingredientBatch.getIngredientId() != recipeComponent.getIngredientId()){
                userInput = rm208("", Lang.msg("errNoIngredientBatchForIngredient"), IWeightController.KeyPadState.NUMERIC);
                if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");
                getIngredientBatch(); return;
            } else if (ingredientBatch.getAmount() < recipeComponent.getNominatedNetWeight()){
                userInput = rm208("", Lang.msg("errStockNotSufficient"), IWeightController.KeyPadState.NUMERIC);
                if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");
                getIngredientBatch(); return;
            }
        } catch (DALException | NumberFormatException e) {
            weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNoIngredientBatch"), IWeightController.KeyPadState.NUMERIC);
            getIngredientBatch(); return;
        }
        productBatchComponent.setIngredientbatchId(ingredientBatch.getIngredientBatchId());
    }

    private void getWeight() throws IOException {
        // Place ingredient
        weightCtrl.showWeightDisplay();
        weightCtrl.writeToSecondaryDisplay(Lang.msg("place") + " " + ingredient.getIngredientName());

        LinkedList<String> buttons = new LinkedList<>();
        buttons.add("");
        buttons.add("");
        buttons.add("");
        buttons.add("");
        buttons.add(Lang.msg("ok"));
        weightCtrl.rm36(buttons);
        weightCtrl.rm38(buttons.size());
        weightCtrl.receiveMessage(); //Wait for user to push a button

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
            throw new IllegalStateException(Lang.msg("errWeight"));
        }
    }

    private void removeAll() throws IOException {
        // Remove all
        String userInput = rm208("", Lang.msg("removeall"), IWeightController.KeyPadState.NUMERIC);
        if (userInput.startsWith("RM20 C")) throw new IllegalStateException("User cancelled operation");

        this.removedWeight = stof(weightCtrl.getCurrentWeight());
    }

    private boolean tareControl() throws IOException {
        // Taring control
        if (removedWeight >= ((-tareWeight) - 0.002) && removedWeight <= ((-tareWeight) + 0.002)) {
            weightCtrl.writeToPrimaryDisplay("OK");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
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
    }

    private String rm208(String primary, String secondary, IWeightController.KeyPadState keyPadState) throws IOException {
        return weightCtrl.rm208(substring7(primary), substring24(secondary), keyPadState);
    }

    private String substring7(String str) {
        if (str.length() > 7) {
            return str.substring(0, 7);
        } else {
            return str;
        }
    }

    private String substring24(String str) {
        if (str.length() > 24) {
            return str.substring(0, 24);
        } else {
            return str;
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
