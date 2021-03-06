package unit;

import db.dao.ProductBatchDAO;
import db.dto.ProductBatchDTO;
import jdbclib.IConnector;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertTrue;

public class MYSQLProductBatchDAOTest {
    private final ProductBatchDTO testProductBatch = new ProductBatchDTO(1, new Timestamp(2017,6,9,11,22,0,0), new Timestamp(2017,6,9,12,22,0,0), 0, 1, 1);
    private IConnector connector;
    private ProductBatchDAO productBatchDAO;

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        productBatchDAO = new ProductBatchDAO(connector);
    }

    @Test
    public void getProductBatch() throws Exception {
        final ProductBatchDTO pbActual = productBatchDAO.getProductBatch(1);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(testProductBatch.equals(pbActual));
    }

    @Test
    public void updateProductBatch() throws Exception {
        final ProductBatchDTO newProductBatch = new ProductBatchDTO(1, new Timestamp(2017,6,9,11,22,0,0), new Timestamp(2017,6,9,12,22,0,0), 1, 1, 1);
        productBatchDAO.updateProductBatch(newProductBatch);
        assertTrue(((TestConnector) connector).isUpdated());
    }


}