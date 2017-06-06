package dao;

import jdbclib.DALException;

import dto.UserDTO;

public interface IUserDAO {
	UserDTO getUser(int userId) throws DALException;
}
