package demo.security.servlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HomeServlet
 */
public class HomeServletTest {

    private HomeServlet homeServlet;

    @BeforeEach
    public void setUp() {
        homeServlet = new HomeServlet();
    }

    @Test
    public void testHomeServletInstantiation() {
        assertNotNull(homeServlet);
        assertTrue(homeServlet instanceof HomeServlet);
    }

    @Test
    public void testHomeServletClassMethods() {
        // Test basic class properties
        String className = homeServlet.getClass().getSimpleName();
        assertEquals("HomeServlet", className);
        
        // Test that it's a servlet
        assertTrue(homeServlet instanceof javax.servlet.http.HttpServlet);
    }
}
