package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PopulationTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private Statement mockStatement;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private Population population;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException {
        closeable = MockitoAnnotations.openMocks(this);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        population = new Population(mockConnection);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testDefaultConstructor() {
        Population defaultPopulation = new Population();
        assertNotNull(defaultPopulation);
    }

    @Test
    void testConnectionConstructor() {
        assertNotNull(population);
    }

    @Test
    void testContinentPopulation() throws SQLException {
        // Setup mock result set for continent population
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("Name")).thenReturn("Asia", "Europe");
        when(mockResultSet.getLong("Total_Population")).thenReturn(3700000000L, 730000000L);
        when(mockResultSet.getLong("City_Population")).thenReturn(1500000000L, 500000000L);
        when(mockResultSet.getLong("No_City_Population")).thenReturn(2200000000L, 230000000L);
        when(mockResultSet.getDouble("City_Percentage")).thenReturn(40.5, 68.5);
        when(mockResultSet.getDouble("No_City_Percentage")).thenReturn(59.5, 31.5);

        ArrayList<Population> populations = population.continentPopulation();

        assertNotNull(populations);
        assertEquals(2, populations.size());

        // Verify first continent (Asia)
        Population asia = populations.get(0);
        assertEquals("Asia", getPrivateField(asia, "Name"));
        assertEquals(3700000000L, getPrivateField(asia, "totalPopulation"));
        assertEquals(1500000000L, getPrivateField(asia, "cityPopulation"));
        assertEquals(2200000000L, getPrivateField(asia, "noCityPopulation"));
        Double cityPercentage = (Double) getPrivateField(asia, "cityPercentage");
        Double noCityPercentage = (Double) getPrivateField(asia, "noCityPercentage");
        assertNotNull(cityPercentage);
        assertNotNull(noCityPercentage);
        assertEquals(40.5, cityPercentage, 0.001);
        assertEquals(59.5, noCityPercentage, 0.001);

        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery(anyString());
    }

    @Test
    void testRegionPopulation() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Eastern Asia");
        when(mockResultSet.getLong("Total_Population")).thenReturn(1500000000L);
        when(mockResultSet.getLong("City_Population")).thenReturn(800000000L);
        when(mockResultSet.getLong("No_City_Population")).thenReturn(700000000L);
        when(mockResultSet.getDouble("City_Percentage")).thenReturn(53.3);
        when(mockResultSet.getDouble("No_City_Percentage")).thenReturn(46.7);

        ArrayList<Population> populations = population.regionPopulation();

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population region = populations.get(0);
        assertEquals("Eastern Asia", getPrivateField(region, "Name"));
        assertEquals(1500000000L, getPrivateField(region, "totalPopulation"));
        assertEquals(800000000L, getPrivateField(region, "cityPopulation"));
        assertEquals(700000000L, getPrivateField(region, "noCityPopulation"));
        Double cityPercentage = (Double) getPrivateField(region, "cityPercentage");
        Double noCityPercentage = (Double) getPrivateField(region, "noCityPercentage");
        assertNotNull(cityPercentage);
        assertNotNull(noCityPercentage);
        assertEquals(53.3, cityPercentage, 0.001);
        assertEquals(46.7, noCityPercentage, 0.001);
    }

    @Test
    void testCountryPopulation() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("China");
        when(mockResultSet.getLong("Total_Population")).thenReturn(1300000000L);
        when(mockResultSet.getLong("City_Population")).thenReturn(600000000L);
        when(mockResultSet.getLong("No_City_Population")).thenReturn(700000000L);
        when(mockResultSet.getDouble("City_Percentage")).thenReturn(46.2);
        when(mockResultSet.getDouble("No_City_Percentage")).thenReturn(53.8);

        ArrayList<Population> populations = population.countryPopulation();

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population country = populations.get(0);
        assertEquals("China", getPrivateField(country, "Name"));
        assertEquals(1300000000L, getPrivateField(country, "totalPopulation"));
        assertEquals(600000000L, getPrivateField(country, "cityPopulation"));
        assertEquals(700000000L, getPrivateField(country, "noCityPopulation"));
        Double cityPercentage = (Double) getPrivateField(country, "cityPercentage");
        Double noCityPercentage = (Double) getPrivateField(country, "noCityPercentage");
        assertNotNull(cityPercentage);
        assertNotNull(noCityPercentage);
        assertEquals(46.2, cityPercentage, 0.001);
        assertEquals(53.8, noCityPercentage, 0.001);
    }

    @Test
    void testPopulationWorld() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("World");
        when(mockResultSet.getLong("Population")).thenReturn(7800000000L);

        ArrayList<Population> populations = population.populationWorld();

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population world = populations.get(0);
        assertEquals("World", getPrivateField(world, "Name"));
        assertEquals(7800000000L, getPrivateField(world, "totalPopulation"));

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testPopulationContinent() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Asia");
        when(mockResultSet.getLong("Population")).thenReturn(4500000000L);

        ArrayList<Population> populations = population.populationContinent("Asia");

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population continent = populations.get(0);
        assertEquals("Asia", getPrivateField(continent, "Name"));
        assertEquals(4500000000L, getPrivateField(continent, "totalPopulation"));

        verify(mockPreparedStatement).setString(1, "Asia");
    }

    @Test
    void testPopulationRegion() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Western Europe");
        when(mockResultSet.getLong("Population")).thenReturn(180000000L);

        ArrayList<Population> populations = population.populationRegion("Western Europe");

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population region = populations.get(0);
        assertEquals("Western Europe", getPrivateField(region, "Name"));
        assertEquals(180000000L, getPrivateField(region, "totalPopulation"));

        verify(mockPreparedStatement).setString(1, "Western Europe");
    }

    @Test
    void testPopulationCountry() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Germany");
        when(mockResultSet.getLong("Population")).thenReturn(83000000L);

        ArrayList<Population> populations = population.populationCountry("Germany");

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population country = populations.get(0);
        assertEquals("Germany", getPrivateField(country, "Name"));
        assertEquals(83000000L, getPrivateField(country, "totalPopulation"));

        verify(mockPreparedStatement).setString(1, "Germany");
    }

    @Test
    void testPopulationDistrict() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("California");
        when(mockResultSet.getLong("Population")).thenReturn(39000000L);

        ArrayList<Population> populations = population.populationDistrict("California");

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population district = populations.get(0);
        assertEquals("California", getPrivateField(district, "Name"));
        assertEquals(39000000L, getPrivateField(district, "totalPopulation"));

        verify(mockPreparedStatement).setString(1, "California");
    }

    @Test
    void testPopulationCity() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("London");
        when(mockResultSet.getLong("Population")).thenReturn(9000000L);

        ArrayList<Population> populations = population.populationCity("London");

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population city = populations.get(0);
        assertEquals("London", getPrivateField(city, "Name"));
        assertEquals(9000000L, getPrivateField(city, "totalPopulation"));

        verify(mockPreparedStatement).setString(1, "London");
    }

    @Test
    void testPrintPopulationEmptyList() {
        ArrayList<Population> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> population.printPopulation(emptyList));
    }

    @Test
    void testPrintPopulationNull() {
        assertDoesNotThrow(() -> population.printPopulation(null));
    }

    @Test
    void testPrintSinglePopulationEmptyList() {
        ArrayList<Population> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> population.printSinglePopulation(emptyList));
    }

    @Test
    void testPrintSinglePopulationNull() {
        assertDoesNotThrow(() -> population.printSinglePopulation(null));
    }

    @Test
    void testPrintPopulationWithData() {
        ArrayList<Population> populations = new ArrayList<>();

        Population pop = new Population();
        setPrivateField(pop, "Name", "Test Continent");
        setPrivateField(pop, "totalPopulation", 1000000000L);
        setPrivateField(pop, "cityPopulation", 400000000L);
        setPrivateField(pop, "noCityPopulation", 600000000L);
        setPrivateField(pop, "cityPercentage", 40.0);
        setPrivateField(pop, "noCityPercentage", 60.0);
        populations.add(pop);

        assertDoesNotThrow(() -> population.printPopulation(populations));
    }

    @Test
    void testPrintSinglePopulationWithData() {
        ArrayList<Population> populations = new ArrayList<>();

        Population pop = new Population();
        setPrivateField(pop, "Name", "Test Country");
        setPrivateField(pop, "totalPopulation", 50000000L);
        populations.add(pop);

        assertDoesNotThrow(() -> population.printSinglePopulation(populations));
    }

    @Test
    void testSQLExceptionHandling() throws SQLException {
        when(mockConnection.createStatement()).thenThrow(new SQLException("Database error"));

        ArrayList<Population> populations = population.continentPopulation();

        assertNotNull(populations);
        assertTrue(populations.isEmpty());
    }

    @Test
    void testEmptyResultSet() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        ArrayList<Population> populations = population.continentPopulation();

        assertNotNull(populations);
        assertTrue(populations.isEmpty());
    }

    @Test
    void testMultipleContinents() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, true, false);
        when(mockResultSet.getString("Name")).thenReturn("Asia", "Africa", "Europe");
        when(mockResultSet.getLong("Total_Population")).thenReturn(4500000000L, 1300000000L, 750000000L);
        when(mockResultSet.getLong("City_Population")).thenReturn(1800000000L, 400000000L, 500000000L);
        when(mockResultSet.getLong("No_City_Population")).thenReturn(2700000000L, 900000000L, 250000000L);
        when(mockResultSet.getDouble("City_Percentage")).thenReturn(40.0, 30.8, 66.7);
        when(mockResultSet.getDouble("No_City_Percentage")).thenReturn(60.0, 69.2, 33.3);

        ArrayList<Population> populations = population.continentPopulation();

        assertEquals(3, populations.size());

        // Verify ordering by total population (descending)
        Long pop1 = (Long) getPrivateField(populations.get(0), "totalPopulation");
        Long pop2 = (Long) getPrivateField(populations.get(1), "totalPopulation");
        Long pop3 = (Long) getPrivateField(populations.get(2), "totalPopulation");

        assertNotNull(pop1);
        assertNotNull(pop2);
        assertNotNull(pop3);
        assertTrue(pop1 >= pop2);
        assertTrue(pop2 >= pop3);
        assertEquals(4500000000L, pop1);
        assertEquals(1300000000L, pop2);
        assertEquals(750000000L, pop3);
    }

    @Test
    void testPopulationDataConsistency() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Test Region");
        when(mockResultSet.getLong("Total_Population")).thenReturn(100000000L);
        when(mockResultSet.getLong("City_Population")).thenReturn(40000000L);
        when(mockResultSet.getLong("No_City_Population")).thenReturn(60000000L);
        when(mockResultSet.getDouble("City_Percentage")).thenReturn(40.0);
        when(mockResultSet.getDouble("No_City_Percentage")).thenReturn(60.0);

        ArrayList<Population> populations = population.regionPopulation();

        Population region = populations.get(0);
        Long totalPop = (Long) getPrivateField(region, "totalPopulation");
        Long cityPop = (Long) getPrivateField(region, "cityPopulation");
        Long noCityPop = (Long) getPrivateField(region, "noCityPopulation");
        Double cityPct = (Double) getPrivateField(region, "cityPercentage");
        Double noCityPct = (Double) getPrivateField(region, "noCityPercentage");

        // Verify data consistency
        assertNotNull(totalPop);
        assertNotNull(cityPop);
        assertNotNull(noCityPop);
        assertNotNull(cityPct);
        assertNotNull(noCityPct);
        assertEquals(totalPop, cityPop + noCityPop);
        assertTrue(cityPct >= 0 && cityPct <= 100);
        assertTrue(noCityPct >= 0 && noCityPct <= 100);
        assertEquals(100.0, cityPct + noCityPct, 0.1); // Allow small rounding differences
    }

    @Test
    void testExtractPopulationsMethod() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Test Country");
        when(mockResultSet.getLong("Population")).thenReturn(50000000L);

        ArrayList<Population> populations = population.populationCountry("Test Country");

        assertNotNull(populations);
        assertEquals(1, populations.size());

        Population country = populations.get(0);
        assertEquals("Test Country", getPrivateField(country, "Name"));
        assertEquals(50000000L, getPrivateField(country, "totalPopulation"));
        // Other fields should be 0/default for single population queries
        assertEquals(0L, getPrivateField(country, "cityPopulation"));
        assertEquals(0L, getPrivateField(country, "noCityPopulation"));
        Double cityPercentage = (Double) getPrivateField(country, "cityPercentage");
        Double noCityPercentage = (Double) getPrivateField(country, "noCityPercentage");
        assertNotNull(cityPercentage);
        assertNotNull(noCityPercentage);
        assertEquals(0.0, cityPercentage, 0.001);
        assertEquals(0.0, noCityPercentage, 0.001);
    }

    // ===== NEW TESTS ADDED FOR COMPLETE COVERAGE =====

    @Test
    void testGetPopulationWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.createStatement()).thenThrow(new SQLException("Database error"));

        // ACT
        ArrayList<Population> populations = population.continentPopulation();

        // ASSERT
        assertNotNull(populations);
        assertTrue(populations.isEmpty());
    }

    @Test
    void testExecuteQueryNoParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Population> result = population.populationWorld();

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteQueryWithStringParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Population> result = population.populationContinent("Asia");

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test extractPopulations indirectly through public methods with null values
    @Test
    void testExtractPopulationsWithNullValues() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Name")).thenReturn(null);
        when(mockResultSet.getLong("Population")).thenReturn(0L);

        // ACT
        ArrayList<Population> populations = population.populationWorld();

        // ASSERT
        assertNotNull(populations);
        assertEquals(1, populations.size());
        assertNull(getPrivateField(populations.get(0), "Name"));
        assertEquals(0L, getPrivateField(populations.get(0), "totalPopulation"));
        assertEquals(0L, getPrivateField(populations.get(0), "cityPopulation"));
        assertEquals(0L, getPrivateField(populations.get(0), "noCityPopulation"));
        Double cityPercentage = (Double) getPrivateField(populations.get(0), "cityPercentage");
        Double noCityPercentage = (Double) getPrivateField(populations.get(0), "noCityPercentage");
        assertNotNull(cityPercentage);
        assertNotNull(noCityPercentage);
        assertEquals(0.0, cityPercentage, 0.001);
        assertEquals(0.0, noCityPercentage, 0.001);
    }

    // Test outputPopulation method thoroughly
    @Test
    void testOutputPopulationWithNullList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> population.outputPopulation(null, "test.md"));
    }

    @Test
    void testOutputPopulationWithEmptyList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> population.outputPopulation(new ArrayList<>(), "test.md"));
    }

    @Test
    void testOutputPopulationWithNullPopulationInList() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        populations.add(null);
        Population validPopulation = new Population();
        setPrivateField(validPopulation, "Name", "Europe");
        setPrivateField(validPopulation, "totalPopulation", 750000000L);
        setPrivateField(validPopulation, "cityPopulation", 500000000L);
        setPrivateField(validPopulation, "noCityPopulation", 250000000L);
        setPrivateField(validPopulation, "cityPercentage", 66.7);
        setPrivateField(validPopulation, "noCityPercentage", 33.3);
        populations.add(validPopulation);

        // ACT & ASSERT - Should handle null gracefully
        assertDoesNotThrow(() -> population.outputPopulation(populations, "test-null.md"));
    }

    @Test
    void testOutputPopulationSuccess() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population pop1 = new Population();
        setPrivateField(pop1, "Name", "Asia");
        setPrivateField(pop1, "totalPopulation", 4500000000L);
        setPrivateField(pop1, "cityPopulation", 1800000000L);
        setPrivateField(pop1, "noCityPopulation", 2700000000L);
        setPrivateField(pop1, "cityPercentage", 40.0);
        setPrivateField(pop1, "noCityPercentage", 60.0);
        populations.add(pop1);

        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> population.outputPopulation(populations, "success-test.md"));
    }

    @Test
    void testOutputPopulationIOException() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population testPopulation = new Population();
        setPrivateField(testPopulation, "Name", "Test Continent");
        setPrivateField(testPopulation, "totalPopulation", 1000000000L);
        setPrivateField(testPopulation, "cityPopulation", 400000000L);
        setPrivateField(testPopulation, "noCityPopulation", 600000000L);
        setPrivateField(testPopulation, "cityPercentage", 40.0);
        setPrivateField(testPopulation, "noCityPercentage", 60.0);
        populations.add(testPopulation);

        // This test verifies the method handles IO exceptions gracefully
        assertDoesNotThrow(() -> population.outputPopulation(populations, "/invalid/path/test.md"));
    }

    // Test outputSinglePopulation method thoroughly
    @Test
    void testOutputSinglePopulationWithNullList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> population.outputSinglePopulation(null, "test.md"));
    }

    @Test
    void testOutputSinglePopulationWithEmptyList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> population.outputSinglePopulation(new ArrayList<>(), "test.md"));
    }

    @Test
    void testOutputSinglePopulationSuccess() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population pop1 = new Population();
        setPrivateField(pop1, "Name", "Germany");
        setPrivateField(pop1, "totalPopulation", 83000000L);
        populations.add(pop1);

        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> population.outputSinglePopulation(populations, "single-success-test.md"));
    }

    @Test
    void testOutputSinglePopulationIOException() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population testPopulation = new Population();
        setPrivateField(testPopulation, "Name", "Test Country");
        setPrivateField(testPopulation, "totalPopulation", 50000000L);
        populations.add(testPopulation);

        // This test verifies the method handles IO exceptions gracefully
        assertDoesNotThrow(() -> population.outputSinglePopulation(populations, "/invalid/path/test.md"));
    }

    // Test print methods with edge cases
    @Test
    void testPrintPopulationWithZeroValues() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population zeroPopulation = new Population();
        setPrivateField(zeroPopulation, "Name", "Zero Population");
        setPrivateField(zeroPopulation, "totalPopulation", 0L);
        setPrivateField(zeroPopulation, "cityPopulation", 0L);
        setPrivateField(zeroPopulation, "noCityPopulation", 0L);
        setPrivateField(zeroPopulation, "cityPercentage", 0.0);
        setPrivateField(zeroPopulation, "noCityPercentage", 0.0);
        populations.add(zeroPopulation);

        // ACT & ASSERT - Should handle zero values without exceptions
        assertDoesNotThrow(() -> population.printPopulation(populations));
    }

    @Test
    void testPrintSinglePopulationWithZeroPopulation() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population zeroPopulation = new Population();
        setPrivateField(zeroPopulation, "Name", "Zero Population");
        setPrivateField(zeroPopulation, "totalPopulation", 0L);
        populations.add(zeroPopulation);

        // ACT & ASSERT - Should handle zero population without exceptions
        assertDoesNotThrow(() -> population.printSinglePopulation(populations));
    }

    @Test
    void testPrintPopulationWithVeryLongNames() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population longNamePopulation = new Population();
        setPrivateField(longNamePopulation, "Name", "ThisIsAVeryLongContinentNameThatExceedsNormalLength");
        setPrivateField(longNamePopulation, "totalPopulation", 1234567890L);
        setPrivateField(longNamePopulation, "cityPopulation", 500000000L);
        setPrivateField(longNamePopulation, "noCityPopulation", 734567890L);
        setPrivateField(longNamePopulation, "cityPercentage", 40.5);
        setPrivateField(longNamePopulation, "noCityPercentage", 59.5);
        populations.add(longNamePopulation);

        // ACT & ASSERT - Should handle long names without exceptions
        assertDoesNotThrow(() -> population.printPopulation(populations));
    }

    // Test SQLException for all single population methods
    @Test
    void testPopulationRegionSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<Population> populations = population.populationRegion("Western Europe");

        assertNotNull(populations);
        assertTrue(populations.isEmpty());
    }

    @Test
    void testPopulationCountrySQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<Population> populations = population.populationCountry("Germany");

        assertNotNull(populations);
        assertTrue(populations.isEmpty());
    }

    @Test
    void testPopulationDistrictSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<Population> populations = population.populationDistrict("California");

        assertNotNull(populations);
        assertTrue(populations.isEmpty());
    }

    @Test
    void testPopulationCitySQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<Population> populations = population.populationCity("London");

        assertNotNull(populations);
        assertTrue(populations.isEmpty());
    }

    // Test with empty string parameters
    @Test
    void testPopulationContinentWithEmptyString() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Population> populations = population.populationContinent("");

        // ASSERT
        assertNotNull(populations);
        assertTrue(populations.isEmpty());
        verify(mockPreparedStatement).setString(1, "");
    }

    @Test
    void testPopulationRegionWithEmptyString() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Population> populations = population.populationRegion("");

        // ASSERT
        assertNotNull(populations);
        assertTrue(populations.isEmpty());
        verify(mockPreparedStatement).setString(1, "");
    }

    // Test with null parameters
    @Test
    void testPopulationContinentWithNull() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Population> populations = population.populationContinent(null);

        // ASSERT
        assertNotNull(populations);
        assertTrue(populations.isEmpty());
        verify(mockPreparedStatement).setString(1, null);
    }

    @Test
    void testPopulationRegionWithNull() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Population> populations = population.populationRegion(null);

        // ASSERT
        assertNotNull(populations);
        assertTrue(populations.isEmpty());
        verify(mockPreparedStatement).setString(1, null);
    }

    // Test population calculations consistency
    @Test
    void testPopulationCalculationsConsistency() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Test Continent");
        when(mockResultSet.getLong("Total_Population")).thenReturn(1000000000L);
        when(mockResultSet.getLong("City_Population")).thenReturn(400000000L);
        when(mockResultSet.getLong("No_City_Population")).thenReturn(600000000L);
        when(mockResultSet.getDouble("City_Percentage")).thenReturn(40.0);
        when(mockResultSet.getDouble("No_City_Percentage")).thenReturn(60.0);

        ArrayList<Population> populations = population.continentPopulation();

        Population testPop = populations.get(0);
        Long totalPop = (Long) getPrivateField(testPop, "totalPopulation");
        Long cityPop = (Long) getPrivateField(testPop, "cityPopulation");
        Long noCityPop = (Long) getPrivateField(testPop, "noCityPopulation");
        Double cityPct = (Double) getPrivateField(testPop, "cityPercentage");
        Double noCityPct = (Double) getPrivateField(testPop, "noCityPercentage");

        // Verify calculations are consistent
        assertNotNull(totalPop);
        assertNotNull(cityPop);
        assertNotNull(noCityPop);
        assertNotNull(cityPct);
        assertNotNull(noCityPct);
        assertEquals(totalPop, cityPop + noCityPop);
        assertEquals(100.0, cityPct + noCityPct, 0.1); // Allow small rounding differences
    }

    // Test output methods with special characters
    @Test
    void testOutputPopulationWithSpecialCharacters() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population specialPopulation = new Population();
        setPrivateField(specialPopulation, "Name", "Region & Name/With-Special|Chars");
        setPrivateField(specialPopulation, "totalPopulation", 100000000L);
        setPrivateField(specialPopulation, "cityPopulation", 40000000L);
        setPrivateField(specialPopulation, "noCityPopulation", 60000000L);
        setPrivateField(specialPopulation, "cityPercentage", 40.0);
        setPrivateField(specialPopulation, "noCityPercentage", 60.0);
        populations.add(specialPopulation);

        // ACT & ASSERT - Should handle special characters without exceptions
        assertDoesNotThrow(() -> population.outputPopulation(populations, "special-chars.md"));
    }

    @Test
    void testOutputSinglePopulationWithSpecialCharacters() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population specialPopulation = new Population();
        setPrivateField(specialPopulation, "Name", "Country & Name/With-Special|Chars");
        setPrivateField(specialPopulation, "totalPopulation", 50000000L);
        populations.add(specialPopulation);

        // ACT & ASSERT - Should handle special characters without exceptions
        assertDoesNotThrow(() -> population.outputSinglePopulation(populations, "special-chars-single.md"));
    }

    // Test printSinglePopulation with very long population numbers
    @Test
    void testPrintSinglePopulationWithLongNumbers() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();
        Population largePopulation = new Population();
        setPrivateField(largePopulation, "Name", "World");
        setPrivateField(largePopulation, "totalPopulation", 7800000000L);
        populations.add(largePopulation);

        // ACT & ASSERT - Should handle large numbers without exceptions
        assertDoesNotThrow(() -> population.printSinglePopulation(populations));
    }

    // Test outputPopulation with multiple populations
    @Test
    void testOutputPopulationWithMultiplePopulations() {
        // ARRANGE
        ArrayList<Population> populations = new ArrayList<>();

        Population pop1 = new Population();
        setPrivateField(pop1, "Name", "Asia");
        setPrivateField(pop1, "totalPopulation", 4500000000L);
        setPrivateField(pop1, "cityPopulation", 1800000000L);
        setPrivateField(pop1, "noCityPopulation", 2700000000L);
        setPrivateField(pop1, "cityPercentage", 40.0);
        setPrivateField(pop1, "noCityPercentage", 60.0);
        populations.add(pop1);

        Population pop2 = new Population();
        setPrivateField(pop2, "Name", "Europe");
        setPrivateField(pop2, "totalPopulation", 750000000L);
        setPrivateField(pop2, "cityPopulation", 500000000L);
        setPrivateField(pop2, "noCityPopulation", 250000000L);
        setPrivateField(pop2, "cityPercentage", 66.7);
        setPrivateField(pop2, "noCityPercentage", 33.3);
        populations.add(pop2);

        // ACT & ASSERT - Should handle multiple populations without exceptions
        assertDoesNotThrow(() -> population.outputPopulation(populations, "multiple-populations.md"));
    }

    // Helper method to get private field values using reflection
    private Object getPrivateField(Population population, String fieldName) {
        try {
            Field field = Population.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(population);
        } catch (Exception e) {
            fail("Failed to access private field: " + fieldName + ", error: " + e.getMessage());
            return null;
        }
    }

    // Helper method to set private field values using reflection
    private void setPrivateField(Population population, String fieldName, Object value) {
        try {
            Field field = Population.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(population, value);
        } catch (Exception e) {
            fail("Failed to set private field: " + fieldName + ", error: " + e.getMessage());
        }
    }
}