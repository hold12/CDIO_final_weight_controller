package daoimpl;

import jdbclib.*;
import daointerfaces.ProductBatchDAO;
import dto.ProductBatchDTO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MYSQLProductBatchDAO implements ProductBatchDAO {
    private IConnector connector;

    public MYSQLProductBatchDAO(IConnector connector) {
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
                    rs.getInt("status"),
                    rs.getInt("recipe_id")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }

    @Override
    public List<ProductBatchDTO> getProductBatchList() throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "productbatch.select.all"
        ));

        List<ProductBatchDTO> list = new LinkedList<>();

        try {
            while (rs.next()) {
                list.add(new ProductBatchDTO(
                        rs.getInt("productbatch_id"),
                        rs.getInt("status"),
                        rs.getInt("recipe_id")
                ));
            }
        } catch (SQLException e) {
            throw new DALException(e);
        }

        return list;
    }

    @Override
    public void createProductBatch(ProductBatchDTO productBatch) throws DALException {
        connector.update(Queries.getFormatted(
                "productbatch.insert",
                Integer.toString(productBatch.getStatus()),
                Integer.toString(productBatch.getRecipeId())
        ));
    }

    @Override
    public void updateProductBatch(ProductBatchDTO productBatch) throws DALException {
        connector.update(Queries.getFormatted(
                "productbatch.update",
                Integer.toString(productBatch.getProductbatchId()),
                Integer.toString(productBatch.getStatus()),
                Integer.toString(productBatch.getRecipeId())
        ));
    }

    @Override
    public void deleteProductBatch(ProductBatchDTO productBatch) throws DALException {
        throw new NotImplementedException();
    }
}
