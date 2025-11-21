package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CountryTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private Country country;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException {
        closeable = MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        country = new Country(mockConnection);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testDefaultConstructor() {
        Country defaultCountry = new Country();
        assertNotNull(defaultCountry);
    }

    @Test
    void testConnectionConstructor() {
        assertNotNull(country);
    }

    @Test
    void testGetAllCountriesByPopulation() throws SQLException {
        // Setup mock result set behavior
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("Code")).thenReturn("CHN", "IND");
        when(mockResultSet.getString("Name")).thenReturn("China", "India");
        when(mockResultSet.getString("Continent")).thenReturn("Asia", "Asia");
        when(mockResultSet.getString("Region")).thenReturn("Eastern Asia", "Southern and Central Asia");
        when(mockResultSet.getInt("Population")).thenReturn(1277558000, 1013662000);
        when(mockResultSet.getString("Capital")).thenReturn("Peking", "New Delhi");

        ArrayList<Country> countries = country.getAllCountriesByPopulation();

        assertNotNull(countries);
        assertEquals(2, countries.size());

        Country firstCountry = countries.get(0);
        assertEquals("CHN", firstCountry.code);
        assertEquals("China", firstCountry.name);
        assertEquals("Asia", firstCountry.continent);
        assertEquals("Eastern Asia", firstCountry.region);
        assertEquals(1277558000, firstCountry.population);
        assertEquals("Peking", firstCountry.capital);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetCountriesByContinent() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Code")).thenReturn("DEU");
        when(mockResultSet.getString("Name")).thenReturn("Germany");
        when(mockResultSet.getString("Continent")).thenReturn("Europe");
        when(mockResultSet.getString("Region")).thenReturn("Western Europe");
        when(mockResultSet.getInt("Population")).thenReturn(82164700);
        when(mockResultSet.getString("Capital")).thenReturn("Berlin");

        ArrayList<Country> countries = country.getCountriesByContinent("Europe");

        assertNotNull(countries);
        assertEquals(1, countries.size());

        Country resultCountry = countries.get(0);
        assertEquals("DEU", resultCountry.code);
        assertEquals("Germany", resultCountry.name);
        assertEquals("Europe", resultCountry.continent);
        assertEquals("Western Europe", resultCountry.region);
        assertEquals(82164700, resultCountry.population);
        assertEquals("Berlin", resultCountry.capital);

        verify(mockPreparedStatement).setString(1, "Europe");
    }

    @Test
    void testGetCountriesByRegion() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Code")).thenReturn("EGY");
        when(mockResultSet.getString("Name")).thenReturn("Egypt");
        when(mockResultSet.getString("Continent")).thenReturn("Africa");
        when(mockResultSet.getString("Region")).thenReturn("Northern Africa");
        when(mockResultSet.getInt("Population")).thenReturn(68470000);
        when(mockResultSet.getString("Capital")).thenReturn("Cairo");

        ArrayList<Country> countries = country.getCountriesByRegion("Northern Africa");

        assertNotNull(countries);
        assertEquals(1, countries.size());

        Country resultCountry = countries.get(0);
        assertEquals("EGY", resultCountry.code);
        assertEquals("Egypt", resultCountry.name);
        assertEquals("Africa", resultCountry.continent);
        assertEquals("Northern Africa", resultCountry.region);
        assertEquals(68470000, resultCountry.population);
        assertEquals("Cairo", resultCountry.capital);

        verify(mockPreparedStatement).setString(1, "Northern Africa");
    }

    @Test
    void testGetTopCountriesInWorld() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("Code")).thenReturn("USA", "IDN");
        when(mockResultSet.getString("Name")).thenReturn("United States", "Indonesia");
        when(mockResultSet.getString("Continent")).thenReturn("North America", "Asia");
        when(mockResultSet.getString("Region")).thenReturn("North America", "Southeast Asia");
        when(mockResultSet.getInt("Population")).thenReturn(278357000, 212107000);
        when(mockResultSet.getString("Capital")).thenReturn("Washington", "Jakarta");

        ArrayList<Country> countries = country.getTopCountriesInWorld(5);

        assertNotNull(countries);
        assertEquals(2, countries.size());

        Country firstCountry = countries.get(0);
        assertEquals("USA", firstCountry.code);
        assertEquals("United States", firstCountry.name);
        assertEquals("North America", firstCountry.continent);
        assertEquals("North America", firstCountry.region);
        assertEquals(278357000, firstCountry.population);
        assertEquals("Washington", firstCountry.capital);

        verify(mockPreparedStatement).setInt(1, 5);
    }

    @Test
    void testGetTopCountriesInContinent() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Code")).thenReturn("BRA");
        when(mockResultSet.getString("Name")).thenReturn("Brazil");
        when(mockResultSet.getString("Continent")).thenReturn("South America");
        when(mockResultSet.getString("Region")).thenReturn("South America");
        when(mockResultSet.getInt("Population")).thenReturn(170115000);
        when(mockResultSet.getString("Capital")).thenReturn("Brasília");

        ArrayList<Country> countries = country.getTopCountriesInContinent("South America", 3);

        assertNotNull(countries);
        assertEquals(1, countries.size());

        Country resultCountry = countries.get(0);
        assertEquals("BRA", resultCountry.code);
        assertEquals("Brazil", resultCountry.name);
        assertEquals("South America", resultCountry.continent);
        assertEquals("South America", resultCountry.region);
        assertEquals(170115000, resultCountry.population);
        assertEquals("Brasília", resultCountry.capital);

        verify(mockPreparedStatement).setString(1, "South America");
        verify(mockPreparedStatement).setInt(2, 3);
    }

    @Test
    void testGetTopCountriesInRegion() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Code")).thenReturn("AUS");
        when(mockResultSet.getString("Name")).thenReturn("Australia");
        when(mockResultSet.getString("Continent")).thenReturn("Oceania");
        when(mockResultSet.getString("Region")).thenReturn("Australia and New Zealand");
        when(mockResultSet.getInt("Population")).thenReturn(18886000);
        when(mockResultSet.getString("Capital")).thenReturn("Canberra");

        ArrayList<Country> countries = country.getTopCountriesInRegion("Australia and New Zealand", 2);

        assertNotNull(countries);
        assertEquals(1, countries.size());

        Country resultCountry = countries.get(0);
        assertEquals("AUS", resultCountry.code);
        assertEquals("Australia", resultCountry.name);
        assertEquals("Oceania", resultCountry.continent);
        assertEquals("Australia and New Zealand", resultCountry.region);
        assertEquals(18886000, resultCountry.population);
        assertEquals("Canberra", resultCountry.capital);

        verify(mockPreparedStatement).setString(1, "Australia and New Zealand");
        verify(mockPreparedStatement).setInt(2, 2);
    }

    @Test
    void testPrintCountriesEmptyList() {
        // This test just ensures no exception is thrown with empty list
        ArrayList<Country> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> country.printCountries(emptyList));
    }

    @Test
    void testPrintCountriesNull() {
        // This test just ensures no exception is thrown with null
        assertDoesNotThrow(() -> country.printCountries(null));
    }

    @Test
    void testPrintCountriesWithData() {
        ArrayList<Country> countries = new ArrayList<>();
        Country testCountry = new Country();
        testCountry.code = "TEST";
        testCountry.name = "Test Country";
        testCountry.continent = "Test Continent";
        testCountry.region = "Test Region";
        testCountry.population = 1000000;
        testCountry.capital = "Test Capital";
        countries.add(testCountry);

        // This test just ensures no exception is thrown
        assertDoesNotThrow(() -> country.printCountries(countries));
    }

    @Test
    void testSQLExceptionHandling() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<Country> countries = country.getAllCountriesByPopulation();

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    void testCountryFieldAssignment() {
        // Test that Country fields can be assigned directly
        Country testCountry = new Country();
        testCountry.code = "TST";
        testCountry.name = "Test Country";
        testCountry.continent = "Test Continent";
        testCountry.region = "Test Region";
        testCountry.population = 5000000;
        testCountry.capital = "Test Capital";

        assertEquals("TST", testCountry.code);
        assertEquals("Test Country", testCountry.name);
        assertEquals("Test Continent", testCountry.continent);
        assertEquals("Test Region", testCountry.region);
        assertEquals(5000000, testCountry.population);
        assertEquals("Test Capital", testCountry.capital);
    }

    @Test
    void testMultipleCountriesInResultSet() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, true, false);
        when(mockResultSet.getString("Code")).thenReturn("FRA", "GBR", "ITA");
        when(mockResultSet.getString("Name")).thenReturn("France", "United Kingdom", "Italy");
        when(mockResultSet.getString("Continent")).thenReturn("Europe", "Europe", "Europe");
        when(mockResultSet.getString("Region")).thenReturn("Western Europe", "British Islands", "Southern Europe");
        when(mockResultSet.getInt("Population")).thenReturn(59225700, 59623400, 57680000);
        when(mockResultSet.getString("Capital")).thenReturn("Paris", "London", "Roma");

        ArrayList<Country> countries = country.getAllCountriesByPopulation();

        assertEquals(3, countries.size());

        // Verify all countries have correct data
        assertEquals("FRA", countries.get(0).code);
        assertEquals("GBR", countries.get(1).code);
        assertEquals("ITA", countries.get(2).code);

        assertEquals("France", countries.get(0).name);
        assertEquals("United Kingdom", countries.get(1).name);
        assertEquals("Italy", countries.get(2).name);
    }

    @Test
    void testZeroLimitParameter() throws SQLException {
        when(mockResultSet.next()).thenReturn(false); // No results expected with limit 0

        ArrayList<Country> countries = country.getTopCountriesInWorld(0);

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setInt(1, 0);
    }

    @Test
    void testEmptyResultSet() throws SQLException {
        when(mockResultSet.next()).thenReturn(false); // No results

        ArrayList<Country> countries = country.getAllCountriesByPopulation();

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    void testSpecialCharactersInParameters() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Code")).thenReturn("RUS");
        when(mockResultSet.getString("Name")).thenReturn("Russia");
        when(mockResultSet.getString("Continent")).thenReturn("Europe");
        when(mockResultSet.getString("Region")).thenReturn("Eastern Europe");
        when(mockResultSet.getInt("Population")).thenReturn(146934000);
        when(mockResultSet.getString("Capital")).thenReturn("Moscow");

        // Test with region containing special characters or spaces
        ArrayList<Country> countries = country.getCountriesByRegion("Eastern Europe");

        assertNotNull(countries);
        assertEquals(1, countries.size());
        verify(mockPreparedStatement).setString(1, "Eastern Europe");
    }

    @Test
    void testLargePopulationNumbers() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Code")).thenReturn("CHN");
        when(mockResultSet.getString("Name")).thenReturn("China");
        when(mockResultSet.getString("Continent")).thenReturn("Asia");
        when(mockResultSet.getString("Region")).thenReturn("Eastern Asia");
        when(mockResultSet.getInt("Population")).thenReturn(Integer.MAX_VALUE); // Very large population
        when(mockResultSet.getString("Capital")).thenReturn("Beijing");

        ArrayList<Country> countries = country.getAllCountriesByPopulation();

        assertNotNull(countries);
        assertEquals(1, countries.size());
        assertEquals(Integer.MAX_VALUE, countries.get(0).population);
    }
}