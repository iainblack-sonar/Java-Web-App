package demo.security.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit tests for InsecureUtils class
 */
class InsecureUtilsTest {

    @Test
    void testInsecureUtilsClassExists() {
        // Just test that the class exists and can be referenced
        assertNotNull(InsecureUtils.class);
        assertEquals("InsecureUtils", InsecureUtils.class.getSimpleName());
    }
}

