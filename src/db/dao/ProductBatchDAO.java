package db.dao;

import db.dto.ProductBatchDTO;
import jdbclib.DALException;
import jdbclib.IConnector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductBatchDAO implements IProductBatchDAO {
    private IConnector connector;

    public ProductBatchDAO(IConnector connector) {
        this.connector = connector;
    }

    @Override
    public ProductBatchDTO getProductBatch(int productbatchId) throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "productbatch.select.where.id",
                Integer.toString(productbatchId)
        ));

        try {
            if (!rs.first()) throw new DALException("The productbatch " + productbatchId + " does not exist.");
            return new ProductBatchDTO(
                    rs.getInt("productbatch_id"),
                    rs.getTimestamp("created_time"),
                    rs.getTimestamp("finished_time"),
                    rs.getInt("status"),
                    rs.getInt("recipe_id"),
                    rs.getInt("user_id")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    @Override
    public void updateProductBatch(ProductBatchDTO productBatch) throws DALException {
        //@TODO Do something here, so NULL can be passed to SQL
        //@TODO Describe in report what we have done here and why
        if(productBatch.getFinishedTime() == null) {
            connector.update(Queries.getFormatted(
                    "productbatch.update.null",
                    Integer.toString(productBatch.getProductbatchId()),
                    productBatch.getCreatedTime().toString(),
                    Integer.toString(productBatch.getStatus()),
                    Integer.toString(productBatch.getRecipeId()),
                    Integer.toString(productBatch.getUserId())
            ));
        } else {
            connector.update(Queries.getFormatted(
                    "productbatch.update",
                    Integer.toString(productBatch.getProductbatchId()),
                    productBatch.getCreatedTime().toString(),
                    productBatch.getFinishedTime().toString(),
                    Integer.toString(productBatch.getStatus()),
                    Integer.toString(productBatch.getRecipeId()),
                    Integer.toString(productBatch.getUserId())
            ));
        }
    }

}
