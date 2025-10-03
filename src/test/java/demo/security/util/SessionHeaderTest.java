package demo.security.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SessionHeader class
 */
public class SessionHeaderTest {

    @Test
    public void testSessionHeaderCreation() {
        SessionHeader header = new SessionHeader("testUser", "session123");
        assertNotNull(header);
    }

    @Test
    public void testGetUsername() {
        SessionHeader header = new SessionHeader("testUser", "session123");
        assertEquals("testUser", header.getUsername());
    }

    @Test
    public void testGetSessionId() {
        SessionHeader header = new SessionHeader("testUser", "session123");
        assertEquals("session123", header.getSessionId());
    }

    @Test
    public void testSetUsername() {
        SessionHeader header = new SessionHeader("oldUser", "session123");
        header.setUsername("newUser");
        assertEquals("newUser", header.getUsername());
    }

    @Test
    public void testSetSessionId() {
        SessionHeader header = new SessionHeader("testUser", "oldSession");
        header.setSessionId("newSession");
        assertEquals("newSession", header.getSessionId());
    }
}
