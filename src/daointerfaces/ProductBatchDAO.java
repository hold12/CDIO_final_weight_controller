package daointerfaces;

import dto.ProductBatchDTO;
import jdbclib.DALException;

import java.util.List;

public interface ProductBatchDAO {
    ProductBatchDTO getProductBatch(int productBatchId) throws DALException;

    List<ProductBatchDTO> getProductBatchList() throws DALException;

    void createProductBatch(ProductBatchDTO productBatch) throws DALException;

    void updateProductBatch(ProductBatchDTO productBatch) throws DALException;

    void deleteProductBatch(ProductBatchDTO productBatch) throws DALException;
}