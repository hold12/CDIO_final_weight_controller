package ctrl;

import dao.*;
import dto.*;
import jdbclib.DALException;
import jdbclib.IConnector;
import lang.Lang;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class BatchController {
    private IConnector connector;
    private IngredientBatchDAO ingredientBatchDAO;
    private ProductBatchDAO productBatchDAO;
    private IngredientDAO ingredientDAO;
    private ProductBatchComponentDAO productBatchComponentDAO;
    private RecipeComponentDAO recipeComponentDAO;
    private IWeightController weightCtrl;
    private ProductBatchDTO productBatch;
    private List<ProductBatchComponentDTO> productBatchComponents;

    public BatchController(IConnector connector, IWeightController weightClient) {
        this.connector = connector;
        this.weightCtrl = weightClient;
        ingredientBatchDAO = new IngredientBatchDAO(connector);
        productBatchDAO = new ProductBatchDAO(connector);
        ingredientDAO = new IngredientDAO(connector);
        productBatchComponentDAO = new ProductBatchComponentDAO(connector);
        recipeComponentDAO = new RecipeComponentDAO(connector);
    }

    public void batch(ProductBatchDTO productBatch) throws DALException {
        this.productBatch = productBatch;
        productBatchComponents = new LinkedList<>();

        // Get recipecomponents
        List<RecipeComponentDTO> recipeComponents = recipeComponentDAO.getRecipeComponentList(productBatch.getRecipeId());

        // Go through every recipe component
        for (RecipeComponentDTO recipeComponent : recipeComponents) {
            if (!batchComponent(recipeComponent))
                return;
        }
        insertProductBatchComponents(productBatchComponents);

        //Update productbatch status = 2 and Finished time
        productBatch.setStatus(2);
        productBatch.setFinishedTime(new Timestamp(System.currentTimeMillis()));
        productBatchDAO.updateProductBatch(productBatch);

        //Update ingredient batch amount
        updateIngredientBatches(productBatchComponents);
    }

    private boolean batchComponent(RecipeComponentDTO recipeComponent) throws DALException{
        BatchComponentController batchComponentCtrl = new BatchComponentController(connector, weightCtrl);

        //Find ingredient for recipe component
        IngredientDTO ingredient = ingredientDAO.getIngredient(recipeComponent.getIngredientId());

        boolean isSuccess;
        do {
            try {
                weightCtrl.cancelCurrentOperation();
            } catch (IOException e) {
                System.err.println(Lang.msg("exceptionReset"));
            }

            try {
                isSuccess = batchComponentCtrl.batchComponent(productBatch, recipeComponent, ingredient);
            } catch (IllegalStateException | IOException | DALException e) {
                //Update productbatch status = 0
                productBatch.setStatus(0);
                productBatchDAO.updateProductBatch(productBatch);
                return false;
            }
        } while (!isSuccess);

        // Add product batch component to list
        productBatchComponents.add(batchComponentCtrl.getProductBatchComponent());

        return true;
    }

    private void insertProductBatchComponents(List<ProductBatchComponentDTO> productBatchComponents) throws DALException {
        for (ProductBatchComponentDTO component : productBatchComponents) {
            productBatchComponentDAO.createProductBatchComponent(component);
        }
    }

    private void updateIngredientBatches(List<ProductBatchComponentDTO> productBatchComponents) throws DALException {
        for (ProductBatchComponentDTO component : productBatchComponents){
            IngredientBatchDTO ingredientBatch = ingredientBatchDAO.getIngredientBatch(component.getIngredientbatchId());

            double amount = ingredientBatch.getAmount();
            amount = amount - component.getNetWeight();

            ingredientBatch.setAmount(amount);
            ingredientBatchDAO.updateIngredientBatch(ingredientBatch);
        }
    }

}
