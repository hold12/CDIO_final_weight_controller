package dao;

import jdbclib.*;
import dto.RecipeDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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

    @Override
    public List<RecipeDTO> getRecipeList() throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "recipe.select.all"
        ));

        List<RecipeDTO> list = new LinkedList<>();

        try {
            while (rs.next()) {
                list.add(new RecipeDTO(
                        rs.getInt("recipe_id"),
                        rs.getString("recipe_name")
                ));
            }
        } catch (SQLException e) {
            throw new DALException(e);
        }

        return list;
    }
}
