package unit;

import jdbclib.IConnector;
import dao.IngredientBatchDAO;
import dto.IngredientBatchDTO;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.*;

public class MYSQLIngredientBatchDAOTest {
	private final IngredientBatchDTO testIngredientBatch = new IngredientBatchDTO(1, 2, 3.4);
    private IConnector connector;
    private IngredientBatchDAO ingredientBatchDAO;

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        ingredientBatchDAO = new IngredientBatchDAO(connector);
    }

    @Test
    public void getIngredientBatch() throws Exception {
        final IngredientBatchDTO ibActual = ingredientBatchDAO.getIngredientBatch(1);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(testIngredientBatch.equals(ibActual));
    }

    @Test
    public void createIngredientBatch() throws Exception {
        ingredientBatchDAO.createIngredientBatch(testIngredientBatch);
        assertTrue(((TestConnector) connector).isInserted());
    }

    @Test
    public void updateIngredientBatch() throws Exception {
        final IngredientBatchDTO newIngredientBatch = new IngredientBatchDTO(1, 1, 3.4);
        ingredientBatchDAO.updateIngredientBatch(newIngredientBatch);
        assertTrue(((TestConnector) connector).isUpdated());
    }

    @Test(expected = NotImplementedException.class)
    public void deleteIngredientBatch() throws Exception {
        ingredientBatchDAO.deleteIngredientBatch(testIngredientBatch);
    }

}