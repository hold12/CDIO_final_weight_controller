package unit;

import dao.ProductBatchComponentDAO;
import dto.ProductBatchComponentDTO;
import jdbclib.IConnector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MYSQLProductBatchComponentDAOTest {
    private final ProductBatchComponentDTO testProductBatchComponent = new ProductBatchComponentDTO(1, 1, 0.5, 10);
    private IConnector connector;
    private ProductBatchComponentDAO productBatchComponentDAO;

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        productBatchComponentDAO = new ProductBatchComponentDAO(connector);
    }

    @Test
    public void getProductBatchComponent() throws Exception {
        final ProductBatchComponentDTO pbcActual = productBatchComponentDAO.getProductBatchComponent(1, 1);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(testProductBatchComponent.equals(pbcActual));
    }

    @Test
    public void createProductBatchComponent() throws Exception {
        productBatchComponentDAO.createProductBatchComponent(testProductBatchComponent);
        assertTrue(((TestConnector) connector).isInserted());
    }

}
