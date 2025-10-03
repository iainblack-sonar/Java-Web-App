package demo.security.util;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class MoreIssues {
    private static final Logger LOGGER = Logger.getLogger(MoreIssues.class.getName());
    
    // Predictable Random - Using Random instead of SecureRandom
    public static String generateToken() {
        Random random = new Random();
        return String.valueOf(random.nextLong());
    }

    // Command Injection vulnerability
    public static void executeCommand(String userInput) {
        try {
            Runtime.getRuntime().exec("cmd.exe /c " + userInput);
        } catch (IOException e) {
            e.printStackTrace(); // Bad practice: printStackTrace
        }
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

    // Empty catch block - Silent failure
    public static void silentFailure() {
        try {
            File file = new File("nonexistent.txt");
            FileInputStream fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // Empty catch block - Bad practice
        }
    }

    // Mutable static field
    public static String[] SENSITIVE_DATA = {"password", "key", "token"};

    // Poorly structured code with multiple returns
    public static int complexConditions(int a, int b, int c) {
        if (a > 0) {
            if (b > 0) {
                return a + b;
            }
            return a;
        }
        if (b > 0) {
            if (c > 0) {
                return b + c;
            }
            return b;
        }
        return c;
    }

    // Dead store
    public static void deadStore() {
        int unused = 42; // Value is never used
        String result = "test";
        result = "newValue"; // Original value is never used
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

    // Magic numbers
    public static double calculateDiscount(double price) {
        if (price > 100.0) {
            return price * 0.15; // Magic number
        } else if (price > 50.0) {
            return price * 0.1; // Magic number
        }
        return price * 0.05; // Magic number
    }
}
