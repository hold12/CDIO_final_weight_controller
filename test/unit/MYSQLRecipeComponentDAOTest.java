package unit;

import static org.junit.Assert.*;
import jdbclib.IConnector;
import dto.RecipeComponentDTO;
import daoimpl.MYSQLRecipeComponentDAO;
import org.junit.Before;
import org.junit.Test;

public class MYSQLRecipeComponentDAOTest {
    private IConnector connector;
    private MYSQLRecipeComponentDAO recipeComponentDAO;
    private final RecipeComponentDTO testRecipeComponent = new RecipeComponentDTO(1, 2, 1.2,  1.2);



    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        recipeComponentDAO = new MYSQLRecipeComponentDAO(connector);

    }

    @Test
    public void getRecipeComponent() throws Exception {
        RecipeComponentDTO ibExpected = testRecipeComponent;
        RecipeComponentDTO ibActual = recipeComponentDAO.getRecipeComponent(1, 2);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(ibExpected.equals(ibActual));

    }

    @Test
    public void createRecipeComponent() throws Exception {
        recipeComponentDAO.createRecipeComponent(testRecipeComponent);
        assertTrue(((TestConnector) connector).isInserted());

    }

    @Test
    public void updateRecipeComponent() throws Exception {
        RecipeComponentDTO newRecipeComponent = new RecipeComponentDTO(1, 2, 1.4,  1.4);
        recipeComponentDAO.updateRecipeComponent(newRecipeComponent);
        assertTrue(((TestConnector) connector).isUpdated());


    }

    @Test
    public void deleteRecipeComponent() throws Exception {
        recipeComponentDAO.deleteRecipeComponent(testRecipeComponent);

    }

}