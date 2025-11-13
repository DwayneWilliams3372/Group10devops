package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class City {
    public String name;
    public String country;
    public String district;
    public int population;

    private Connection con;

    public City(Connection con) {
        this.con = con;
    }

    public City () {
        //
    }

//    All the cities in the world
    public ArrayList<City> getCities() {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
        "FROM city JOIN country ON city.CountryCode = country.Code " +
        "ORDER BY city.Population DESC ";
        return executeCityQuery(query);
    }
//    All the cities in a continent.
    public ArrayList<City> getCitiesContinent(String continent) {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Continent = ? " +
                "ORDER BY city.Population DESC";
        return executeCityQuery(query, continent);
    }
//    All the cities in a region
    public ArrayList<City> getCitiesRegion(String region) {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Region = ? " +
                "ORDER BY city.Population DESC";
        return executeCityQuery(query, region);
    }

//    All the cities in a country
    public ArrayList<City> getCitiesCountry(String country) {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Name = ? " +
                "ORDER BY city.Population DESC";
        return executeCityQuery(query, country);
    }

//    All the cities in a district
    public ArrayList<City> getCitiesDistrict(String district) {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE city.District = ? " +
                "ORDER BY city.Population DESC";
        return executeCityQuery(query, district);
    }

//    The top N populated cities in the world
//    The top N populated cities in a continent
//    The top N populated cities in a region
//    The top N populated cities in a country
//    The top N populated cities in a district

    //    Query without parameters
    private ArrayList<City> executeCityQuery(String query) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rset = stmt.executeQuery()) {
            while (rset.next()) {
                City c = extractCity(rset);
                cities.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return cities;
    }

    //    Query with one string parameter
    private ArrayList<City> executeCityQuery(String query, String param) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setString(1, param);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    City c = extractCity(rset);
                    cities.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return cities;
    }

    //    Helper to extract a city record from ResultSet
    private City extractCity(ResultSet rset) throws SQLException {
        City c = new City();
        c.name = rset.getString("Name");
        c.country = rset.getString("Country");
        c.population = rset.getInt("Population");
        c.district = rset.getString("District");
        return c;
    }

    // Print the Reports for city
    public void printCities(ArrayList<City> cities) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found");
            return;
        }

        // Define column headers
        String[] headers = {"Name", "Country", "District", "Population"};

        // Compute max width for each column
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (City c : cities) {
            widths[0] = Math.max(widths[0], c.name.length());
            widths[1] = Math.max(widths[1], c.country.length());
            widths[2] = Math.max(widths[2], c.district.length());
            widths[3] = Math.max(widths[3], String.valueOf(c.population).length());
        }

        // Helper to print a borderline
        Runnable printBorder = () -> {
            for (int w : widths) {
                System.out.print("+" + "-".repeat(w + 2));
            }
            System.out.println("+");
        };

        // Print header
        printBorder.run();
        for (int i = 0; i < headers.length; i++) {
            System.out.printf("| %-" + widths[i] + "s ", headers[i]);
        }
        System.out.println("|");
        printBorder.run();

        // Print rows
        for (City c : cities) {
            System.out.printf("| %-" + widths[0] + "s ", c.name);
            System.out.printf("| %-" + widths[1] + "s ", c.country);
            System.out.printf("| %-" + widths[2] + "s ", c.district);
            System.out.printf("| %-" + widths[3] + "s ", c.population);
            System.out.println("|");
        }

        printBorder.run();
    }
}
