package db.dao;

import db.dto.ProductBatchComponentDTO;
import jdbclib.DALException;

public interface IProductBatchComponentDAO {
	ProductBatchComponentDTO getProductBatchComponent(int productBatchId, int ingredientBatchId) throws DALException;
	void createProductBatchComponent(ProductBatchComponentDTO productBatchComponent) throws DALException;
}

