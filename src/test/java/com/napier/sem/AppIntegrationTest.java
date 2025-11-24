package com.napier.sem;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppIntegrationTest {

    private Connection con;
    private Country countryDao;
    private Capital capitalDao;  // New DAO

    @BeforeAll
    void setupDatabase() throws SQLException {
        // Connect to the Docker MySQL world database
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:33060/world?useSSL=false&serverTimezone=UTC",
                "root",  // MySQL user
                ""       // empty password
        );

        countryDao = new Country(con);
        capitalDao = new Capital(con);  // Initialize the Capital DAO
    }

    @AfterAll
    void closeDatabase() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }

    // ----------------- Country Tests -----------------
    @Test
    void testGetAllCountriesByPopulation() {
        ArrayList<Country> countries = countryDao.getAllCountriesByPopulation();
        assertNotNull(countries);
        assertFalse(countries.isEmpty());

        for (int i = 0; i < countries.size() - 1; i++) {
            assertTrue(countries.get(i).population >= countries.get(i + 1).population);
        }
    }

    @Test
    void testGetCountriesByContinent() {
        ArrayList<Country> countries = countryDao.getCountriesByContinent("Europe");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        for (Country c : countries) {
            assertEquals("Europe", c.continent);
        }
    }

    @Test
    void testGetTopCountriesInWorld() {
        ArrayList<Country> top3 = countryDao.getTopCountriesInWorld(3);
        assertEquals(3, top3.size());
        assertTrue(top3.get(0).population >= top3.get(1).population);
    }

    // ----------------- Capital Tests -----------------
    @Test
    void testGetCapitals() {
        ArrayList<Capital> capitals = capitalDao.getCapitals();
        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());
        System.out.println("Capitals count: " + capitals.size());
    }

    @Test
    void testGetCapitalsContinent() {
        ArrayList<Capital> euroCapitals = capitalDao.getCapitalsContinent("Europe");
        assertNotNull(euroCapitals);
        assertFalse(euroCapitals.isEmpty());
        for (Capital c : euroCapitals) {
            assertNotNull(c.name);
        }
    }

    @Test
    void testTopCapitalsPopulation() {
        ArrayList<Capital> top3 = capitalDao.getCapitalsPopulation(3);
        assertNotNull(top3);
        assertEquals(3, top3.size());
        assertTrue(top3.get(0).population >= top3.get(1).population);
    }
}
