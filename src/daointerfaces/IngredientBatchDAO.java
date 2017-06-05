package daointerfaces;

import java.util.List;
import jdbclib.DALException;

import dto.IngredientBatchDTO;

public interface IngredientBatchDAO {
	IngredientBatchDTO getIngredientBatch(int recipeId) throws DALException;
	List<IngredientBatchDTO> getIngredientBatchList() throws DALException;
	List<IngredientBatchDTO> getIngredientBatchList(int ingredientId) throws DALException;
	void createIngredientBatch(IngredientBatchDTO ingredientBatch) throws DALException;
	void updateIngredientBatch(IngredientBatchDTO ingredientBatch) throws DALException;
	void deleteIngredientBatch(IngredientBatchDTO ingredientBatch) throws DALException;
}

