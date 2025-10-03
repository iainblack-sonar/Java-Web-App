package demo.security.servlet;

import demo.security.util.InsecureUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/vulnerable")
public class VulnerableServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Vulnerable to XSS
            InsecureUtils.displayUserInput(request, response);
            
            // Path Traversal
            String fileName = request.getParameter("file");
            String content = InsecureUtils.readFile(fileName);
            response.getWriter().write(content);
            
            // XXE Processing
            String xml = request.getParameter("xml");
            InsecureUtils.parseXML(xml);
            
            // Unsafe Deserialization
            String serializedData = request.getParameter("data");
            Object obj = InsecureUtils.deserialize(serializedData);
            
            // Weak Password Hash
            String password = request.getParameter("password");
            String hashedPassword = InsecureUtils.weakHash(password);
            response.getWriter().write(hashedPassword);
            
            // Using Hard-coded Credentials
            InsecureUtils.connectToDatabase();
            
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
