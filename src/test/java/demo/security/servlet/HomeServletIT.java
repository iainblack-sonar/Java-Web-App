package demo.security.servlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration tests for HomeServlet
 */
class HomeServletIT {

    private HomeServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new HomeServlet();
    }

    @Test
    void testServletInstantiation() {
        assertNotNull(servlet);
        assertTrue(servlet instanceof HomeServlet);
    }

    @Test
    void testServletClassProperties() {
        String className = servlet.getClass().getSimpleName();
        assertEquals("HomeServlet", className);
        assertTrue(servlet instanceof javax.servlet.http.HttpServlet);
    }
}