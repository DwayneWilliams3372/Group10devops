package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public ArrayList<City> getCitiesPopulation(int n) {
        String query =  "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "ORDER BY city.Population DESC LIMIT ? ";
        return executeCityQuery(query, n);
    }
//    The top N populated cities in a continent
    public ArrayList<City> getTopCitiesContinent(String continent, int n) {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Continent = ? " +
                "ORDER BY city.Population DESC LIMIT ? ";
        return executeCityQuery(query, continent, n);
    }

//    The top N populated cities in a region
    public ArrayList<City> getTopCitiesRegion(String region, int n) {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Region = ? " +
                "ORDER BY city.Population DESC LIMIT ? ";
        return executeCityQuery(query, region, n);
    }

//    The top N populated cities in a country
    public ArrayList<City> getTopCitiesCountry(String country, int n) {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE country.Name = ? " +
                "ORDER BY city.Population DESC LIMIT ? ";
        return executeCityQuery(query, country, n);
    }

//    The top N populated cities in a district
    public ArrayList<City> getTopCitiesDistrict(String district, int n) {
        String query = "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                "FROM city JOIN country ON city.CountryCode = country.Code " +
                "WHERE city.District = ? " +
                "ORDER BY city.Population DESC LIMIT ? ";
        return executeCityQuery(query, district, n);
    }

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

    //  Query with one integer parameter
    private ArrayList<City> executeCityQuery(String query, int n) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setInt(1, n);
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

    //  Query with two parameters
    private ArrayList<City> executeCityQuery(String query, String param, int n) {
        ArrayList<City> cities = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setString(1, param);
            stmt.setInt(2, n);
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

    public void outputCities(ArrayList<City> cities, String filename) {
        System.out.println("Starting outputCountries method...");
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities to output");
            return;
        }
        System.out.println("Countries list has " + cities.size() + " items. Generating Markdown...");

        StringBuilder sb = new StringBuilder();
        sb.append("| Name | Country | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");
        for (City c : cities) {
            if (c == null) continue;
            sb.append(" | " + c.name + " | " + c.country + " | " +
                    c.district + " | " + c.population + "|\r\n");
        }
        System.out.println("Markdown content generated. Attempting to write to file: /app/reports/" + filename);
        try {
            File reportsDir = new File("/app/reports/");  // Absolute path to match volume mount
            if (!reportsDir.exists()) {
                boolean dirCreated = reportsDir.mkdirs();
                System.out.println("Reports directory created: " + dirCreated + " at " + reportsDir.getAbsolutePath());
            }
            File outputFile = new File("/app/reports/" + filename);  // Absolute path
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(sb.toString());
            writer.close();
            System.out.println("File written successfully to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
