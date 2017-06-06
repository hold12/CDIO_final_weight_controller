package dao;

import java.util.List;
import jdbclib.DALException;

import dto.RecipeComponentDTO;

public interface IRecipeComponentDAO {
	RecipeComponentDTO getRecipeComponent(int recipeId, int ingredientId) throws DALException;
	List<RecipeComponentDTO> getRecipeComponentList(int recipeId) throws DALException;
	List<RecipeComponentDTO> getRecipeComponentList() throws DALException;
}
