package demo.security.servlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Integration tests for HomeServlet
 * These tests focus on servlet behavior and HTTP interactions
 */
public class HomeServletIT {

    private HomeServlet homeServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setUp() throws Exception {
        homeServlet = new HomeServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testDoGetIntegration() throws Exception {
        // Setup request parameters
        when(request.getParameter("name")).thenReturn("TestUser");
        
        // Execute the servlet method
        homeServlet.doGet(request, response);
        
        // Verify response setup
        verify(response).setContentType("text/html");
        verify(response).getWriter();
        
        // Verify content was written
        printWriter.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("TestUser"), "Output should contain the user name");
        assertTrue(output.contains("Welcome"), "Output should contain welcome message");
    }

    @Test
    public void testDoPostIntegration() throws Exception {
        // Execute the servlet method
        homeServlet.doPost(request, response);
        
        // Verify that doGet was called (since doPost delegates to doGet)
        verify(response).setContentType("text/html");
        verify(response).getWriter();
    }

    @Test
    public void testServletInstantiationAndInheritance() {
        // Test that servlet is properly instantiated and inherits from HttpServlet
        assertNotNull(homeServlet);
        assertTrue(homeServlet instanceof javax.servlet.http.HttpServlet);
        assertEquals("HomeServlet", homeServlet.getClass().getSimpleName());
    }
}