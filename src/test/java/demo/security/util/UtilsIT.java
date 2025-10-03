package demo.security.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.security.KeyPair;

/**
 * Simple integration tests for Utils class
 */
class UtilsIT {

    private Utils utils;

    @BeforeEach
    void setUp() {
        utils = new Utils();
    }

    @Test
    void testUtilsInstantiation() {
        assertNotNull(utils);
        assertTrue(utils instanceof Utils);
    }

    @Test
    void testGenerateKeyIntegration() {
        // Test actual key generation - this is an integration test
        KeyPair keyPair = utils.generateKey();
        
        assertNotNull(keyPair, "KeyPair should not be null");
        assertNotNull(keyPair.getPublic(), "Public key should not be null");
        assertNotNull(keyPair.getPrivate(), "Private key should not be null");
        
        // Verify key algorithm
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
        assertEquals("RSA", keyPair.getPrivate().getAlgorithm());
    }

    @Test
    void testUtilsClassProperties() {
        String className = utils.getClass().getSimpleName();
        assertEquals("Utils", className);
    }
}