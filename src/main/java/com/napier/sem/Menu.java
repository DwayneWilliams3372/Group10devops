package com.napier.sem;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;


public class Menu {
    private final Connection con;
    private final Country country;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(Connection con) {
        this.con = con;
        this.country = new Country(con);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Population Report Menu ===");
            System.out.println("1. All countries in the world");
            System.out.println("2. All countries in a continent");
            System.out.println("3. All countries in a region");
            System.out.println("4. Top N countries in the world");
            System.out.println("5. Top N countries in a continent");
            System.out.println("6. Top N countries in a region");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: {
                    ArrayList<Country> countries = country.getAllCountriesByPopulation();
                    country.printCountries(countries);
                    break;
                }
                case 2: {
                    System.out.print("Enter continent: ");
                    String cont = scanner.nextLine();
                    ArrayList<Country> continentCountries = country.getCountriesByContinent(cont);
                    country.printCountries(continentCountries);
                    break;
                }
                case 3: {
                    System.out.print("Enter region: ");
                    String region = scanner.nextLine();
                    ArrayList<Country> regionCountries = country.getCountriesByRegion(region);
                    country.printCountries(regionCountries);
                    break;
                }
                case 4: {
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<Country> topCountries = country.getTopCountriesInWorld(n);
                    country.printCountries(topCountries);
                    break;
                }
                case 5: {
                    System.out.print("Enter continent: ");
                    String con = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<Country> topCoutriesInContinent = country.getTopCountriesInContinent(con, n);
                    country.printCountries(topCoutriesInContinent);
                    break;
                }
                case 6: {
                    System.out.print("Enter region: ");
                    String region = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<Country> topCoutriesInRegion = country.getTopCountriesInRegion(region, n);
                    break;
                }
                case 0:  {
                    System.out.println("Exiting menu...");
                    return;
                }
                default: {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }
}
