package daoimpl;

import jdbclib.*;
import daointerfaces.RecipeComponentDAO;
import dto.RecipeComponentDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MYSQLRecipeComponentDAO implements RecipeComponentDAO {
    private IConnector connector;

    public MYSQLRecipeComponentDAO(IConnector connector) {
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

    @Override
    public List<RecipeComponentDTO> getRecipeComponentList() throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "recipecomponent.select.all"
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

    @Override
    public void createRecipeComponent(RecipeComponentDTO recipeComponent) throws DALException {
        connector.update(Queries.getFormatted(
                "recipecomponent.insert",
                Integer.toString(recipeComponent.getIngredientId()),
                Integer.toString(recipeComponent.getRecipeId()),
                Double.toString(recipeComponent.getNominatedNetWeight()),
                Double.toString(recipeComponent.getTolerance())
        ));
    }

    @Override
    public void updateRecipeComponent(RecipeComponentDTO recipeComponent) throws DALException {
        connector.update(Queries.getFormatted(
                "recipecomponent.update",
                Integer.toString(recipeComponent.getIngredientId()),
                Integer.toString(recipeComponent.getRecipeId()),
                Double.toString(recipeComponent.getNominatedNetWeight()),
                Double.toString(recipeComponent.getTolerance())
        ));
    }

    @Override
    public void deleteRecipeComponent(RecipeComponentDTO recipeComponent) throws DALException {
        connector.update(Queries.getFormatted(
                "recipecomponent.delete",
                Integer.toString(recipeComponent.getIngredientId()),
                Integer.toString(recipeComponent.getRecipeId())
        ));
    }
}
