package daoimpl;

import jdbclib.*;
import daointerfaces.UserDAO;
import dto.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserDAO implements UserDAO {
    private IConnector connector;

    public MySQLUserDAO(IConnector connector) {
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
                    rs.getString("user_firstname"),
                    rs.getString("user_lastname"),
                    rs.getString("initials"),
                    rs.getString("password"),
                    rs.getBoolean("is_active")
            );
        } catch (SQLException e) {
            throw new DALException(e);
        }

    }

    public void createUser(UserDTO user) throws DALException {
        connector.update(Queries.getFormatted(
                "user.insert",
                user.getUserFirstname(),
                user.getUserLastname(),
                user.getInitials(),
                user.getPassword()
        ));
    }

    public void updateUser(UserDTO user) throws DALException {
        int isActive = 0;

        if (user.isActive()) {
            isActive = 1;
        }

        connector.update(Queries.getFormatted(
                "user.update",
                Integer.toString(user.getUserId()),
                user.getUserFirstname(),
                user.getUserLastname(),
                user.getInitials(),
                user.getPassword(),
                Integer.toString(isActive)
        ));
    }

    public void deleteUser(UserDTO user) throws DALException {
        connector.update(Queries.getFormatted(
                "user.delete",
                Integer.toString(user.getUserId())
        ));
    }

    public List<UserDTO> getUserList() throws DALException {
        List<UserDTO> list = new ArrayList<UserDTO>();
        ResultSet rs = connector.query(
                Queries.getSQL("user.select.all")
        );

        try {
            while (rs.next()) {
                list.add(new UserDTO(
                        rs.getInt("user_id"),
                        rs.getString("user_firstname"),
                        rs.getString("user_lastname"),
                        rs.getString("initials"),
                        rs.getString("password"),
                        rs.getBoolean("is_active"))
                );
            }
        } catch (SQLException e) {
            throw new DALException(e);
        }
        return list;
    }
}