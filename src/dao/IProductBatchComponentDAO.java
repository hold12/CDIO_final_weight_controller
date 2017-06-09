package dao;

import dto.ProductBatchComponentDTO;
import jdbclib.DALException;

public interface IProductBatchComponentDAO {
	ProductBatchComponentDTO getProductBatchComponent(int productBatchId, int ingredientBatchId) throws DALException;
	void createProductBatchComponent(ProductBatchComponentDTO productBatchComponent) throws DALException;
	void updateProductBatchComponent(ProductBatchComponentDTO productBatchComponent) throws DALException;
	void deleteProductBatchComponent(ProductBatchComponentDTO productBatchComponent) throws DALException;
}

