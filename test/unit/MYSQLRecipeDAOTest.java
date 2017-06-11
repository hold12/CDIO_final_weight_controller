package unit;

import jdbclib.IConnector;
import db.dao.RecipeDAO;
import db.dto.RecipeDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MYSQLRecipeDAOTest {
    private final RecipeDTO testRecipe = new RecipeDTO(1, "pizza");
    private IConnector connector;
    private RecipeDAO recipeDAO;

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        recipeDAO = new RecipeDAO(connector);
    }

    @Test
    public void getRecipe() throws Exception {
		final RecipeDTO reActual = recipeDAO.getRecipe(1);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(testRecipe.equals(reActual));
    }

}
