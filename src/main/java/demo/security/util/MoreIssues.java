package demo.security.util;

import java.io.*;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MoreIssues {
    private static final Logger LOGGER = Logger.getLogger(MoreIssues.class.getName());
    private static final SecureRandom secureRandom = new SecureRandom();
    

    // Secure token generation using SecureRandom
    public static String generateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    // Safe command execution with input validation
    public static void executeCommand(String userInput) {
        if (!isValidCommand(userInput)) {
            LOGGER.warning("Invalid command attempt: " + userInput);
            throw new IllegalArgumentException("Invalid command");
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder()
                .command("cmd.exe", "/c", userInput)
                .redirectErrorStream(true);
            Process process = processBuilder.start();
            // Log the output instead of printing to stderr
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    LOGGER.info(line);
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Command execution failed: " + e.getMessage());
            throw new RuntimeException("Command execution failed", e);
        }
    }
    
    private static boolean isValidCommand(String command) {
        // Whitelist of allowed commands
        return Pattern.matches("^[a-zA-Z0-9\\s-_./]+$", command) 
            && !command.contains("../") 
            && !command.contains("&&") 
            && !command.contains("|");
    }

    // Weak Regular Expression
    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$"); // Overly permissive
        return pattern.matcher(email).matches();
    }

    // Resource leak and null pointer potential
    public static String readFromSocket(String host, int port) {
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            return new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
        } catch (IOException e) {
            return null; // Bad practice: returning null
        }
        // Resource leak: socket is never closed
    }

    // Session fixation vulnerability
    public static void handleSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionId = request.getParameter("sessionId");
        if (sessionId != null) {
            // Vulnerable to session fixation
            session.setAttribute("authenticatedUser", sessionId);
        }
    }

    // Proper error handling with logging
    public static void handleFileOperation() {
        try (FileInputStream fis = new FileInputStream(new File("config.txt"))) {
            // Process file...
        } catch (FileNotFoundException e) {
            LOGGER.warning("Configuration file not found: " + e.getMessage());
            throw new IllegalStateException("Required configuration file is missing", e);
        } catch (IOException e) {
            LOGGER.severe("Error reading configuration: " + e.getMessage());
            throw new IllegalStateException("Failed to read configuration", e);
        }
    }

    // Mutable static field - intentional issue
    public static String[] SENSITIVE_DATA = {"password", "key", "token"};
    
    // Calculate discount with magic numbers - intentional issue
    public static double calculateDiscount(double price) {
        if (price > 100.0) {
            return price * 0.15;
        } else if (price > 50.0) {
            return price * 0.1;
        }
        return price * 0.05;
    }
}
