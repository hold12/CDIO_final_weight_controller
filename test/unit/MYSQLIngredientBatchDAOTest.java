package unit;

import dao.IngredientBatchDAO;
import dto.IngredientBatchDTO;
import jdbclib.IConnector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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

}