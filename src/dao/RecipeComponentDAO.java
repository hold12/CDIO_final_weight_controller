package dao;

import dto.RecipeComponentDTO;
import jdbclib.DALException;
import jdbclib.IConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RecipeComponentDAO implements IRecipeComponentDAO {
    private IConnector connector;

    public RecipeComponentDAO(IConnector connector) {
        this.connector = connector;
    }
    @Override
    public RecipeComponentDTO getRecipeComponent(int recipeId, int ingredientId) throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "recipecomponent.select.where.id",
                Integer.toString(recipeId),
                Integer.toString(ingredientId)
        ));

        try {
            if (!rs.first())
                throw new DALException("The recipecomponent " + recipeId + " " + ingredientId + " does not exist.");
            return new RecipeComponentDTO(
                    rs.getInt("recipe_id"),
                    rs.getInt("ingredient_id"),
                    rs.getDouble("nominated_net_weight"),
                    rs.getDouble("tolerance")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    @Override
    public List<RecipeComponentDTO> getRecipeComponentList(int recipeId) throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "recipecomponent.select.where.recipeid",
                Integer.toString(recipeId)
        ));

        List<RecipeComponentDTO> list = new LinkedList<>();

        try {
            while (rs.next()) {
                list.add(new RecipeComponentDTO(
                        rs.getInt("recipe_id"),
                        rs.getInt("ingredient_id"),
                        rs.getDouble("nominated_net_weight"),
                        rs.getDouble("tolerance")
                ));
            }
        } catch (SQLException e) {
            throw new DALException(e);
        }

        return list;
    }

}
