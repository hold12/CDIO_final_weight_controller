package dao;

import dto.IngredientBatchDTO;
import jdbclib.DALException;
import jdbclib.IConnector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientBatchDAO implements IIngredientBatchDAO {
    private IConnector connector;

    public IngredientBatchDAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public IngredientBatchDTO getIngredientBatch(int ingredientBatchId) throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "ingredientbatch.select.where.id",
                Integer.toString(ingredientBatchId)
        ));

        try {
            if (!rs.first()) throw new DALException("The ingredientbatch " + ingredientBatchId + " does not exist.");
            return new IngredientBatchDTO(
                    rs.getInt("ingredientbatch_id"),
                    rs.getInt("ingredient_id"),
                    rs.getDouble("amount")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    @Override
    public void updateIngredientBatch(IngredientBatchDTO ingredientBatch) throws DALException {
        connector.update(Queries.getFormatted(
                "ingredientbatch.update",
                Integer.toString(ingredientBatch.getIngredientBatchId()),
                Integer.toString(ingredientBatch.getIngredientId()),
                Double.toString(ingredientBatch.getAmount())
        ));
    }

}
