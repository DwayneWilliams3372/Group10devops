package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Capital {
    public String name;
    public String country;
    public int population;

    private Connection con;
    public Capital(Connection con) {
        this.con = con;
    }

    public Capital() {
        //
    }

    // All capital cities in the world
    public ArrayList<Capital> getCapitals() {
        String query = "SELECT city.Name, country.Name AS COUNTRY, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "ORDER BY city.Population DESC";
        return executeQuery(query);
    }

    //All the capital cities in a continent
    public ArrayList<Capital> getCapitalsContinent(String continent) {
        String query = "SELECT city.Name, country.Name AS COUNTRY, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Continent = ? " +
                "ORDER BY city.Population DESC";
        return executeQuery(query, continent);
    }

    //All the capital cities in a region
    public ArrayList<Capital> getCapitalsRegion(String region) {
        String query = "SELECT city.Name, country.Name AS COUNTRY, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Region = ? " +
                "ORDER BY city.Population DESC";
        return executeQuery(query, region);
    }

    //The top N populated capital cities in the world
    public ArrayList<Capital> getCapitalsPopulation(int n) {
        String query = "SELECT city.Name, country.Name AS COUNTRY, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "ORDER BY city.Population DESC LIMIT ?";
        return executeQuery(query, n);
    }
    //The top N populated capital cities in a continent
    public ArrayList<Capital> topCapitalsContinent(String continent, int n) {
        String query = "SELECT city.Name, country.Name AS COUNTRY, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Continent = ? " +
                "ORDER BY city.Population DESC LIMIT ?";
        return executeQuery(query, continent, n);
    }

    //The top N populated capital cities in a region
    public ArrayList<Capital> topCapitalsRegion(String region, int n) {
        String query = "SELECT city.Name, country.Name AS COUNTRY, city.Population " +
                "FROM city JOIN country ON city.ID = country.Capital " +
                "WHERE country.Region = ? " +
                "ORDER BY city.Population DESC LIMIT ?";
        return executeQuery(query, region, n);
    }

    // Query with no param
    private ArrayList<Capital> executeQuery(String query) {
        ArrayList<Capital> capitals = new ArrayList<Capital>();
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rset = stmt.executeQuery();) {
            while (rset.next()) {
                Capital c = extractCapital(rset);
                capitals.add(c);
            }
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return capitals;
    }

    // Query with one param
    private ArrayList<Capital> executeQuery(String query, String param) {
        ArrayList<Capital> capitals = new ArrayList<Capital>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setString(1,  param);
            try (ResultSet rset = stmt.executeQuery();) {
                while (rset.next()) {
                    Capital c = extractCapital(rset);
                    capitals.add(c);
                }
            }
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return capitals;
    }

    // Query with int param
    private ArrayList<Capital> executeQuery(String query, int n)  {
        ArrayList<Capital> capitals = new ArrayList<Capital>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setInt(1,  n);
            try (ResultSet rset = stmt.executeQuery();) {
                while (rset.next()) {
                    Capital c = extractCapital(rset);
                    capitals.add(c);
                }
            }
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return capitals;
    }

    // Query with two param
    private ArrayList<Capital> executeQuery(String query, String param, int i) {
        ArrayList<Capital> capitals = new ArrayList<Capital>();
        try (PreparedStatement stmt = con.prepareStatement(query);) {
            stmt.setString(1,  param);
            stmt.setInt(2, i);
            try (ResultSet rset = stmt.executeQuery();) {
                while (rset.next()) {
                    Capital c = extractCapital(rset);
                    capitals.add(c);
                }
            }
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return capitals;
    }

    // Helper to extract capital from ResultSet
    private Capital extractCapital(ResultSet rset) throws SQLException {
        Capital c = new Capital();
        c.name = rset.getString("NAME");
        c.country = rset.getString("COUNTRY");
        c.population = rset.getInt("POPULATION");
        return c;
    }

    // Print Result for capitals
    public void printCapitals (ArrayList<Capital> capitals) {
        if (capitals == null || capitals.isEmpty()) {
            System.out.println("No countries found");
            return;
        }

        // Define column headers
        String[] headers = {"Name", "Country", "Population"};

        // Compute max width for each column
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (Capital c : capitals) {
            widths[0] = Math.max(widths[0], c.name.length());
            widths[1] = Math.max(widths[1], c.country.length());
            widths[2] = Math.max(widths[2], String.valueOf(c.population).length());
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
        for (Capital c : capitals) {
            System.out.printf("| %-" + widths[0] + "s ", c.name);
            System.out.printf("| %-" + widths[1] + "s ", c.country);
            System.out.printf("| %-" + widths[2] + "s ", c.population);
            System.out.println("|");
        }

        printBorder.run();
    }

    public void outputCapitals(ArrayList<Capital> capitals, String filename) {
        System.out.println("Starting outputCapitals method...");
        if (capitals == null || capitals.isEmpty()) {
            System.out.println("No capitals to output");
            return;
        }
        System.out.println("Capital list has " + capitals.size() + " items. Generating Markdown...");

        StringBuilder sb = new StringBuilder();
        sb.append("| Name | Country | Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        for (Capital c : capitals) {
            if (c == null) continue;
            sb.append(" | " + c.name + " | " + c.country + " | " + c.population + "|\r\n");
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