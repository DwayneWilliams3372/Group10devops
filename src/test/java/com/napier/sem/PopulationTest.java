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
        assertEquals(40.5, (Double) getPrivateField(asia, "cityPercentage"), 0.001);
        assertEquals(59.5, (Double) getPrivateField(asia, "noCityPercentage"), 0.001);

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
        assertEquals(53.3, (Double) getPrivateField(region, "cityPercentage"), 0.001);
        assertEquals(46.7, (Double) getPrivateField(region, "noCityPercentage"), 0.001);
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
        assertEquals(46.2, (Double) getPrivateField(country, "cityPercentage"), 0.001);
        assertEquals(53.8, (Double) getPrivateField(country, "noCityPercentage"), 0.001);
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
    void testPrintPopulationWithData() throws Exception {
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
    void testPrintSinglePopulationWithData() throws Exception {
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
        long pop1 = (Long) getPrivateField(populations.get(0), "totalPopulation");
        long pop2 = (Long) getPrivateField(populations.get(1), "totalPopulation");
        long pop3 = (Long) getPrivateField(populations.get(2), "totalPopulation");

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
        long totalPop = (Long) getPrivateField(region, "totalPopulation");
        long cityPop = (Long) getPrivateField(region, "cityPopulation");
        long noCityPop = (Long) getPrivateField(region, "noCityPopulation");
        double cityPct = (Double) getPrivateField(region, "cityPercentage");
        double noCityPct = (Double) getPrivateField(region, "noCityPercentage");

        // Verify data consistency
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
        assertEquals(0.0, (Double) getPrivateField(country, "cityPercentage"), 0.001);
        assertEquals(0.0, (Double) getPrivateField(country, "noCityPercentage"), 0.001);
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