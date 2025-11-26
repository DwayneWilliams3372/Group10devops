package com.napier.sem;

import org.junit.jupiter.api.*;
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

    // ===== EXISTING TESTS (keep these) =====

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

    // ===== NEW TESTS TO ADD =====

    @Test
    void testExecuteQueryNoParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Capital> result = capital.getCapitals(); // Uses executeQuery() with no params

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteQueryWithStringParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Capital> result = capital.getCapitalsContinent("Europe");

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteQueryWithIntParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Capital> result = capital.getCapitalsPopulation(5);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteQueryWithTwoParamsWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Capital> result = capital.topCapitalsContinent("Asia", 3);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test extractCapital indirectly through public methods
    @Test
    void testExtractCapitalWithNullValues() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("NAME")).thenReturn(null);
        when(mockResultSet.getString("COUNTRY")).thenReturn(null);
        when(mockResultSet.getInt("POPULATION")).thenReturn(0);

        // ACT
        ArrayList<Capital> capitals = capital.getCapitals();

        // ASSERT
        assertNotNull(capitals);
        assertEquals(1, capitals.size());
        assertNull(capitals.get(0).name);
        assertNull(capitals.get(0).country);
        assertEquals(0, capitals.get(0).population);
    }

    @Test
    void testOutputCapitalsWithNullList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> capital.outputCapitals(null, "test.md"));
    }

    @Test
    void testOutputCapitalsWithEmptyList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> capital.outputCapitals(new ArrayList<>(), "test.md"));
    }

    @Test
    void testOutputCapitalsWithNullCapitalInList() {
        // ARRANGE
        ArrayList<Capital> capitals = new ArrayList<>();
        capitals.add(null);
        Capital validCapital = new Capital();
        validCapital.name = "Berlin";
        validCapital.country = "Germany";
        validCapital.population = 3644826;
        capitals.add(validCapital);

        // ACT & ASSERT - Should handle null gracefully
        assertDoesNotThrow(() -> capital.outputCapitals(capitals, "test-null.md"));
    }

    @Test
    void testOutputCapitalsIOException() {
        // ARRANGE
        ArrayList<Capital> capitals = new ArrayList<>();
        Capital capital = new Capital();
        capital.name = "Test";
        capital.country = "TestCountry";
        capital.population = 1000000;
        capitals.add(capital);

        // This test verifies the method handles IO exceptions gracefully
        assertDoesNotThrow(() -> capital.outputCapitals(capitals, "/invalid/path/test.md"));
    }

    @Test
    void testPrintCapitalsWithNull() {
        // ACT & ASSERT - Should handle null input gracefully
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            capital.printCapitals(null);
            String output = outputStream.toString();
            assertTrue(output.contains("No countries found"));
        } finally {
            System.setOut(originalOut);
        }
    }

    // Test the default constructor
    @Test
    void testDefaultConstructor() {
        Capital capitalWithDefaultConstructor = new Capital();
        assertNotNull(capitalWithDefaultConstructor);
    }

    // Test outputCapitals with valid data (success case)
    @Test
    void testOutputCapitalsSuccess() {
        // ARRANGE
        ArrayList<Capital> capitals = new ArrayList<>();
        Capital cap = new Capital();
        cap.name = "Madrid";
        cap.country = "Spain";
        cap.population = 3223334;
        capitals.add(cap);

        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> capital.outputCapitals(capitals, "success-test.md"));
    }
}