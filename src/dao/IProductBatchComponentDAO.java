package dao;

import java.util.List;
import jdbclib.DALException;

import dto.ProductBatchComponentDTO;

public interface IProductBatchComponentDAO {
	ProductBatchComponentDTO getProductBatchComponent(int productBatchId, int ingredientBatchId) throws DALException;
	List<ProductBatchComponentDTO> getProductBatchComponentList(int productBatchId) throws DALException;
	List<ProductBatchComponentDTO> getProductBatchComponentList() throws DALException;
	void createProductBatchComponent(ProductBatchComponentDTO productBatchComponent) throws DALException;
	void updateProductBatchComponent(ProductBatchComponentDTO productBatchComponent) throws DALException;
	void deleteProductBatchComponent(ProductBatchComponentDTO productBatchComponent) throws DALException;
}

