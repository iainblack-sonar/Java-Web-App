package demo.security.servlet;

import demo.security.util.DBUtils;
import demo.security.util.SessionHeader;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private DBUtils dbUtils;

    private UserServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        servlet = new UserServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoGet_ValidUser() throws Exception {
        // Arrange
        String username = "testUser";
        when(request.getParameter("username")).thenReturn(username);
        when(dbUtils.findUsers(username)).thenReturn(Arrays.asList("user1", "user2"));
        
        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        String output = stringWriter.toString();
        assertTrue(output.contains("User user1"));
        assertTrue(output.contains("User user2"));
    }

    @Test(expected = RuntimeException.class)
    public void testDoGet_DBError() throws Exception {
        // Arrange
        when(request.getParameter("username")).thenReturn("error");
        when(dbUtils.findUsers("error")).thenThrow(new RuntimeException("DB Error"));

        // Act
        servlet.doGet(request, response);
    }

    @Test
    public void testDoGet_SqlInjection() throws Exception {
        // Demonstrate SQL Injection vulnerability
        String maliciousInput = "' OR '1'='1";
        when(request.getParameter("username")).thenReturn(maliciousInput);
        when(dbUtils.findUsers(maliciousInput)).thenReturn(Arrays.asList("all_users_exposed"));
        
        servlet.doGet(request, response);
        
        verify(response).setContentType("text/html");
        assertTrue(stringWriter.toString().contains("all_users_exposed"));
    }

    @Test
    public void testDoGet_XssVulnerability() throws Exception {
        // Demonstrate XSS vulnerability
        String xssPayload = "<script>alert('xss')</script>";
        when(request.getParameter("username")).thenReturn(xssPayload);
        when(dbUtils.findUsers(xssPayload)).thenReturn(Arrays.asList("user1"));
        
        servlet.doGet(request, response);
        
        verify(response).setContentType("text/html");
        assertTrue(stringWriter.toString().contains(xssPayload));
    }

    @Test
    public void testDoGet_PathTraversal() throws Exception {
        // Demonstrate path traversal vulnerability
        String traversalPath = "../../../etc/passwd";
        when(request.getParameter("username")).thenReturn(traversalPath);
        when(dbUtils.findUsers(traversalPath)).thenReturn(Arrays.asList("sensitive_data"));
        
        servlet.doGet(request, response);
        
        verify(response).setContentType("text/html");
        assertTrue(stringWriter.toString().contains("sensitive_data"));
    }

    @Test
    public void testDoPost_ValidSessionHeader() throws Exception {
        // Arrange
        SessionHeader sessionHeader = new SessionHeader("testUser", "session123");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(sessionHeader);
        String encodedHeader = Base64.encodeBase64String(baos.toByteArray());
        
        when(request.getHeader("Session-Auth")).thenReturn(encodedHeader);
        when(dbUtils.findUsers("testUser")).thenReturn(Arrays.asList("user1"));

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).setContentType("text/html");
        assertTrue(stringWriter.toString().contains("User user1"));
    }

    @Test
    public void testDoPost_InvalidSessionHeader() throws Exception {
        // Arrange
        when(request.getHeader("Session-Auth")).thenReturn("invalid-base64");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response, never()).setContentType(anyString());
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testDoPost_NullSessionHeader() throws Exception {
        // Arrange
        when(request.getHeader("Session-Auth")).thenReturn(null);

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response, never()).setContentType(anyString());
        assertEquals("", stringWriter.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testDoPost_DBError() throws Exception {
        // Arrange
        SessionHeader sessionHeader = new SessionHeader("error", "session123");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(sessionHeader);
        String encodedHeader = Base64.encodeBase64String(baos.toByteArray());
        
        when(request.getHeader("Session-Auth")).thenReturn(encodedHeader);
        when(dbUtils.findUsers("error")).thenThrow(new RuntimeException("DB Error"));

        // Act
        servlet.doPost(request, response);
    }

    @Test
    public void testDoPost_InsecureDeserialization() throws Exception {
        // Demonstrate insecure deserialization vulnerability
        String maliciousBase64 = Base64.encodeBase64String(new byte[]{-84, -19, 0, 5, 115, 114, 0, 0}); // Malicious serialized object
        when(request.getHeader("Session-Auth")).thenReturn(maliciousBase64);
        
        servlet.doPost(request, response);
        
        // No exception means the malicious object was processed
        verify(response, never()).setContentType(anyString());
    }

    @Test
    public void testDoPost_HeaderInjection() throws Exception {
        // Demonstrate header injection vulnerability
        SessionHeader sessionHeader = new SessionHeader("testUser\r\nCustom-Header: injected", "session123");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(sessionHeader);
        String encodedHeader = Base64.encodeBase64String(baos.toByteArray());
        
        when(request.getHeader("Session-Auth")).thenReturn(encodedHeader);
        when(dbUtils.findUsers(contains("testUser"))).thenReturn(Arrays.asList("user1"));
        
        servlet.doPost(request, response);
        
        verify(response).setContentType("text/html");
    }

    @Test
    public void testDoPost_LogInjection() throws Exception {
        // Demonstrate log injection vulnerability
        SessionHeader sessionHeader = new SessionHeader("testUser%0d%0aINJECTED LOG", "session123");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(sessionHeader);
        String encodedHeader = Base64.encodeBase64String(baos.toByteArray());
        
        when(request.getHeader("Session-Auth")).thenReturn(encodedHeader);
        
        servlet.doPost(request, response);
        
        // Log injection would be visible in server logs
        verify(response, never()).setContentType(anyString());
    }
}
