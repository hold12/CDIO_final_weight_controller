package dao;

import java.util.List;
import jdbclib.DALException;

import dto.IngredientBatchDTO;

public interface IIngredientBatchDAO {
	IngredientBatchDTO getIngredientBatch(int ingredientBatchId) throws DALException;
	List<IngredientBatchDTO> getIngredientBatchList() throws DALException;
	List<IngredientBatchDTO> getIngredientBatchList(int ingredientId) throws DALException;
}

