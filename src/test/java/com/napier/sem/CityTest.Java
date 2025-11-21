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

class CityTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private City city;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException {
        closeable = MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        city = new City(mockConnection);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
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
        // Setup mock result set behavior
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
    }

    @Test
    void testGetCitiesRegion() throws SQLException {
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
    }

    @Test
    void testGetCitiesCountry() throws SQLException {
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
    }

    @Test
    void testGetCitiesDistrict() throws SQLException {
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
    }

    @Test
    void testGetCitiesPopulation() throws SQLException {
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
    }

    @Test
    void testGetTopCitiesContinent() throws SQLException {
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
    }

    @Test
    void testGetTopCitiesRegion() throws SQLException {
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
    }

    @Test
    void testGetTopCitiesCountry() throws SQLException {
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
    }

    @Test
    void testGetTopCitiesDistrict() throws SQLException {
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
}