package com.napier.sem;

import org.junit.jupiter.api.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppIntegrationTest {

    private App app;

    @BeforeEach
    public void init() {
        app = new App();
        System.setProperty("TEST_MODE", "true");  // Enable faster DB wait
    }

    @Test
    @Order(1)
    @DisplayName("Integration: App successfully connects to MySQL")
    public void testConnect() {
        app.connect();
        assertNotNull(app.con, "Connection should not be null");

        try {
            assertFalse(app.con.isClosed(), "Connection should be open");
        } catch (Exception e) {
            fail("Unexpected SQL exception: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Integration: App disconnect() closes the connection")
    public void testDisconnect() {
        app.connect();
        assertNotNull(app.con);

        app.disconnect();

        try {
            assertTrue(app.con.isClosed(), "Connection should be closed");
        } catch (Exception e) {
            fail("Unexpected SQL exception: " + e.getMessage());
        }
    }
}
