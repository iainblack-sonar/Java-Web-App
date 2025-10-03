package demo.security.servlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserServlet
 */
public class UserServletTest {

    private UserServlet userServlet;

    @BeforeEach
    public void setUp() {
        userServlet = new UserServlet();
    }

    @Test
    public void testUserServletInstantiation() {
        assertNotNull(userServlet);
        assertTrue(userServlet instanceof UserServlet);
    }

    @Test
    public void testUserServletClassMethods() {
        // Test basic class properties
        String className = userServlet.getClass().getSimpleName();
        assertEquals("UserServlet", className);
        
        // Test that it's a servlet
        assertTrue(userServlet instanceof javax.servlet.http.HttpServlet);
    }
}
