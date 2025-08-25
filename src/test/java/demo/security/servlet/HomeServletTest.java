package demo.security.servlet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HomeServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private HomeServlet servlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        servlet = new HomeServlet();
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoGet_ValidName() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("name")).thenReturn("John");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        assertTrue(stringWriter.toString().contains("Hello John"));
    }

    @Test
    public void testDoGet_NameWithSpaces() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("name")).thenReturn("  John Doe  ");

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setContentType("text/html");
        assertTrue(stringWriter.toString().contains("Hello John Doe"));
    }

    @Test(expected = NullPointerException.class)
    public void testDoGet_NullName() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("name")).thenReturn(null);

        // Act
        servlet.doGet(request, response);
    }

    @Test
    public void testDoPost_ShouldCallDoGet() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("name")).thenReturn("John");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).setContentType("text/html");
        assertTrue(stringWriter.toString().contains("Hello John"));
    }
}
