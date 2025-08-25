package demo.security.servlet;

import demo.security.util.MoreIssues;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/more-issues")
public class MoreIssuesServlet extends HttpServlet {
    
    // Non-final static field
    public static String ADMIN_ROLE = "ADMIN";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        // Predictable token generation
        String token = MoreIssues.generateToken();
        response.setHeader("Token", token);
        
        // Command injection vulnerability
        String command = request.getParameter("command");
        MoreIssues.executeCommand(command);
        
        // Email validation with weak regex
        String email = request.getParameter("email");
        if (!MoreIssues.validateEmail(email)) {
            response.sendError(400, "Invalid email");
            return;
        }
        
        // Resource leak potential
        String socketData = MoreIssues.readFromSocket("localhost", 8080);
        if (socketData == null) {
            // Null check but no proper handling
            return;
        }
        
        // Session fixation vulnerability
        MoreIssues.handleSession(request);
        
        // Silent failure
        MoreIssues.silentFailure();
        
        // Accessing mutable static field
        for (String data : MoreIssues.SENSITIVE_DATA) {
            // Direct usage of sensitive data without proper handling
            response.getWriter().println(data);
        }
        
        // Complex conditions
        int result = MoreIssues.complexConditions(1, 2, 3);
        response.getWriter().println("Result: " + result);
        
        // Magic numbers in business logic
        double price = Double.parseDouble(request.getParameter("price"));
        double discount = MoreIssues.calculateDiscount(price);
        response.getWriter().println("Discount: " + discount);
    }
    
    // Duplicate code from doGet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String command = request.getParameter("command");
        MoreIssues.executeCommand(command);
        
        String email = request.getParameter("email");
        if (!MoreIssues.validateEmail(email)) {
            response.sendError(400, "Invalid email");
            return;
        }
    }
}
