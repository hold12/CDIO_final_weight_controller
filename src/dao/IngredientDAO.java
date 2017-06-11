package dao;

import dto.IngredientDTO;
import jdbclib.DALException;
import jdbclib.IConnector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientDAO implements IIngredientDAO {
    private IConnector connector;

    public IngredientDAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public IngredientDTO getIngredient(int ingredientId) throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "ingredient.select.where.id",
                Integer.toString(ingredientId)
        ));

        try {
            if (!rs.first()) throw new DALException("The ingredient " + ingredientId + " does not exist.");
            return new IngredientDTO(
                    rs.getInt("ingredient_id"),
                    rs.getString("ingredient_name")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }
}
