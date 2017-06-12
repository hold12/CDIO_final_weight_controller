package unit;

import db.dao.IngredientDAO;
import db.dto.IngredientDTO;
import jdbclib.IConnector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MYSQLIngredientDAOTest {
    private final IngredientDTO testIngredient = new IngredientDTO(1, "tomato");
    private IConnector connector;
    private IngredientDAO ingredientDAO;

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        ingredientDAO = new IngredientDAO(connector);
    }

    @Test
    public void getIngredient() throws Exception {
        final IngredientDTO inActual = ingredientDAO.getIngredient(1);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(testIngredient.equals(inActual));
    }

}