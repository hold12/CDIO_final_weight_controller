package db.dao;

import jdbclib.DALException;

import db.dto.UserDTO;

public interface IUserDAO {
	UserDTO getUser(int userId) throws DALException;
}
