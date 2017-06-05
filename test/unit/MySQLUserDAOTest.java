package unit;

import jdbclib.IConnector;
import dto.UserDTO;
import daoimpl.MySQLUserDAO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MySQLUserDAOTest {
	private final UserDTO testUser = new UserDTO(1, "John", "Doe", "JD", "p455w0rd!", false);
    private IConnector connector;
    private MySQLUserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        userDAO = new MySQLUserDAO(connector);
    }

    @Test
    public void getUser() throws Exception {
		final UserDTO userActual = userDAO.getUser(1);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(testUser.equals(userActual));
    }

    @Test
    public void createUser() throws Exception {
        userDAO.createUser(testUser);
        assertTrue(((TestConnector) connector).isInserted());
    }

    @Test
    public void updateUser() throws Exception {
		final UserDTO opNew = new UserDTO(1, "Jane", "Doe", "JD", "p455w0rd!", false);
        opNew.setUserFirstname("Jane");
        userDAO.updateUser(opNew);
        assertTrue(((TestConnector) connector).isUpdated());
    }

    @Test
    public void deleteUser() throws Exception {
        userDAO.deleteUser(testUser);
        assertTrue(((TestConnector) connector).isDeleted());
    }

}