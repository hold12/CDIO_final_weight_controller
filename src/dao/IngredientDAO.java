package dao;

import jdbclib.*;
import dto.IngredientDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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
                    rs.getString("ingredient_name"),
                    rs.getString("supplier")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    @Override
    public List<IngredientDTO> getIngredientList() throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "ingredient.select.all"
        ));

        List<IngredientDTO> list = new LinkedList<>();

        try {
            while (rs.next()) {
                list.add(new IngredientDTO(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getString("supplier")
                ));
            }
        } catch (SQLException e) {
            throw new DALException(e);
        }

        return list;
    }

}
