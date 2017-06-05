package daointerfaces;

import java.util.List;
import jdbclib.DALException;

import dto.UserDTO;

public interface UserDAO {
	UserDTO getUser(int userId) throws DALException;
	List<UserDTO> getUserList() throws DALException;
	void createUser(UserDTO user) throws DALException;
	void updateUser(UserDTO user) throws DALException;
	void deleteUser(UserDTO user) throws DALException;
}
