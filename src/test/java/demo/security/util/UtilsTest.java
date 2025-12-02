package demo.security.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Utils class
 */
public class UtilsTest {

    private Utils utils;

    @BeforeEach
    public void setUp() {
        utils = new Utils();
    }

    @Test
    public void testUtilsInstantiation() {
        assertNotNull(utils);
        assertTrue(utils instanceof Utils);
    }

    @Test
    public void testUtilsClassMethods() {
        // Test basic class properties
        String className = utils.getClass().getSimpleName();
        assertEquals("Utils", className);
    }

    @Test
    public void testGenerateKey() {
        // Test that generateKey method exists and can be called
        assertDoesNotThrow(() -> {
            utils.generateKey();
        });
    }
}
