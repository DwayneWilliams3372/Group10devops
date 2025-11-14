package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class Population {
    private String Name;
    private long totalPopulation;
    private long cityPopulation;
    private long noCityPopulation;
    private double cityPercentage;
    private double noCityPercentage;

    private Connection con;
    public Population(Connection con) {
        this.con = con;
    }
    public Population() {
        //
    }

    // Continent
    // So, total population is sum of population in country table.
    // Total population for people living in cities is the sum of population in City table
    // The difference between sum of population (country) and sum of population (city) is the rest of people not living in cities.
    public ArrayList<Population> continentPopulation() {
        String query = "SELECT country.Continent AS Name, " +
                "SUM(country.Population) AS Total_Population, " +
                "SUM(city.Population) AS City_Population, " +
                "SUM(country.Population) - SUM(city.Population) AS No_City_Population, " +
                "(SUM(city.Population) / SUM(country.Population)) * 100 AS City_Percentage, " +
                "((SUM(country.Population) - SUM(city.Population)) / SUM(country.Population)) * 100 AS No_City_Percentage " +
                "FROM country JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Continent " +
                "ORDER BY Total_Population DESC";
        return getPopulation(query);
    }

    public ArrayList<Population> regionPopulation() {
        String query = "SELECT country.Region AS Name, " +
                "SUM(country.Population) AS Total_Population, " +
                "SUM(city.Population) AS City_Population, " +
                "SUM(country.Population) - SUM(city.Population) AS No_City_Population, " +
                "(SUM(city.Population) / SUM(country.Population)) * 100 AS City_Percentage, " +
                "((SUM(country.Population) - SUM(city.Population)) / SUM(country.Population)) * 100 AS No_City_Percentage " +
                "FROM country JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Region " +
                "ORDER BY Total_Population DESC";
        return getPopulation(query);
    }

    public ArrayList<Population> countryPopulation() {
        String query = "SELECT country.Name AS Name, " +
                "SUM(country.Population) AS Total_Population, " +
                "SUM(city.Population) AS City_Population, " +
                "SUM(country.Population) - SUM(city.Population) AS No_City_Population, " +
                "(SUM(city.Population) / SUM(country.Population)) * 100 AS City_Percentage, " +
                "((SUM(country.Population) - SUM(city.Population)) / SUM(country.Population)) * 100 AS No_City_Percentage " +
                "FROM country JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Name, country.Population " +
                "ORDER BY Total_Population DESC";
        return getPopulation(query);
    }

    // To get population
    public ArrayList<Population> getPopulation(String query) {
        ArrayList<Population> populations = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(query);) {
            while (rset.next()) {
                Population p = new Population();
                p.Name = rset.getString("Name");
                p.totalPopulation = rset.getLong("Total_Population");
                p.cityPopulation = rset.getLong("City_Population");
                p.cityPercentage = rset.getDouble("City_Percentage");
                p.noCityPopulation = rset.getLong("No_City_Population");
                p.noCityPercentage = rset.getDouble("No_City_Percentage");
                populations.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Query Failed: " + e.getMessage());
        }
        return populations;
    }

    // Print result
    public void printPopulation(ArrayList<Population> populations) {
        if (populations == null || populations.isEmpty()) {
            System.out.println("Population not found");
            return;
        }

        // Define column headers
        String[] headers = {"Name", "Total_Population", "City_Population", "% in cities", "No_City_Population", "% in non cities"};

        // Compute max width for each column
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (Population p : populations) {
            widths[0] = Math.max(widths[0], p.Name.length());
            widths[1] = Math.max(widths[1], String.valueOf(p.totalPopulation).length());
            widths[2] = Math.max(widths[2], String.valueOf(p.cityPopulation).length());
            widths[3] = Math.max(widths[3], String.valueOf(p.cityPercentage).length());
            widths[4] = Math.max(widths[4], String.valueOf(p.noCityPopulation).length());
            widths[5] = Math.max(widths[5], String.valueOf(p.noCityPercentage).length());
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
        for (Population p : populations) {
            System.out.printf("| %-" + widths[0] + "s ", p.Name);
            System.out.printf("| %-" + widths[1] + "s ", p.totalPopulation);
            System.out.printf("| %-" + widths[2] + "s ", p.cityPopulation);
            System.out.printf("| %-" + widths[3] + "s ", p.cityPercentage);
            System.out.printf("| %-" + widths[4] + "s ", p.noCityPopulation);
            System.out.printf("| %-" + widths[5] + "s ", p.noCityPercentage);
            System.out.println("|");
        }
        printBorder.run();
    }

    // Population of single continent/ region/ country/ district/ city
    public ArrayList<Population> populationWorld() {
        String query = "SELECT 'World' AS Name, SUM(Population) AS Population " +
                "FROM country";
        return executeQuery(query);
    }

    public ArrayList<Population> populationContinent(String continent) {
        String query = "SELECT Continent AS Name, SUM(Population) AS Population " +
                "FROM country WHERE Continent = ? " +
                "GROUP BY Continent " +
                "ORDER BY Population DESC";
        return executeQuery(query, continent);
    }

    public ArrayList<Population> populationRegion(String region) {
        String query = "SELECT Region AS Name, SUM(Population) AS Population " +
                "FROM country WHERE Region = ? " +
                "GROUP BY Region " +
                "ORDER BY Population DESC";
        return executeQuery(query, region);
    }

    public ArrayList<Population> populationCountry(String country) {
        String query = "SELECT Name, Population " +
                "FROM country WHERE Name = ? " +
                "ORDER BY Population DESC";
        return executeQuery(query, country);
    }

    public ArrayList<Population> populationDistrict(String district) {
        String query = "SELECT District AS Name, Population " +
                "FROM city WHERE District = ? " +
                "ORDER BY Population DESC";
        return executeQuery(query, district);
    }

    public ArrayList<Population> populationCity(String city) {
        String query = "SELECT Name, Population " +
                "FROM city WHERE Name = ? " +
                "ORDER BY Population DESC";
        return executeQuery(query, city);
    }

    // Query without param
    private ArrayList<Population> executeQuery(String query) {
        ArrayList<Population> populations = new ArrayList<Population>();
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rset = stmt.executeQuery();) {
            while (rset.next()) {
                Population p = extractPopulations(rset);
                populations.add(p);
            }
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return populations;
    }

    // Query with one param
    private ArrayList<Population> executeQuery(String query, String param) {
        ArrayList<Population> populations = new ArrayList<Population>();
        try(PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setString(1, param);
            try(ResultSet rset = stmt.executeQuery();) {
                while (rset.next()) {
                    Population p = extractPopulations(rset);
                    populations.add(p);
                }
            }
        }  catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return populations;
    }

    // extract population method
    private Population extractPopulations(ResultSet rset) throws SQLException {
        Population p = new Population();
        p.Name = rset.getString("Name");
        p.totalPopulation = rset.getLong("Population");
        return p;
    }

    // print population method
    public void printSinglePopulation (ArrayList<Population> populations) {
        if (populations == null || populations.isEmpty()) {
            System.out.println("No population found");
            return;
        }

        // Define column headers
        String[] headers = {"Name", "Population"};

        // Compute max width for each column
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (Population p : populations) {
            widths[0] = Math.max(widths[0], p.Name.length());
            widths[1] = Math.max(widths[1], String.valueOf(p.totalPopulation).length());
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
        for (Population p : populations) {
            System.out.printf("| %-" + widths[0] + "s ", p.Name);
            System.out.printf("| %-" + widths[1] + "s ", p.totalPopulation);
            System.out.println("|");
        }

        printBorder.run();
    }
}
