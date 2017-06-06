package dao;

import jdbclib.*;
import dto.IngredientBatchDTO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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

}
