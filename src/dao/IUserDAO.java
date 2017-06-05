package dao;

import java.util.List;
import jdbclib.DALException;

import dto.UserDTO;

public interface IUserDAO {
	UserDTO getUser(int userId) throws DALException;
	List<UserDTO> getUserList() throws DALException;
	void createUser(UserDTO user) throws DALException;
	void updateUser(UserDTO user) throws DALException;
	void deleteUser(UserDTO user) throws DALException;
}
