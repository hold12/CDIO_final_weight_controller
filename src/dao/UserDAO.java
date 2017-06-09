package dao;

import dto.UserDTO;
import jdbclib.DALException;
import jdbclib.IConnector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements IUserDAO {
    private IConnector connector;

    public UserDAO(IConnector connector) {
        this.connector = connector;
    }

    public UserDTO getUser(int userId) throws DALException {
        ResultSet rs = connector.query(Queries.getFormatted(
                "user.select.where.id", Integer.toString(userId)
        ));

        try {
            if (!rs.first()) throw new DALException("The user " + userId + " does not exist.");

            return new UserDTO(
                    rs.getInt("user_id"),
                    rs.getString("firstname"),
                    rs.getString("lastname")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }
    }
}