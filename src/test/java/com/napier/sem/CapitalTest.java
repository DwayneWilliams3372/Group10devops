package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Complete Unit Tests for the Capital class using Mockito.
 * Aims for 100% code coverage by testing all public methods, branches, and error scenarios.
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
     * when the database call is successful and returns multiple results.
     */
    @Test
    void testGetAllCapitals() throws SQLException {
        // ARRANGE: Set up the mock flow
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // Mock the ResultSet iteration: returns true for two rows, then false
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        // Mock the data retrieval for the first row
        when(mockResultSet.getString("NAME")).thenReturn("Beijing").thenReturn("Tokyo");
        when(mockResultSet.getString("COUNTRY")).thenReturn("China").thenReturn("Japan");
        when(mockResultSet.getInt("POPULATION")).thenReturn(21542000).thenReturn(13929286);

        // ACT
        ArrayList<Capital> capitals = capital.getCapitals();

        // ASSERT
        assertNotNull(capitals);
        assertEquals(2, capitals.size());
        assertEquals("Beijing", capitals.get(0).name);
        assertEquals("China", capitals.get(0).country);
        assertEquals(21542000, capitals.get(0).population);
        assertEquals("Tokyo", capitals.get(1).name);
        assertEquals("Japan", capitals.get(1).country);
        assertEquals(13929286, capitals.get(1).population);

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
     * continent String parameter on the PreparedStatement and returns results.
     */
    @Test
    void testContinentQuery() throws SQLException {
        // ARRANGE
        final String expectedContinent = "Europe";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("NAME")).thenReturn("London");
        when(mockResultSet.getString("COUNTRY")).thenReturn("United Kingdom");
        when(mockResultSet.getInt("POPULATION")).thenReturn(8961989);

        // ACT
        ArrayList<Capital> capitals = capital.getCapitalsContinent(expectedContinent);

        // ASSERT
        assertNotNull(capitals);
        assertEquals(1, capitals.size());
        assertEquals("London", capitals.get(0).name);
        // Verify that the PreparedStatement received the correct string parameter
        verify(mockStatement, times(1)).setString(1, expectedContinent);
        verify(mockStatement, times(1)).executeQuery();
    }

    /**
     * Tests that the getCapitalsRegion method correctly sets the
     * region String parameter on the PreparedStatement.
     */
    @Test
    void testRegionQuery() throws SQLException {
        // ARRANGE
        final String expectedRegion = "Western Europe";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No data

        // ACT
        capital.getCapitalsRegion(expectedRegion);

        // ASSERT
        // Verify that the PreparedStatement received the correct string parameter
        verify(mockStatement, times(1)).setString(1, expectedRegion);
        verify(mockStatement, times(1)).executeQuery();
    }

    /**
     * Tests that the getCapitalsPopulation method correctly sets the
     * int parameter on the PreparedStatement and returns results.
     */
    @Test
    void testPopulationQuery() throws SQLException {
        // ARRANGE
        final int expectedN = 5;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("NAME")).thenReturn("Beijing");
        when(mockResultSet.getString("COUNTRY")).thenReturn("China");
        when(mockResultSet.getInt("POPULATION")).thenReturn(21542000);

        // ACT
        ArrayList<Capital> capitals = capital.getCapitalsPopulation(expectedN);

        // ASSERT
        assertNotNull(capitals);
        assertEquals(1, capitals.size());
        // Verify that the PreparedStatement received the correct int parameter
        verify(mockStatement, times(1)).setInt(1, expectedN);
        verify(mockStatement, times(1)).executeQuery();
    }

    /**
     * Tests that the topCapitalsContinent method correctly sets the
     * string and int parameters on the PreparedStatement.
     */
    @Test
    void testTopCapitalsContinent() throws SQLException {
        // ARRANGE
        final String expectedContinent = "Asia";
        final int expectedN = 3;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No data

        // ACT
        capital.topCapitalsContinent(expectedContinent, expectedN);

        // ASSERT
        // Verify that the PreparedStatement received the correct parameters
        verify(mockStatement, times(1)).setString(1, expectedContinent);
        verify(mockStatement, times(1)).setInt(2, expectedN);
        verify(mockStatement, times(1)).executeQuery();
    }

    /**
     * Tests that the topCapitalsRegion method correctly sets the
     * string and int parameters on the PreparedStatement and returns results.
     */
    @Test
    void testTopCapitalsRegion() throws SQLException {
        // ARRANGE
        final String expectedRegion = "Eastern Asia";
        final int expectedN = 2;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // One result
        when(mockResultSet.getString("NAME")).thenReturn("Seoul");
        when(mockResultSet.getString("COUNTRY")).thenReturn("South Korea");
        when(mockResultSet.getInt("POPULATION")).thenReturn(9981619);

        // ACT
        ArrayList<Capital> capitals = capital.topCapitalsRegion(expectedRegion, expectedN);

        // ASSERT
        assertNotNull(capitals);
        assertEquals(1, capitals.size());
        // Verify that the PreparedStatement received the correct parameters
        verify(mockStatement, times(1)).setString(1, expectedRegion);
        verify(mockStatement, times(1)).setInt(2, expectedN);
        verify(mockStatement, times(1)).executeQuery();
    }

    /**
     * Tests that printCapitals handles an empty list correctly.
     */
    @Test
    void testPrintCapitalsEmpty() {
        // ARRANGE
        ArrayList<Capital> emptyList = new ArrayList<>();

        // ACT & ASSERT: Capture output to avoid cluttering test output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            capital.printCapitals(emptyList);
            String output = outputStream.toString();
            assertTrue(output.contains("No countries found"));
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that printCapitals prints a non-empty list correctly.
     */
    @Test
    void testPrintCapitalsWithData() {
        // ARRANGE
        ArrayList<Capital> capitals = new ArrayList<>();
        Capital c1 = new Capital();
        c1.name = "TestCity";
        c1.country = "TestCountry";
        c1.population = 1000000;
        capitals.add(c1);

        // ACT & ASSERT: Capture output to verify printing occurs without errors
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            capital.printCapitals(capitals);
            String output = outputStream.toString();
            assertTrue(output.contains("TestCity"));
            assertTrue(output.contains("TestCountry"));
            assertTrue(output.contains("1000000"));
        } finally {
            System.setOut(originalOut);
        }
    }
}