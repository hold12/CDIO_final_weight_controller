package daointerfaces;

import java.util.List;
import jdbclib.DALException;

import dto.IngredientDTO;

public interface IngredientDAO {
	IngredientDTO getIngredient(int ingredientId) throws DALException;
	List<IngredientDTO> getIngredientList() throws DALException;
	void createIngredient(IngredientDTO ingredient) throws DALException;
	void updateIngredient(IngredientDTO ingredient) throws DALException;
	void deleteIngredient(IngredientDTO ingredient) throws DALException;
}
