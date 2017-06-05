package daointerfaces;

import java.util.List;
import jdbclib.DALException;

import dto.RecipeComponentDTO;

public interface RecipeComponentDAO {
	RecipeComponentDTO getRecipeComponent(int recipeId, int ingredientId) throws DALException;
	List<RecipeComponentDTO> getRecipeComponentList(int recipeId) throws DALException;
	List<RecipeComponentDTO> getRecipeComponentList() throws DALException;
		void createRecipeComponent(RecipeComponentDTO recipeComponent) throws DALException;
	void updateRecipeComponent(RecipeComponentDTO recipeComponent) throws DALException;
	void deleteRecipeComponent(RecipeComponentDTO recipeComponent) throws DALException;
}
