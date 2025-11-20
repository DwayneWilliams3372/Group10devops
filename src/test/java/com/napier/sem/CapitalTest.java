package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Simple Unit Tests for the Capital class using Mockito.
 * Focuses on basic query execution, parameter setting, and error handling.
 */
@ExtendWith(MockitoExtension.class)
public class CapitalTest {

    // Mock JDBC objects to simulate database interaction
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    // The class under test, injected with the mocked connection
    @InjectMocks
    private Capital capital;

    /**
     * Initialize the Capital object with the mocked connection before each test.
     */
    @BeforeEach
    public void setUp() {
        capital = new Capital(mockConnection);
    }

    /**
     * Tests that the getCapitals method successfully returns a list of capital objects
     * when the database call is successful.
     */
    @Test
    void testGetAllCapitals() throws SQLException {
        // ARRANGE: Set up the mock flow
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // 1. Mock the ResultSet iteration: returns true (has data), then false (no more data)
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        // 2. Mock the data retrieval for the single row
        when(mockResultSet.getString("NAME")).thenReturn("Beijing");
        when(mockResultSet.getString("COUNTRY")).thenReturn("China");
        when(mockResultSet.getInt("POPULATION")).thenReturn(21542000);

        // ACT
        ArrayList<Capital> capitals = capital.getCapitals();

        // ASSERT
        assertNotNull(capitals);
        assertEquals(1, capitals.size());

        // Verify that the database execute method was called
        verify(mockStatement, times(1)).executeQuery();
    }

    /**
     * Tests that the method handles a database failure (SQLException)
     * by catching the error and returning an empty list.
     */
    @Test
    void testQueryFails() throws SQLException {
        // ARRANGE: Mock the connection to throw an exception immediately
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Simulated DB connection failure"));

        // ACT
        ArrayList<Capital> capitals = capital.getCapitals();

        // ASSERT
        assertNotNull(capitals);
        assertTrue(capitals.isEmpty());
    }

    /**
     * Tests that the getCapitalsContinent method correctly sets the
     * continent String parameter on the PreparedStatement.
     */
    @Test
    void testContinentQuery() throws SQLException {
        // ARRANGE
        final String expectedContinent = "Europe";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Mock no data returned

        // ACT
        capital.getCapitalsContinent(expectedContinent);

        // ASSERT
        // Verify that the PreparedStatement received the correct string parameter
        verify(mockStatement, times(1)).setString(1, expectedContinent);
        verify(mockStatement, times(1)).executeQuery();
    }
}