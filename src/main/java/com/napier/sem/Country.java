package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Country {
    public String code;
    public String name;
    public String continent;
    public String region;
    public int population;
    public String capital;

    private Connection con;

    public Country(Connection con) {
        this.con = con;
    }

    public Country() {
        //
    }

    //  All countries in the world
    public ArrayList<Country> getAllCountriesByPopulation() {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                        "FROM country JOIN city ON country.Capital = city.ID " +
                        "ORDER BY country.Population DESC";
        return executeCountryQuery(query);
    }

    //   All countries in a continent
    public ArrayList<Country> getCountriesByContinent(String continent) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                        "FROM country JOIN city ON country.Capital = city.ID " +
                        "WHERE country.Continent = ? " +
                        "ORDER BY country.Population DESC";
        return executeCountryQuery(query, continent);
    }

    //    All countries in a region
    public ArrayList<Country> getCountriesByRegion(String region) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                        "FROM country JOIN city ON country.Capital = city.ID " +
                        "WHERE country.Region = ? " +
                        "ORDER BY country.Population DESC";
        return executeCountryQuery(query, region);
    }

    //    Top N populated countries in the world
    public ArrayList<Country> getTopCountriesInWorld(int n) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                        "FROM country JOIN city ON country.Capital = city.ID " +
                        "ORDER BY country.Population DESC LIMIT ?";
        return executeCountryQueryWithLimit(query, n);
    }

    //    Top N populated countries in a continent
    public ArrayList<Country> getTopCountriesInContinent(String continent, int n) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                        "FROM country JOIN city ON country.Capital = city.ID " +
                        "WHERE country.Continent = ? " +
                        "ORDER BY country.Population DESC LIMIT ?";
        return executeCountryQuery(query, continent, n);
    }

    //    Top N populated countries in a region
    public ArrayList<Country> getTopCountriesInRegion(String region, int n) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, city.Name AS Capital " +
                        "FROM country JOIN city ON country.Capital = city.ID " +
                        "WHERE country.Region = ? " +
                        "ORDER BY country.Population DESC LIMIT ?";
        return executeCountryQuery(query, region, n);
    }


    //    Query without parameters
    private ArrayList<Country> executeCountryQuery(String query) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rset = stmt.executeQuery()) {
            while (rset.next()) {
                Country c = extractCountry(rset);
                countries.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return countries;
    }

    //    Query with one string parameter
    private ArrayList<Country> executeCountryQuery(String query, String param) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setString(1, param);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    Country c = extractCountry(rset);
                    countries.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return countries;
    }

    //    Query with one string + one integer parameter
    private ArrayList<Country> executeCountryQuery(String query, String param, int limit) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setString(1, param);
            stmt.setInt(2, limit);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    Country c = extractCountry(rset);
                    countries.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return countries;
    }


    //    Query with the only integer parameter (LIMIT)
    private ArrayList<Country> executeCountryQueryWithLimit(String query, int limit) {
        ArrayList<Country> countries = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setInt(1, limit);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    Country c = extractCountry(rset);
                    countries.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return countries;
    }

    //    Helper to extract a country record from ResultSet
    private Country extractCountry(ResultSet rset) throws SQLException {
        Country c = new Country();
        c.code = rset.getString("Code");
        c.name = rset.getString("Name");
        c.continent = rset.getString("Continent");
        c.region = rset.getString("Region");
        c.population = rset.getInt("Population");
        c.capital = rset.getString("Capital");
        return c;
    }

    // Print the Reports for country
    public void printCountries(ArrayList<Country> countries) {
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries found");
            return;
        }

        // Define column headers
        String[] headers = {"Code", "Name", "Continent", "Region", "Population", "Capital"};

        // Compute max width for each column
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (Country c : countries) {
            widths[0] = Math.max(widths[0], c.code.length());
            widths[1] = Math.max(widths[1], c.name.length());
            widths[2] = Math.max(widths[2], c.continent.length());
            widths[3] = Math.max(widths[3], c.region.length());
            widths[4] = Math.max(widths[4], String.valueOf(c.population).length());
            widths[5] = Math.max(widths[5], c.capital.length());
        }

        // Helper to print a border line
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
        for (Country c : countries) {
            System.out.printf("| %-" + widths[0] + "s ", c.code);
            System.out.printf("| %-" + widths[1] + "s ", c.name);
            System.out.printf("| %-" + widths[2] + "s ", c.continent);
            System.out.printf("| %-" + widths[3] + "s ", c.region);
            System.out.printf("| % " + widths[4] + "d ", c.population);
            System.out.printf("| %-" + widths[5] + "s ", c.capital);
            System.out.println("|");
        }

        printBorder.run();
    }

    /**
     * Outputs countries to a Markdown file in the ./reports/ directory.
     *
     * @param countries The list of countries to output.
     * @param filename  The name of the file (e.g., "countries.md"). Include the extension if desired.
     */
    public void outputCountries(ArrayList<Country> countries, String filename) {
        System.out.println("Starting outputCountries method...");
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries to output");
            return;
        }
        System.out.println("Countries list has " + countries.size() + " items. Generating Markdown...");

        StringBuilder sb = new StringBuilder();
        sb.append("| Code | Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        for (Country c : countries) {
            if (c == null) continue;
            sb.append("| " + c.code + " | " +
                    c.name + " | " + c.continent + " | " +
                    c.region + " | " + c.population + " | " +
                    c.capital + " |\r\n");
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