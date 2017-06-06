package unit;

import jdbclib.IConnector;
import jdbclib.DALException;
import dto.*;
import org.jmock.Expectations;
import org.jmock.Mockery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConnector implements IConnector {
    private final Mockery mockery = new Mockery();
    private final ResultSet resultSet = mockery.mock(ResultSet.class);
    private boolean selected;
    private boolean inserted;
    private boolean updated;
    private boolean deleted;

    public TestConnector() throws SQLException{
        this.selected = false;
        this.inserted = false;
        this.updated = false;
        this.deleted = false;

        mockery.checking(new Expectations() {{
            allowing(resultSet).first(); will(returnValue(true));
        }});
    }

    @Override
    public Connection connectToDatabase() throws ClassNotFoundException, SQLException {
        return null;
    }

    @Override
    public ResultSet query(String cmd) throws DALException {
        cmd = cmd.toLowerCase();
        if (cmd.contains("select")) {
            this.selected = true;

            // If the SQL statement includes "user" in it
            if (cmd.contains("from view_user")) {
                UserDTO user = new UserDTO(1, "John", "Doe", "JD", "p455w0rd!", false);
                // Insert an user to the ResultSet
                insertUserResultSet(user);
            } else if (cmd.contains("from view_ingredientbatch")) {
                IngredientBatchDTO ingredientBatchDTO = new IngredientBatchDTO(1, 2, 3.4);
                // Insert an ingredient batch to the ResultSet
                insertIngredientBatchResultSet(ingredientBatchDTO);
            } else if (cmd.contains("from view_ingredient")) {
                IngredientDTO ingredientDTO = new IngredientDTO(1, "tomato", "Heinz");
                // Insert an ingredient to the ResultSet
                insertIngredientResultSet(ingredientDTO);
            } else if (cmd.contains("from view_productbatchcomponent")) {
                ProductBatchComponentDTO productBatchComponentDTO = new ProductBatchComponentDTO(1, 1, 0.5, 10);
                // Insert an ingredient to the ResultSet
                insertProductBatchComponentResultSet(productBatchComponentDTO);
            } else if(cmd.contains("from view_recipecomponent")){
                RecipeComponentDTO recipeComponentDTO = new RecipeComponentDTO(1,2, 1.2, 1.2);
                insertRecipeComponentResultSet(recipeComponentDTO);
            } else if (cmd.contains("from view_recipe")) {
                RecipeDTO recipeDTO = new RecipeDTO(1, "pizza");
                // Insert a recipe to the ResultSet
                insertRecipeResultSet(recipeDTO);
            } else if (cmd.contains("from view_productbatch")) {
                ProductBatchDTO productBatchDTO = new ProductBatchDTO(1, 0, 1,1);
                // Insert an productbatch to the ResultSet
                insertProductBatchResultSet(productBatchDTO);
            }
        }

        return resultSet;
    }

    @Override
    public int update(String cmd) throws DALException {
        cmd = cmd.toLowerCase();
        if (cmd.contains("update"))
            this.updated = true;
        else if (cmd.contains("delete"))
            this.deleted = true;
        else if (cmd.contains("insert"))
            this.inserted = true;
        return 0;
    }

    @Override
    public void close() throws DALException {
        // Do nothing...
    }

    private void insertUserResultSet(UserDTO user) throws DALException {
        try {
            mockery.checking(new Expectations() {{
                allowing(resultSet).getInt("user_id");           will(returnValue(user.getUserId()));
                allowing(resultSet).getString("user_firstname"); will(returnValue(user.getFirstname()));
                allowing(resultSet).getString("user_lastname");  will(returnValue(user.getLastname()));
                allowing(resultSet).getString("initials");           will(returnValue(user.getInitials()));
                allowing(resultSet).getString("password");           will(returnValue(user.getPassword()));
                allowing(resultSet).getBoolean("is_active");         will(returnValue(user.isActive()));
            }});
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    private void insertIngredientBatchResultSet(IngredientBatchDTO ingredientBatch) throws DALException {
        try {
            mockery.checking(new Expectations() {{
                allowing(resultSet).getInt("ingredientbatch_id"); will(returnValue(1));
                allowing(resultSet).getInt("ingredient_id");      will(returnValue(2));
                allowing(resultSet).getDouble("amount");          will(returnValue(3.4));
            }});
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    private void insertIngredientResultSet(IngredientDTO ingredient) throws DALException {
        try {
            mockery.checking(new Expectations() {{
                allowing(resultSet).getInt("ingredient_id");      will(returnValue(ingredient.getIngredientId()));
                allowing(resultSet).getString("ingredient_name"); will(returnValue(ingredient.getIngredientName()));
                allowing(resultSet).getString("supplier");        will(returnValue(ingredient.getSupplier()));
            }});
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    private void insertProductBatchComponentResultSet(ProductBatchComponentDTO productBatchComponent) throws DALException {
        try {
            mockery.checking(new Expectations() {{
                allowing(resultSet).getInt("productbatch_id");    will(returnValue(productBatchComponent.getProductbatchId()));
                allowing(resultSet).getInt("ingredientbatch_id"); will(returnValue(productBatchComponent.getIngredientbatchId()));
                allowing(resultSet).getDouble("tare");            will(returnValue(productBatchComponent.getTare()));
                allowing(resultSet).getDouble("net_weight");      will(returnValue(productBatchComponent.getNetWeight()));
            }});
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    private void insertRecipeResultSet(RecipeDTO recipe) throws DALException {
        try {
            mockery.checking(new Expectations() {{
                allowing(resultSet).getInt("recipe_id");        will(returnValue((recipe.getRecipeId())));
                allowing(resultSet).getString("recipe_name");   will(returnValue((recipe.getRecipeName())));
            }});
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    private void insertRecipeComponentResultSet(RecipeComponentDTO recipeComponent) throws DALException{
        try {
            mockery.checking(new Expectations() {{
                allowing(resultSet).getInt("recipe_id");               will(returnValue(recipeComponent.getRecipeId()));
                allowing(resultSet).getInt("ingredient_id");           will(returnValue(recipeComponent.getIngredientId()));
                allowing(resultSet).getDouble("nominated_net_weight"); will(returnValue(recipeComponent.getNominatedNetWeight()));
                allowing(resultSet).getDouble("tolerance");            will(returnValue(recipeComponent.getTolerance()));
            }});
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    private void insertProductBatchResultSet(ProductBatchDTO productBatch) throws DALException {
        try {
            mockery.checking(new Expectations() {{
                allowing(resultSet).getInt("productbatch_id");  will(returnValue(productBatch.getProductbatchId()));
                allowing(resultSet).getInt("status");           will(returnValue(productBatch.getStatus()));
                allowing(resultSet).getInt("recipe_id");        will(returnValue(productBatch.getRecipeId()));
            }});
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    public boolean isSelected() { return selected; }
    public boolean isInserted() { return inserted; }
    public boolean isUpdated() { return updated; }
    public boolean isDeleted() { return deleted; }
}
