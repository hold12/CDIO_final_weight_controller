package daoimpl;

import jdbclib.*;
import daointerfaces.IngredientBatchDAO;
import dto.IngredientBatchDTO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MYSQLIngredientBatchDAO implements IngredientBatchDAO {
    private IConnector connector;

    public MYSQLIngredientBatchDAO(IConnector connector) {
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
    public List<IngredientBatchDTO> getIngredientBatchList() throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "ingredientbatch.select.all"
        ));

        List<IngredientBatchDTO> list = new LinkedList<>();

        try {
            while (rs.next()) {
                list.add(new IngredientBatchDTO(
                        rs.getInt("ingredientbatch_id"),
                        rs.getInt("ingredient_id"),
                        rs.getDouble("amount")
                ));
            }
        } catch (SQLException e) {
            throw new DALException(e);
        }

        return list;
    }

    @Override
    public List<IngredientBatchDTO> getIngredientBatchList(int ingredientId) throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "ingredientbatch.select.where.ingredientid",
                Integer.toString(ingredientId)
        ));

        List<IngredientBatchDTO> list = new LinkedList<>();

        try {
            while (rs.next()) {
                list.add(new IngredientBatchDTO(
                        rs.getInt("ingredientbatch_id"),
                        rs.getInt("ingredient_id"),
                        rs.getDouble("amount")
                ));
            }
        } catch (SQLException e) {
            throw new DALException(e);
        }

        return list;
    }

    @Override
    public void createIngredientBatch(IngredientBatchDTO ingredientBatch) throws DALException {
        connector.update(Queries.getFormatted(
                "ingredientbatch.insert",
                Integer.toString(ingredientBatch.getIngredientId()),
                Double.toString(ingredientBatch.getAmount())
        ));
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

    @Override
    public void deleteIngredientBatch(IngredientBatchDTO ingredientBatch) throws DALException {
        throw new NotImplementedException();
    }
}
