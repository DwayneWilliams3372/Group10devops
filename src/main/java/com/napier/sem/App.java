package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class App
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt ");
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Create the report service (accessing the inner class)
        CountryReportService reportService = new CountryReportService(a.con);
        ReportPrinter reportPrinter = new ReportPrinter();

        // Example 1: All countries in the world
        ArrayList<Country> worldCountries = reportService.getAllCountriesByPopulation();
        System.out.println("\nAll countries in the world:");
        reportPrinter.printCountries(worldCountries);

        // Example 2: All countries in Asia
        ArrayList<Country> asiaCountries = reportService.getCountriesByContinent("Asia");
        System.out.println("\nCountries in Asia:");
        reportPrinter.printCountries(asiaCountries);

        // Example 3: Top 5 populated countries in the world
        ArrayList<Country> top5 = reportService.getTopCountriesInWorld(5);
        System.out.println("\nTop 5 populated countries in the world:");
        reportPrinter.printCountries(top5);

        // Disconnect from database
        a.disconnect();
    }
}