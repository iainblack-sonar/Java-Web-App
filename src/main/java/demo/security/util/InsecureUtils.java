package demo.security.util;

import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InsecureUtils {
    // Weak hash - MD5
    public static String weakHash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(input.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    // Hard-coded encryption key
    private static final String ENCRYPTION_KEY = "MyHardCodedKey12";

    // Weak encryption
    public static String weakEncryption(String input) throws Exception {
        SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
    }

    // XXE Vulnerability
    public static void parseXML(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        builder.parse(is);
    }

    // Path Traversal
    public static String readFile(String fileName) throws IOException {
        File file = new File("base_directory/" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        reader.close();
        return content.toString();
    }

    // Unsafe Deserialization
    public static Object deserialize(String base64SerializedObj) throws Exception {
        byte[] serializedObj = Base64.getDecoder().decode(base64SerializedObj);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedObj));
        return ois.readObject();
    }

    // XSS Vulnerability
    public static void displayUserInput(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String userInput = request.getParameter("userInput");
        response.getWriter().write("<div>" + userInput + "</div>");
    }

    // Hard-coded Credentials
    private static final String DB_PASSWORD = "admin123";
    public static void connectToDatabase() {
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "admin", DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
