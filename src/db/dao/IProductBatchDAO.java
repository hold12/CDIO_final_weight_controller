package db.dao;

import db.dto.ProductBatchDTO;
import jdbclib.DALException;

public interface IProductBatchDAO {
    ProductBatchDTO getProductBatch(int productBatchId) throws DALException;
    void updateProductBatch(ProductBatchDTO productBatch) throws DALException;
}