package unit;

import jdbclib.IConnector;
import daoimpl.MYSQLRecipeDAO;
import dto.RecipeDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MYSQLRecipeDAOTest {
    private final RecipeDTO testRecipe = new RecipeDTO(1, "pizza");
    private IConnector connector;
    private MYSQLRecipeDAO recipeDAO;

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        recipeDAO = new MYSQLRecipeDAO(connector);
    }

    @Test
    public void getRecipe() throws Exception {
        RecipeDTO reExpected = testRecipe;
        RecipeDTO reActual = recipeDAO.getRecipe(1);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(reExpected.equals(reActual));
    }

    @Test
    public void createRecipe() throws Exception {
        recipeDAO.createRecipe(testRecipe);
        assertTrue(((TestConnector) connector).isInserted());
    }

    @Test
    public void updateRecipe() throws Exception {
        RecipeDTO newRecipe = new RecipeDTO(1, "Melt cheese");
        recipeDAO.updateRecipe(newRecipe);
        assertTrue(((TestConnector) connector).isUpdated());
    }

    @Test
    public void deleteRecipe() throws Exception {
        recipeDAO.deleteRecipe(testRecipe);
        assertTrue(((TestConnector) connector).isDeleted());
    }
}
