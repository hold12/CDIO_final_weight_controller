package dao;

import dto.RecipeDTO;
import jdbclib.DALException;
import jdbclib.IConnector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeDAO implements IRecipeDAO {
    private IConnector connector;

    public RecipeDAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public RecipeDTO getRecipe(int recipeId) throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "recipe.select.where.id",
                Integer.toString(recipeId)
        ));

        try {
            if (!rs.first())
                throw new DALException("The recipe " + recipeId + " does not exist.");
            return new RecipeDTO(
                    rs.getInt("recipe_id"),
                    rs.getString("recipe_name")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

}
