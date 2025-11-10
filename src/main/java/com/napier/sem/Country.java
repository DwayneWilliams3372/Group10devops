package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

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
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, " +
                        "(SELECT city.Name FROM city WHERE city.ID = country.Capital) AS Capital " +
                        "FROM country ORDER BY country.Population DESC";
        return executeCountryQuery(query);
    }

    //   All countries in a continent
    public ArrayList<Country> getCountriesByContinent(String continent) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, " +
                        "(SELECT city.Name FROM city WHERE city.ID = country.Capital) AS Capital " +
                        "FROM country WHERE country.Continent = ? " +
                        "ORDER BY country.Population DESC";
        return executeCountryQuery(query, continent);
    }

    //    All countries in a region
    public ArrayList<Country> getCountriesByRegion(String region) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, " +
                        "(SELECT city.Name FROM city WHERE city.ID = country.Capital) AS Capital " +
                        "FROM country WHERE country.Region = ? " +
                        "ORDER BY country.Population DESC";
        return executeCountryQuery(query, region);
    }

    //    Top N populated countries in the world
    public ArrayList<Country> getTopCountriesInWorld(int n) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, " +
                        "(SELECT city.Name FROM city WHERE city.ID = country.Capital) AS Capital " +
                        "FROM country ORDER BY country.Population DESC LIMIT ? ";
        return executeCountryQueryWithLimit(query, n);
    }

    //    Top N populated countries in a continent
    public ArrayList<Country> getTopCountriesInContinent(String continent, int n) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, " +
                        "(SELECT city.Name FROM city WHERE city.ID = country.Capital) AS Capital " +
                        "FROM country WHERE country.Continent = ? " +
                        "ORDER BY country.Population DESC LIMIT ?";
        return executeCountryQuery(query, continent, n);
    }

    //    Top N populated countries in a region
    public ArrayList<Country> getTopCountriesInRegion(String region, int n) {
        String query =
                "SELECT country.Code, country.Name, country.Continent, country.Region, country.Population, " +
                        "(SELECT city.Name FROM city WHERE city.ID = country.Capital) AS Capital " +
                        "FROM country WHERE country.Region = ? " +
                        "ORDER BY country.Population DESC ?";
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

        System.out.printf("%-10s %-40s %-20s %-20s %-20s %-30s%n",
                "Code", "Name", "Continent", "Region", "Population", "Capital");

        for (Country c : countries) {
            System.out.printf("%-10s %-40s %-20s %-20s %-20d %-30s%n",
                    c.code, c.name, c.continent, c.region, c.population, c.capital);
        }
    }
}