package demo.security.servlet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit tests for VulnerableServlet
 */
class VulnerableServletTest {

    @Test
    void testServletInstantiation() {
        VulnerableServlet servlet = new VulnerableServlet();
        assertNotNull(servlet);
        assertTrue(servlet instanceof VulnerableServlet);
    }

    @Test
    void testServletClassProperties() {
        VulnerableServlet servlet = new VulnerableServlet();
        String className = servlet.getClass().getSimpleName();
        assertEquals("VulnerableServlet", className);
    }
}
