package com.napier.sem;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private App app;

    @BeforeEach
    void setUp() {
        app = new App();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        app.disconnect();
    }

    @Test
    void testConnectSuccess() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            // Setup
            Connection mockConnection = mock(Connection.class);
            mockedDriverManager.when(() ->
                    DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/world?useSSL=false",
                            "root",
                            "example"
                    )
            ).thenReturn(mockConnection);

            // Execute
            app.connect();

            // Verify
            assertNotNull(app.con);
            assertEquals(mockConnection, app.con);
        }
    }

    @Test
    void testConnectFailure() {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            // Setup - properly stub the static method
            mockedDriverManager.when(() ->
                    DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/world?useSSL=false",
                            "root",
                            "example"
                    )
            ).thenThrow(new SQLException("Connection failed"));

            // Execute & Verify
            Exception exception = assertThrows(SQLException.class, () -> {
                app.connect();
            });

            assertEquals("Connection failed", exception.getMessage());
            assertNull(app.con); // Connection should remain null
        }
    }

    @Test
    void testDisconnectWithConnection() throws SQLException {
        // Setup
        Connection mockConnection = mock(Connection.class);
        app.con = mockConnection;

        // Execute
        app.disconnect();

        // Verify
        verify(mockConnection, times(1)).close();
        assertNull(app.con);
    }

    @Test
    void testDisconnectWithoutConnection() {
        // Setup
        app.con = null;

        // Execute & Verify - should not throw exception
        assertDoesNotThrow(() -> app.disconnect());
    }

    @Test
    void testDisconnectWithSQLExceptionOnClose() throws SQLException {
        // Setup
        Connection mockConnection = mock(Connection.class);
        app.con = mockConnection;

        // Proper void method stubbing
        doThrow(new SQLException("Close failed")).when(mockConnection).close();

        // Execute & Verify - should handle exception gracefully
        assertDoesNotThrow(() -> app.disconnect());
        assertNull(app.con); // Connection should still be set to null
    }

    @Test
    void testMultipleConnectCalls() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            Connection mockConnection1 = mock(Connection.class);
            Connection mockConnection2 = mock(Connection.class);

            // Stub multiple calls
            mockedDriverManager.when(() ->
                            DriverManager.getConnection(
                                    "jdbc:mysql://localhost:3306/world?useSSL=false",
                                    "root",
                                    "example"
                            )
                    ).thenReturn(mockConnection1) // First call returns first mock
                    .thenReturn(mockConnection2); // Second call returns second mock

            // First connect
            app.connect();
            assertEquals(mockConnection1, app.con);

            // Disconnect first
            app.disconnect();
            assertNull(app.con);

            // Second connect
            app.connect();
            assertEquals(mockConnection2, app.con);
        }
    }
}