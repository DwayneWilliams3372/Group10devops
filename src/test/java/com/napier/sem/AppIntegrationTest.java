package com.napier.sem;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for App.java using the live MySQL database
 * running inside docker-compose.
 * Requires the service name 'db' to be resolvable (same Docker network).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppIntegrationTest {

    private static App app;

    @BeforeAll
    public static void setup() {
        // Force CI mode so Menu does NOT start
        System.setProperty("NON_INTERACTIVE", "true");
        System.setProperty("CI", "true");

        app = new App();
        app.connect();
    }

    @AfterAll
    public static void teardown() {
        app.disconnect();
    }

    @Test
    @Order(1)
    public void testConnectionNotNull() {
        assertNotNull(app.con, "Connection should not be null after connect()");
    }

    @Test
    @Order(2)
    public void testSimpleQuery() {
        try {
            Statement stmt = app.con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT 1;");
            assertTrue(result.next(), "SELECT 1 should return one row");
            assertEquals(1, result.getInt(1));
        } catch (Exception e) {
            fail("Query failed: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void testDisconnect() {
        app.disconnect();
        assertDoesNotThrow(() -> {
            if (app.con != null) {
                assertTrue(app.con.isClosed(), "Connection should be closed after disconnect()");
            }
        });
    }
}
