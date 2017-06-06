package dao;

import dto.ProductBatchDTO;
import jdbclib.DALException;

import java.util.List;

public interface IProductBatchDAO {
    ProductBatchDTO getProductBatch(int productBatchId) throws DALException;
    List<ProductBatchDTO> getProductBatchList() throws DALException;
    void updateProductBatch(ProductBatchDTO productBatch) throws DALException;
}