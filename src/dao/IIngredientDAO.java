package dao;

import dto.IngredientDTO;
import jdbclib.DALException;

public interface IIngredientDAO {
	IngredientDTO getIngredient(int ingredientId) throws DALException;
}
