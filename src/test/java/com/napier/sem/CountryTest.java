package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for the Country class using Mockito Extension
 */
@ExtendWith(MockitoExtension.class)
class CountryTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country(mockConnection);
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
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetCountriesByRegion() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetTopCountriesInWorld() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetTopCountriesInContinent() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetTopCountriesInRegion() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        verify(mockPreparedStatement).executeQuery();
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
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No results expected with limit 0

        ArrayList<Country> countries = country.getTopCountriesInWorld(0);

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setInt(1, 0);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testEmptyResultSet() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No results

        ArrayList<Country> countries = country.getAllCountriesByPopulation();

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testSpecialCharactersInParameters() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testLargePopulationNumbers() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
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
        verify(mockPreparedStatement).executeQuery();
    }

    // ===== NEW TESTS ADDED FOR COMPLETE COVERAGE =====

    @Test
    void testExecuteCountryQueryNoParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Country> result = country.getAllCountriesByPopulation(); // Uses executeCountryQuery() with no params

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteCountryQueryWithStringParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Country> result = country.getCountriesByContinent("Europe");

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteCountryQueryWithIntParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Country> result = country.getTopCountriesInWorld(5);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteCountryQueryWithTwoParamsWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<Country> result = country.getTopCountriesInContinent("Asia", 3);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test extractCountry indirectly through public methods with null values
    @Test
    void testExtractCountryWithNullValues() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Code")).thenReturn(null);
        when(mockResultSet.getString("Name")).thenReturn(null);
        when(mockResultSet.getString("Continent")).thenReturn(null);
        when(mockResultSet.getString("Region")).thenReturn(null);
        when(mockResultSet.getInt("Population")).thenReturn(0);
        when(mockResultSet.getString("Capital")).thenReturn(null);

        // ACT
        ArrayList<Country> countries = country.getAllCountriesByPopulation();

        // ASSERT
        assertNotNull(countries);
        assertEquals(1, countries.size());
        assertNull(countries.get(0).code);
        assertNull(countries.get(0).name);
        assertNull(countries.get(0).continent);
        assertNull(countries.get(0).region);
        assertEquals(0, countries.get(0).population);
        assertNull(countries.get(0).capital);
    }

    // Test outputCountries method thoroughly
    @Test
    void testOutputCountriesWithNullList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> country.outputCountries(null, "test.md"));
    }

    @Test
    void testOutputCountriesWithEmptyList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> country.outputCountries(new ArrayList<>(), "test.md"));
    }

    @Test
    void testOutputCountriesWithNullCountryInList() {
        // ARRANGE
        ArrayList<Country> countries = new ArrayList<>();
        countries.add(null);
        Country validCountry = new Country();
        validCountry.code = "DEU";
        validCountry.name = "Germany";
        validCountry.continent = "Europe";
        validCountry.region = "Western Europe";
        validCountry.population = 83149300;
        validCountry.capital = "Berlin";
        countries.add(validCountry);

        // ACT & ASSERT - Should handle null gracefully
        assertDoesNotThrow(() -> country.outputCountries(countries, "test-null.md"));
    }

    @Test
    void testOutputCountriesSuccess() {
        // ARRANGE
        ArrayList<Country> countries = new ArrayList<>();
        Country country1 = new Country();
        country1.code = "FRA";
        country1.name = "France";
        country1.continent = "Europe";
        country1.region = "Western Europe";
        country1.population = 67391582;
        country1.capital = "Paris";
        countries.add(country1);

        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> country.outputCountries(countries, "success-test.md"));
    }

    @Test
    void testOutputCountriesIOException() {
        // ARRANGE
        ArrayList<Country> countries = new ArrayList<>();
        Country testCountry = new Country();
        testCountry.code = "TEST";
        testCountry.name = "Test Country";
        testCountry.continent = "Test Continent";
        testCountry.region = "Test Region";
        testCountry.population = 1000000;
        testCountry.capital = "Test Capital";
        countries.add(testCountry);

        // This test verifies the method handles IO exceptions gracefully
        assertDoesNotThrow(() -> country.outputCountries(countries, "/invalid/path/test.md"));
    }

    // Test SQLException for all query methods
    @Test
    void testGetCountriesByRegionSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<Country> countries = country.getCountriesByRegion("Western Europe");

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    void testGetTopCountriesInRegionSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<Country> countries = country.getTopCountriesInRegion("Western Europe", 5);

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    // Test edge cases for population limits
    @Test
    void testGetTopCountriesInWorldWithNegativeLimit() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No results for negative limit

        // ACT
        ArrayList<Country> countries = country.getTopCountriesInWorld(-1);

        // ASSERT
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setInt(1, -1);
    }

    // Test printCountries with various data scenarios
    @Test
    void testPrintCountriesWithVeryLongNames() {
        // ARRANGE
        ArrayList<Country> countries = new ArrayList<>();
        Country longCountry = new Country();
        longCountry.code = "LONG";
        longCountry.name = "Very Long Country Name That Exceeds Normal Length";
        longCountry.continent = "Continent With Extremely Long Name Too";
        longCountry.region = "Extremely Long Region Name Here";
        longCountry.population = 1234567890; // Large number
        longCountry.capital = "Very Long Capital City Name That Is Also Extremely Long";
        countries.add(longCountry);

        // ACT & ASSERT - Should handle formatting without exceptions
        assertDoesNotThrow(() -> country.printCountries(countries));
    }

    // Test all two-parameter methods with edge cases
    @Test
    void testGetTopCountriesInContinentWithZeroLimit() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Country> countries = country.getTopCountriesInContinent("Europe", 0);

        // ASSERT
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setString(1, "Europe");
        verify(mockPreparedStatement).setInt(2, 0);
    }

    @Test
    void testGetTopCountriesInRegionWithNegativeLimit() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Country> countries = country.getTopCountriesInRegion("Asia", -5);

        // ASSERT
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setString(1, "Asia");
        verify(mockPreparedStatement).setInt(2, -5);
    }

    // Test with empty string parameters
    @Test
    void testGetCountriesByContinentWithEmptyString() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Country> countries = country.getCountriesByContinent("");

        // ASSERT
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setString(1, "");
    }

    @Test
    void testGetCountriesByRegionWithEmptyString() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Country> countries = country.getCountriesByRegion("");

        // ASSERT
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setString(1, "");
    }

    // Test with null parameters (should handle gracefully or throw appropriate exception)
    @Test
    void testGetCountriesByContinentWithNull() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Country> countries = country.getCountriesByContinent(null);

        // ASSERT
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setString(1, null);
    }

    @Test
    void testGetCountriesByRegionWithNull() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<Country> countries = country.getCountriesByRegion(null);

        // ASSERT
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
        verify(mockPreparedStatement).setString(1, null);
    }

    // Test population formatting in print method
    @Test
    void testPrintCountriesWithZeroPopulation() {
        // ARRANGE
        ArrayList<Country> countries = new ArrayList<>();
        Country zeroPopCountry = new Country();
        zeroPopCountry.code = "ZERO";
        zeroPopCountry.name = "Zero Population Country";
        zeroPopCountry.continent = "Test Continent";
        zeroPopCountry.region = "Test Region";
        zeroPopCountry.population = 0;
        zeroPopCountry.capital = "Test Capital";
        countries.add(zeroPopCountry);

        // ACT & ASSERT - Should handle zero population without exceptions
        assertDoesNotThrow(() -> country.printCountries(countries));
    }

    // Test outputCountries with special characters
    @Test
    void testOutputCountriesWithSpecialCharacters() {
        // ARRANGE
        ArrayList<Country> countries = new ArrayList<>();
        Country specialCountry = new Country();
        specialCountry.code = "SP&L";
        specialCountry.name = "Country & Name";
        specialCountry.continent = "Continent/Region";
        specialCountry.region = "Region|Test";
        specialCountry.population = 1000000;
        specialCountry.capital = "Capital-City";
        countries.add(specialCountry);

        // ACT & ASSERT - Should handle special characters without exceptions
        assertDoesNotThrow(() -> country.outputCountries(countries, "special-chars.md"));
    }

    // Test printCountries with mixed data including nulls
    @Test
    void testPrintCountriesWithMixedData() {
        // ARRANGE
        ArrayList<Country> countries = new ArrayList<>();

        // Country with normal data
        Country normalCountry = new Country();
        normalCountry.code = "USA";
        normalCountry.name = "United States";
        normalCountry.continent = "North America";
        normalCountry.region = "North America";
        normalCountry.population = 331002651;
        normalCountry.capital = "Washington, D.C.";
        countries.add(normalCountry);

        // Country with very small population
        Country smallCountry = new Country();
        smallCountry.code = "VAT";
        smallCountry.name = "Vatican City";
        smallCountry.continent = "Europe";
        smallCountry.region = "Southern Europe";
        smallCountry.population = 825;
        smallCountry.capital = "Vatican City";
        countries.add(smallCountry);

        // ACT & ASSERT - Should handle mixed data without exceptions
        assertDoesNotThrow(() -> country.printCountries(countries));
    }

    // Test outputCountries with multiple countries
    @Test
    void testOutputCountriesWithMultipleCountries() {
        // ARRANGE
        ArrayList<Country> countries = new ArrayList<>();

        Country country1 = new Country();
        country1.code = "CAN";
        country1.name = "Canada";
        country1.continent = "North America";
        country1.region = "North America";
        country1.population = 37742154;
        country1.capital = "Ottawa";
        countries.add(country1);

        Country country2 = new Country();
        country2.code = "MEX";
        country2.name = "Mexico";
        country2.continent = "North America";
        country2.region = "Central America";
        country2.population = 128932753;
        country2.capital = "Mexico City";
        countries.add(country2);

        Country country3 = new Country();
        country3.code = "JPN";
        country3.name = "Japan";
        country3.continent = "Asia";
        country3.region = "Eastern Asia";
        country3.population = 126476461;
        country3.capital = "Tokyo";
        countries.add(country3);

        // ACT & ASSERT - Should handle multiple countries without exceptions
        assertDoesNotThrow(() -> country.outputCountries(countries, "multiple-countries.md"));
    }
}
