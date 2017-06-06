package dao;

import java.util.List;
import jdbclib.DALException;

import dto.RecipeDTO;

public interface IRecipeDAO {
	RecipeDTO getRecipe(int recipeId) throws DALException;
	List<RecipeDTO> getRecipeList() throws DALException;
}
