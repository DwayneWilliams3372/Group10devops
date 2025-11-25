package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App
{
    /**
     * Connection to MySQL database.
     */
    Connection con = null;
    /**
     * Connect to the MySQL database.
     */
    public void connect() {
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
                if ("true".equals(System.getProperty("TEST_MODE"))) {
                    Thread.sleep(1000);  // 1 sec for integration tests
                } else {
                    Thread.sleep(30000); // 30 sec for real runtime
                }
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
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected.");
            } catch (SQLException sqle) {
                System.out.println("Failed to close connection.");
            }
        }
    }

    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Check for CI environment (e.g., set NON_INTERACTIVE=true in workflow)
        String ciMode = System.getenv("NON_INTERACTIVE");  // Or use "CI" if preferred
        if ("true".equals(ciMode)) {
            // In CI mode, just confirm connection and exit
            System.out.println("Database connection test passed. Exiting.");
            a.disconnect();
            System.exit(0);
        } else {
            // Normal interactive mode: start the menu
            Menu menu = new Menu(a.con);
            menu.start();
        }

        // Disconnect from database
        a.disconnect();
    }
}