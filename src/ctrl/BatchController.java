package ctrl;

import dao.*;
import dto.*;
import jdbclib.DALException;
import jdbclib.IConnector;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class BatchController {
    private IConnector connector;
    private IWeightController weightCtrl;
    private List<ProductBatchComponentDTO> productBatchComponents;

    public BatchController(IConnector connector, IWeightController weightClient) {
        this.connector = connector;
        this.weightCtrl = weightClient;
    }

    public boolean batch(int batchId) throws DALException {
        productBatchComponents = new LinkedList<>();

        // Get productbatch and recipecomponents
        ProductBatchDTO productBatch = new ProductBatchDAO(connector).getProductBatch(batchId);
        List<RecipeComponentDTO> recipeComponents = new RecipeComponentDAO(connector).getRecipeComponentList(productBatch.getRecipeId());

        // Go through every recipe component
        for (RecipeComponentDTO recipeComponent : recipeComponents) {
            BatchComponentController batchComponentCtrl = new BatchComponentController(connector, weightCtrl);

            //Find ingredient for recipe component
            IngredientDTO ingredient = new IngredientDAO(connector).getIngredient(recipeComponent.getIngredientId());

            boolean isSuccess;
            do {
                try {
                    isSuccess = batchComponentCtrl.batchComponent(productBatch, recipeComponent, ingredient);
                } catch (IllegalStateException | IOException e) {
                    //Update productbatch status = 0
                    productBatch.setStatus(0);
                    new ProductBatchDAO(connector).updateProductBatch(productBatch);
                    return false;
                }
            } while (!isSuccess);

            //Update productbatch status = 1
            productBatch.setStatus(1);
            new ProductBatchDAO(connector).updateProductBatch(productBatch);

            // Add product batch component to list
            productBatchComponents.add(batchComponentCtrl.getProductBatchComponent());
        }

        addProductBatchComponents(productBatchComponents);

        //Update productbatch status = 2 and Finished time
        productBatch.setStatus(2);
        productBatch.setFinishedTime(new Timestamp(System.currentTimeMillis()));
        new ProductBatchDAO(connector).updateProductBatch(productBatch);

        //Update ingredient batch amount
        updateIngredientBatches(productBatchComponents);

        return true;
    }

    private void addProductBatchComponents(List<ProductBatchComponentDTO> productBatchComponents) throws DALException {
        for (ProductBatchComponentDTO component : productBatchComponents) {
            new ProductBatchComponentDAO(connector).createProductBatchComponent(component);
        }
    }

    private void updateIngredientBatches(List<ProductBatchComponentDTO> productBatchComponents) throws DALException {
        for (ProductBatchComponentDTO component : productBatchComponents){
            IngredientBatchDTO ingredientBatch = new IngredientBatchDAO(connector).getIngredientBatch(component.getIngredientbatchId());

            double amount = ingredientBatch.getAmount();
            amount = amount - component.getNetWeight();

            ingredientBatch.setAmount(amount);
            new IngredientBatchDAO(connector).updateIngredientBatch(ingredientBatch);
        }
    }

}
