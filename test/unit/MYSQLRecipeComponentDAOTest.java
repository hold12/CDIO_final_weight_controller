package unit;

import jdbclib.IConnector;
import dto.RecipeComponentDTO;
import dao.RecipeComponentDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MYSQLRecipeComponentDAOTest {
	private final RecipeComponentDTO testRecipeComponent = new RecipeComponentDTO(1, 2, 1.2,  1.2);
    private IConnector connector;
    private RecipeComponentDAO recipeComponentDAO;



    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        recipeComponentDAO = new RecipeComponentDAO(connector);

    }

    @Test
    public void getRecipeComponent() throws Exception {
		final RecipeComponentDTO ibActual = recipeComponentDAO.getRecipeComponent(1, 2);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(testRecipeComponent.equals(ibActual));

    }

}