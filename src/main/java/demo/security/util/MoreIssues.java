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
    
    // Constants for discount calculations
    private static final double PREMIUM_DISCOUNT = 0.15;
    private static final double STANDARD_DISCOUNT = 0.10;
    private static final double BASIC_DISCOUNT = 0.05;
    private static final double PREMIUM_THRESHOLD = 100.0;
    private static final double STANDARD_THRESHOLD = 50.0;

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

    // Safe socket reading with proper resource management
    public static String readFromSocket(String host, int port) {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("No data received from socket");
            }
            return line;
        } catch (IOException e) {
            LOGGER.severe("Socket read failed: " + e.getMessage());
            throw new RuntimeException("Failed to read from socket", e);
        }
    }

    // Secure session handling with fixation protection
    public static void handleSession(HttpServletRequest request) {
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate(); // Invalidate existing session
        }
        // Create new session
        HttpSession newSession = request.getSession(true);
        String username = request.getParameter("username");
        if (username != null && !username.trim().isEmpty()) {
            newSession.setAttribute("authenticatedUser", username);
            LOGGER.info("New session created for user: " + username);
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

    // Immutable static field with proper naming
    private static final String[] sensitiveDataFields = {"password", "key", "token"};
    
    public static String[] getSensitiveDataFields() {
        return sensitiveDataFields.clone(); // Return defensive copy
    }

    // Refactored to reduce complexity and improve readability
    public static int calculateSum(int a, int b, int c) {
        int sum = 0;
        
        if (a > 0) {
            sum += a;
            if (b > 0) {
                sum += b;
            }
        } else if (b > 0) {
            sum += b;
            if (c > 0) {
                sum += c;
            }
        } else if (c > 0) {
            sum += c;
        }
        
        return sum;
    }

    // Duplicate code block
    public static void duplicateBlock1() {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += i;
            System.out.println("Current sum: " + sum);
            if (sum > 50) {
                System.out.println("Sum exceeded 50");
                break;
            }
        }
    }

    // Duplicate code block
    public static void duplicateBlock2() {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += i;
            System.out.println("Current sum: " + sum);
            if (sum > 50) {
                System.out.println("Sum exceeded 50");
                break;
            }
        }
    }

    // Calculate discount using defined constants
    public static double calculateDiscount(double price) {
        if (price > PREMIUM_THRESHOLD) {
            return price * PREMIUM_DISCOUNT;
        } else if (price > STANDARD_THRESHOLD) {
            return price * STANDARD_DISCOUNT;
        }
        return price * BASIC_DISCOUNT;
    }
}
