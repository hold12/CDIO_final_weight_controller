package db.dao;

import db.dto.RecipeDTO;
import jdbclib.DALException;

public interface IRecipeDAO {
	RecipeDTO getRecipe(int recipeId) throws DALException;
}
