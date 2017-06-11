package dao;

import dto.IngredientBatchDTO;
import jdbclib.DALException;

public interface IIngredientBatchDAO {
	IngredientBatchDTO getIngredientBatch(int ingredientBatchId) throws DALException;
	void updateIngredientBatch(IngredientBatchDTO ingredientBatch) throws DALException;
}

