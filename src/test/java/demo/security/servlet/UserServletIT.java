package demo.security.servlet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import demo.security.util.SessionHeader;

/**
 * Integration tests for UserServlet
 * These tests focus on servlet behavior and session management
 */
public class UserServletIT {

    private UserServlet userServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setUp() throws Exception {
        userServlet = new UserServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoGetWithValidSession() throws Exception {
        // Setup session with valid user
        when(session.getAttribute("username")).thenReturn("testuser");
        when(session.getAttribute("sessionId")).thenReturn("session123");
        
        // Execute the servlet method
        userServlet.doGet(request, response);
        
        // Verify response setup
        verify(response).setContentType("text/html");
        verify(response).getWriter();
        
        // Verify session was accessed
        verify(session).getAttribute("username");
        verify(session).getAttribute("sessionId");
    }

    @Test
    public void testDoGetWithInvalidSession() throws Exception {
        // Setup session with null values
        when(session.getAttribute("username")).thenReturn(null);
        when(session.getAttribute("sessionId")).thenReturn(null);
        
        // Execute the servlet method
        userServlet.doGet(request, response);
        
        // Verify response setup
        verify(response).setContentType("text/html");
        verify(response).getWriter();
        
        // Verify session was accessed
        verify(session).getAttribute("username");
        verify(session).getAttribute("sessionId");
    }

    @Test
    public void testSessionHeaderCreationIntegration() {
        // Test SessionHeader creation and usage (since getSessionHeader is private)
        SessionHeader header = new SessionHeader("integrationUser", "integration123");
        
        // Verify the result
        assertNotNull(header);
        assertEquals("integrationUser", header.getUsername());
        assertEquals("integration123", header.getSessionId());
        
        // Test setters
        header.setUsername("updatedUser");
        header.setSessionId("updatedSession");
        assertEquals("updatedUser", header.getUsername());
        assertEquals("updatedSession", header.getSessionId());
    }

    @Test
    public void testDoPostIntegration() throws Exception {
        // Setup request parameters
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("action")).thenReturn("login");
        
        // Execute the servlet method
        userServlet.doPost(request, response);
        
        // Verify response setup
        verify(response).setContentType("text/html");
        verify(response).getWriter();
        
        // Verify parameters were accessed
        verify(request).getParameter("username");
        verify(request).getParameter("action");
    }
}