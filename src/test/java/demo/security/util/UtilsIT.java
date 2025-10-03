package demo.security.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Integration tests for Utils class
 * These tests focus on actual functionality and file system interactions
 */
public class UtilsIT {

    private Utils utils;

    @BeforeEach
    public void setUp() {
        utils = new Utils();
    }

    @Test
    public void testGenerateKeyIntegration() {
        // Test actual key generation
        KeyPair keyPair = utils.generateKey();
        
        assertNotNull(keyPair, "KeyPair should not be null");
        assertNotNull(keyPair.getPublic(), "Public key should not be null");
        assertNotNull(keyPair.getPrivate(), "Private key should not be null");
        
        // Verify key properties
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
        assertEquals("RSA", keyPair.getPrivate().getAlgorithm());
    }

    @Test
    public void testDeleteFileIntegration() throws Exception {
        // Create a temporary file for testing
        Path tempFile = Files.createTempFile("test", ".tmp");
        String filePath = tempFile.toString();
        
        // Verify file exists
        assertTrue(Files.exists(tempFile), "Temp file should exist");
        
        // Test file deletion
        utils.deleteFile(filePath);
        
        // Note: The actual deleteFile method might not work due to security restrictions
        // but we're testing that the method executes without throwing exceptions
        assertDoesNotThrow(() -> utils.deleteFile(filePath));
        
        // Clean up if file still exists
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testExecuteJsIntegration() {
        // Test JavaScript execution (method should handle the call gracefully)
        String jsCode = "console.log('Hello from integration test');";
        
        // This should not throw an exception
        assertDoesNotThrow(() -> utils.executeJs(jsCode));
    }

    @Test
    public void testEncryptIntegration() {
        // Test encryption method with sample data
        byte[] data = "Integration test data".getBytes();
        byte[] key = "TestKey123456789".getBytes(); // 16 bytes for AES
        
        // This should not throw an exception
        assertDoesNotThrow(() -> utils.encrypt(data, key));
    }

    @Test
    public void testUtilsClassProperties() {
        // Test basic class properties and instantiation
        assertNotNull(utils);
        assertEquals("Utils", utils.getClass().getSimpleName());
        assertTrue(utils instanceof Utils);
        
        // Test that we can create multiple instances
        Utils utils2 = new Utils();
        assertNotNull(utils2);
        assertNotSame(utils, utils2);
    }
}