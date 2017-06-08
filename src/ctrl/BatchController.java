package ctrl;

import dao.ProductBatchComponentDAO;
import dao.ProductBatchDAO;
import dto.ProductBatchComponentDTO;
import dto.ProductBatchDTO;
import jdbclib.DALException;
import jdbclib.IConnector;
import lang.Lang;

import java.io.IOException;
import java.util.List;

public class BatchController {
    private IConnector connector;
    private IWeightController weightCtrl;

    public BatchController(IConnector connector, IWeightController weightClient) {
        this.connector = connector;
        this.weightCtrl = weightClient;
    }

    public boolean batch(int batchId){
        ProductBatchComponentDAO productBatchComponentDAO = new ProductBatchComponentDAO(connector);
        List<ProductBatchComponentDTO> components;

        try {
            components = productBatchComponentDAO.getProductBatchComponentList(batchId);
        } catch (DALException e) {
            System.err.println(Lang.msg("errNotAuthenticated"));
            try {
                weightCtrl.rm208(Lang.msg("err"), Lang.msg("errNoComponents"), IWeightController.KeyPadState.LOWER_CHARS);
            } catch (IOException e2) {
                System.err.println(e2.getMessage());
            }
            return false;
        }

        for (ProductBatchComponentDTO comp : components){
            BatchComponentController compCtrl = new BatchComponentController(connector, weightCtrl);
            if (!compCtrl.doWeighing(comp)){
                return false;
            }
        }
        return true;
    }

}
