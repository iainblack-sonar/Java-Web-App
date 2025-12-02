package demo.security.servlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration tests for UserServlet
 */
class UserServletIT {

    private UserServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new UserServlet();
    }

    @Test
    void testServletInstantiation() {
        assertNotNull(servlet);
        assertTrue(servlet instanceof UserServlet);
    }

    @Test
    void testServletClassProperties() {
        String className = servlet.getClass().getSimpleName();
        assertEquals("UserServlet", className);
        assertTrue(servlet instanceof javax.servlet.http.HttpServlet);
    }
}