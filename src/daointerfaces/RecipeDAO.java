package daointerfaces;

import java.util.List;
import jdbclib.DALException;

import dto.RecipeDTO;

public interface RecipeDAO {
	RecipeDTO getRecipe(int recipeId) throws DALException;
	List<RecipeDTO> getRecipeList() throws DALException;
	void createRecipe(RecipeDTO recipe) throws DALException;
	void updateRecipe(RecipeDTO recipe) throws DALException;
	void deleteRecipe(RecipeDTO recipe) throws DALException;
}
