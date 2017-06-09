package unit;

import dao.UserDAO;
import dto.UserDTO;
import jdbclib.IConnector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MySQLUserDAOTest {
	private final UserDTO testUser = new UserDTO(1, "John", "Doe");
    private IConnector connector;
    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        connector = new TestConnector();
        userDAO = new UserDAO(connector);
    }

    @Test
    public void getUser() throws Exception {
		final UserDTO userActual = userDAO.getUser(1);
        assertTrue(((TestConnector) connector).isSelected());
        assertTrue(testUser.equals(userActual));
    }

}