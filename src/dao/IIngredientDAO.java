package dao;

import java.util.List;
import jdbclib.DALException;

import dto.IngredientDTO;

public interface IIngredientDAO {
	IngredientDTO getIngredient(int ingredientId) throws DALException;
	List<IngredientDTO> getIngredientList() throws DALException;
}
