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
 * Unit Tests for the City class using Mockito Extension
 */
@ExtendWith(MockitoExtension.class)
class CityTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private City city;

    @BeforeEach
    void setUp() {
        city = new City(mockConnection);
    }

    @Test
    void testDefaultConstructor() {
        City defaultCity = new City();
        assertNotNull(defaultCity);
    }

    @Test
    void testConnectionConstructor() {
        assertNotNull(city);
    }

    @Test
    void testGetCities() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("Name")).thenReturn("Tokyo", "Delhi");
        when(mockResultSet.getString("Country")).thenReturn("Japan", "India");
        when(mockResultSet.getString("District")).thenReturn("Tokyo-to", "Delhi");
        when(mockResultSet.getInt("Population")).thenReturn(37400068, 29399141);

        ArrayList<City> cities = city.getCities();

        assertNotNull(cities);
        assertEquals(2, cities.size());

        City firstCity = cities.get(0);
        assertEquals("Tokyo", firstCity.name);
        assertEquals("Japan", firstCity.country);
        assertEquals("Tokyo-to", firstCity.district);
        assertEquals(37400068, firstCity.population);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetCitiesContinent() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Berlin");
        when(mockResultSet.getString("Country")).thenReturn("Germany");
        when(mockResultSet.getString("District")).thenReturn("Berliini");
        when(mockResultSet.getInt("Population")).thenReturn(3520031);

        ArrayList<City> cities = city.getCitiesContinent("Europe");

        assertNotNull(cities);
        assertEquals(1, cities.size());

        City resultCity = cities.get(0);
        assertEquals("Berlin", resultCity.name);
        assertEquals("Germany", resultCity.country);
        assertEquals("Berliini", resultCity.district);
        assertEquals(3520031, resultCity.population);

        verify(mockPreparedStatement).setString(1, "Europe");
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetCitiesRegion() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Cairo");
        when(mockResultSet.getString("Country")).thenReturn("Egypt");
        when(mockResultSet.getString("District")).thenReturn("Kairo");
        when(mockResultSet.getInt("Population")).thenReturn(6789479);

        ArrayList<City> cities = city.getCitiesRegion("Northern Africa");

        assertNotNull(cities);
        assertEquals(1, cities.size());

        City resultCity = cities.get(0);
        assertEquals("Cairo", resultCity.name);
        assertEquals("Egypt", resultCity.country);
        assertEquals("Kairo", resultCity.district);
        assertEquals(6789479, resultCity.population);

        verify(mockPreparedStatement).setString(1, "Northern Africa");
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetCitiesCountry() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("London");
        when(mockResultSet.getString("Country")).thenReturn("United Kingdom");
        when(mockResultSet.getString("District")).thenReturn("England");
        when(mockResultSet.getInt("Population")).thenReturn(7285000);

        ArrayList<City> cities = city.getCitiesCountry("United Kingdom");

        assertNotNull(cities);
        assertEquals(1, cities.size());

        City resultCity = cities.get(0);
        assertEquals("London", resultCity.name);
        assertEquals("United Kingdom", resultCity.country);
        assertEquals("England", resultCity.district);
        assertEquals(7285000, resultCity.population);

        verify(mockPreparedStatement).setString(1, "United Kingdom");
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetCitiesDistrict() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Manhattan");
        when(mockResultSet.getString("Country")).thenReturn("United States");
        when(mockResultSet.getString("District")).thenReturn("New York");
        when(mockResultSet.getInt("Population")).thenReturn(1628706);

        ArrayList<City> cities = city.getCitiesDistrict("New York");

        assertNotNull(cities);
        assertEquals(1, cities.size());

        City resultCity = cities.get(0);
        assertEquals("Manhattan", resultCity.name);
        assertEquals("United States", resultCity.country);
        assertEquals("New York", resultCity.district);
        assertEquals(1628706, resultCity.population);

        verify(mockPreparedStatement).setString(1, "New York");
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetCitiesPopulation() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("Name")).thenReturn("Shanghai", "Beijing");
        when(mockResultSet.getString("Country")).thenReturn("China", "China");
        when(mockResultSet.getString("District")).thenReturn("Shanghai", "Peking");
        when(mockResultSet.getInt("Population")).thenReturn(22315474, 11716620);

        ArrayList<City> cities = city.getCitiesPopulation(5);

        assertNotNull(cities);
        assertEquals(2, cities.size());

        City firstCity = cities.get(0);
        assertEquals("Shanghai", firstCity.name);
        assertEquals("China", firstCity.country);
        assertEquals("Shanghai", firstCity.district);
        assertEquals(22315474, firstCity.population);

        verify(mockPreparedStatement).setInt(1, 5);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetTopCitiesContinent() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Paris");
        when(mockResultSet.getString("Country")).thenReturn("France");
        when(mockResultSet.getString("District")).thenReturn("Île-de-France");
        when(mockResultSet.getInt("Population")).thenReturn(2138551);

        ArrayList<City> cities = city.getTopCitiesContinent("Europe", 3);

        assertNotNull(cities);
        assertEquals(1, cities.size());

        City resultCity = cities.get(0);
        assertEquals("Paris", resultCity.name);
        assertEquals("France", resultCity.country);
        assertEquals("Île-de-France", resultCity.district);
        assertEquals(2138551, resultCity.population);

        verify(mockPreparedStatement).setString(1, "Europe");
        verify(mockPreparedStatement).setInt(2, 3);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetTopCitiesRegion() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Sydney");
        when(mockResultSet.getString("Country")).thenReturn("Australia");
        when(mockResultSet.getString("District")).thenReturn("New South Wales");
        when(mockResultSet.getInt("Population")).thenReturn(3278000);

        ArrayList<City> cities = city.getTopCitiesRegion("Australia and New Zealand", 2);

        assertNotNull(cities);
        assertEquals(1, cities.size());

        City resultCity = cities.get(0);
        assertEquals("Sydney", resultCity.name);
        assertEquals("Australia", resultCity.country);
        assertEquals("New South Wales", resultCity.district);
        assertEquals(3278000, resultCity.population);

        verify(mockPreparedStatement).setString(1, "Australia and New Zealand");
        verify(mockPreparedStatement).setInt(2, 2);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetTopCitiesCountry() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Toronto");
        when(mockResultSet.getString("Country")).thenReturn("Canada");
        when(mockResultSet.getString("District")).thenReturn("Ontario");
        when(mockResultSet.getInt("Population")).thenReturn(4612191);

        ArrayList<City> cities = city.getTopCitiesCountry("Canada", 4);

        assertNotNull(cities);
        assertEquals(1, cities.size());

        City resultCity = cities.get(0);
        assertEquals("Toronto", resultCity.name);
        assertEquals("Canada", resultCity.country);
        assertEquals("Ontario", resultCity.district);
        assertEquals(4612191, resultCity.population);

        verify(mockPreparedStatement).setString(1, "Canada");
        verify(mockPreparedStatement).setInt(2, 4);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetTopCitiesDistrict() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Brooklyn");
        when(mockResultSet.getString("Country")).thenReturn("United States");
        when(mockResultSet.getString("District")).thenReturn("New York");
        when(mockResultSet.getInt("Population")).thenReturn(2559903);

        ArrayList<City> cities = city.getTopCitiesDistrict("New York", 3);

        assertNotNull(cities);
        assertEquals(1, cities.size());

        City resultCity = cities.get(0);
        assertEquals("Brooklyn", resultCity.name);
        assertEquals("United States", resultCity.country);
        assertEquals("New York", resultCity.district);
        assertEquals(2559903, resultCity.population);

        verify(mockPreparedStatement).setString(1, "New York");
        verify(mockPreparedStatement).setInt(2, 3);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testPrintCitiesEmptyList() {
        // This test just ensures no exception is thrown with empty list
        ArrayList<City> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> city.printCities(emptyList));
    }

    @Test
    void testPrintCitiesNull() {
        // This test just ensures no exception is thrown with null
        assertDoesNotThrow(() -> city.printCities(null));
    }

    @Test
    void testPrintCitiesWithData() {
        ArrayList<City> cities = new ArrayList<>();
        City testCity = new City();
        testCity.name = "TestCity";
        testCity.country = "TestCountry";
        testCity.district = "TestDistrict";
        testCity.population = 1234567;
        cities.add(testCity);

        // This test just ensures no exception is thrown
        assertDoesNotThrow(() -> city.printCities(cities));
    }

    @Test
    void testSQLExceptionHandling() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<City> cities = city.getCities();

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testCityFieldAssignment() {
        // Test that City fields can be assigned directly
        City testCity = new City();
        testCity.name = "Test City";
        testCity.country = "Test Country";
        testCity.district = "Test District";
        testCity.population = 1000000;

        assertEquals("Test City", testCity.name);
        assertEquals("Test Country", testCity.country);
        assertEquals("Test District", testCity.district);
        assertEquals(1000000, testCity.population);
    }

    @Test
    void testMultipleCitiesInResultSet() throws SQLException {
        // Setup mock behavior for this specific test
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, true, false);
        when(mockResultSet.getString("Name")).thenReturn("City1", "City2", "City3");
        when(mockResultSet.getString("Country")).thenReturn("Country1", "Country2", "Country3");
        when(mockResultSet.getString("District")).thenReturn("District1", "District2", "District3");
        when(mockResultSet.getInt("Population")).thenReturn(1000000, 2000000, 3000000);

        ArrayList<City> cities = city.getCities();

        assertEquals(3, cities.size());

        // Verify all cities have correct data
        assertEquals("City1", cities.get(0).name);
        assertEquals("City2", cities.get(1).name);
        assertEquals("City3", cities.get(2).name);
    }

    // ===== NEW TESTS ADDED FOR COMPLETE COVERAGE =====

    @Test
    void testExecuteCityQueryNoParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<City> result = city.getCities(); // Uses executeCityQuery() with no params

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteCityQueryWithStringParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<City> result = city.getCitiesContinent("Europe");

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteCityQueryWithIntParamWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<City> result = city.getCitiesPopulation(5);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteCityQueryWithTwoParamsWithSQLException() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        // ACT
        ArrayList<City> result = city.getTopCitiesContinent("Asia", 3);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test extractCity indirectly through public methods with null values
    @Test
    void testExtractCityWithNullValues() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("Name")).thenReturn(null);
        when(mockResultSet.getString("Country")).thenReturn(null);
        when(mockResultSet.getString("District")).thenReturn(null);
        when(mockResultSet.getInt("Population")).thenReturn(0);

        // ACT
        ArrayList<City> cities = city.getCities();

        // ASSERT
        assertNotNull(cities);
        assertEquals(1, cities.size());
        assertNull(cities.get(0).name);
        assertNull(cities.get(0).country);
        assertNull(cities.get(0).district);
        assertEquals(0, cities.get(0).population);
    }

    // Test outputCities method thoroughly
    @Test
    void testOutputCitiesWithNullList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> city.outputCities(null, "test.md"));
    }

    @Test
    void testOutputCitiesWithEmptyList() {
        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> city.outputCities(new ArrayList<>(), "test.md"));
    }

    @Test
    void testOutputCitiesWithNullCityInList() {
        // ARRANGE
        ArrayList<City> cities = new ArrayList<>();
        cities.add(null);
        City validCity = new City();
        validCity.name = "Berlin";
        validCity.country = "Germany";
        validCity.district = "Berlin";
        validCity.population = 3644826;
        cities.add(validCity);

        // ACT & ASSERT - Should handle null gracefully
        assertDoesNotThrow(() -> city.outputCities(cities, "test-null.md"));
    }

    @Test
    void testOutputCitiesSuccess() {
        // ARRANGE
        ArrayList<City> cities = new ArrayList<>();
        City city1 = new City();
        city1.name = "Madrid";
        city1.country = "Spain";
        city1.district = "Madrid";
        city1.population = 3223334;
        cities.add(city1);

        // ACT & ASSERT - Should not throw exception
        assertDoesNotThrow(() -> city.outputCities(cities, "success-test.md"));
    }

    @Test
    void testOutputCitiesIOException() {
        // ARRANGE
        ArrayList<City> cities = new ArrayList<>();
        City testCity = new City();
        testCity.name = "Test";
        testCity.country = "TestCountry";
        testCity.district = "TestDistrict";
        testCity.population = 1000000;
        cities.add(testCity);

        // This test verifies the method handles IO exceptions gracefully
        assertDoesNotThrow(() -> city.outputCities(cities, "/invalid/path/test.md"));
    }

    // Test SQLException for all query methods
    @Test
    void testGetCitiesCountrySQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<City> cities = city.getCitiesCountry("United Kingdom");

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetCitiesDistrictSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<City> cities = city.getCitiesDistrict("California");

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetTopCitiesRegionSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<City> cities = city.getTopCitiesRegion("Western Europe", 5);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetTopCitiesCountrySQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<City> cities = city.getTopCitiesCountry("France", 3);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetTopCitiesDistrictSQLException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<City> cities = city.getTopCitiesDistrict("Texas", 2);

        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    // Test edge cases for population limits
    @Test
    void testGetCitiesPopulationWithZero() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No results for zero limit

        // ACT
        ArrayList<City> cities = city.getCitiesPopulation(0);

        // ASSERT
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
        verify(mockPreparedStatement).setInt(1, 0);
    }

    @Test
    void testGetCitiesPopulationWithNegative() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No results for negative limit

        // ACT
        ArrayList<City> cities = city.getCitiesPopulation(-1);

        // ASSERT
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
        verify(mockPreparedStatement).setInt(1, -1);
    }

    // Test printCities with various data scenarios
    @Test
    void testPrintCitiesWithVeryLongNames() {
        // ARRANGE
        ArrayList<City> cities = new ArrayList<>();
        City longCity = new City();
        longCity.name = "Very Long City Name That Exceeds Normal Length";
        longCity.country = "Country With Long Name Too";
        longCity.district = "Extremely Long District Name Here";
        longCity.population = 1234567890; // Large number
        cities.add(longCity);

        // ACT & ASSERT - Should handle formatting without exceptions
        assertDoesNotThrow(() -> city.printCities(cities));
    }

    // Test all two-parameter methods with edge cases
    @Test
    void testGetTopCitiesContinentWithZeroLimit() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<City> cities = city.getTopCitiesContinent("Europe", 0);

        // ASSERT
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
        verify(mockPreparedStatement).setString(1, "Europe");
        verify(mockPreparedStatement).setInt(2, 0);
    }

    @Test
    void testGetTopCitiesRegionWithNegativeLimit() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<City> cities = city.getTopCitiesRegion("Asia", -5);

        // ASSERT
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
        verify(mockPreparedStatement).setString(1, "Asia");
        verify(mockPreparedStatement).setInt(2, -5);
    }

    @Test
    void testGetTopCitiesCountryWithZeroLimit() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<City> cities = city.getTopCitiesCountry("Germany", 0);

        // ASSERT
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
        verify(mockPreparedStatement).setString(1, "Germany");
        verify(mockPreparedStatement).setInt(2, 0);
    }

    @Test
    void testGetTopCitiesDistrictWithNegativeLimit() throws SQLException {
        // ARRANGE
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // ACT
        ArrayList<City> cities = city.getTopCitiesDistrict("California", -3);

        // ASSERT
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
        verify(mockPreparedStatement).setString(1, "California");
        verify(mockPreparedStatement).setInt(2, -3);
    }
}