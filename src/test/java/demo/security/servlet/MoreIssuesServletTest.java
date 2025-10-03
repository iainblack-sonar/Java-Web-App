package demo.security.servlet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit tests for MoreIssuesServlet
 */
class MoreIssuesServletTest {

    @Test
    void testServletInstantiation() {
        MoreIssuesServlet servlet = new MoreIssuesServlet();
        assertNotNull(servlet);
        assertTrue(servlet instanceof MoreIssuesServlet);
    }

    @Test
    void testServletClassProperties() {
        MoreIssuesServlet servlet = new MoreIssuesServlet();
        String className = servlet.getClass().getSimpleName();
        assertEquals("MoreIssuesServlet", className);
    }
}
