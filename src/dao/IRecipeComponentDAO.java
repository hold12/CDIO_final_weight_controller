package dao;

import dto.RecipeComponentDTO;
import jdbclib.DALException;

import java.util.List;

public interface IRecipeComponentDAO {
	RecipeComponentDTO getRecipeComponent(int recipeId, int ingredientId) throws DALException;
	List<RecipeComponentDTO> getRecipeComponentList(int recipeId) throws DALException;
}
