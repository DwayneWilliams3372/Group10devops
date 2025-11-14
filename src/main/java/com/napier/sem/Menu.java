package com.napier.sem;

import jdk.jfr.Percentage;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;


public class Menu {
    private final Connection con;
    private final Country country;
    private final City city;
    private final Capital capital;
    private final Population population;
    private final Language language;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(Connection con) {
        this.con = con;
        this.country = new Country(con);
        this.city = new City(con);
        this.capital = new Capital(con);
        this.population = new Population(con);
        this.language = new Language(con);
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
            System.out.println("7. All the cities in the world");
            System.out.println("8. All the cities in a continent");
            System.out.println("9. All the cities in a region");
            System.out.println("10. All the cities in a country");
            System.out.println("11. All the cities in a district");
            System.out.println("12. Top N cities in the world");
            System.out.println("13. Top N cities in a continent");
            System.out.println("14. Top N cities in a region");
            System.out.println("15. Top N cities in a country");
            System.out.println("16. Top N cities in a district");
            System.out.println("17. All the capital cities in the world");
            System.out.println("18. All the capital cities in a continent");
            System.out.println("19. All the capital cities in a region");
            System.out.println("20. Top N capital cities in the world ");
            System.out.println("21. Top N capital cities in a continent");
            System.out.println("22. Top N capital cities in a region");
            System.out.println("23. The population of people, people living in cities, and people not living in cities in each continent");
            System.out.println("24. The population of people, people living in cities, and people not living in cities in each region");
            System.out.println("25. The population of people, people living in cities, and people not living in cities in each country");
            System.out.println("26. The population of the world");
            System.out.println("27. The population of a continent");
            System.out.println("28. The population of a region");
            System.out.println("29. The population of a country");
            System.out.println("30. The population of a district");
            System.out.println("31. The population of a city");
            System.out.println("32. The specific languages from greatest number to smallest, including the percentage of the world population");
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
                    ArrayList<Country> topCountriesInContinent = country.getTopCountriesInContinent(con, n);
                    country.printCountries(topCountriesInContinent);
                    break;
                }
                case 6: {
                    System.out.print("Enter region: ");
                    String region = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<Country> topCountriesInRegion = country.getTopCountriesInRegion(region, n);
                    country.printCountries(topCountriesInRegion);
                    break;
                }
                case 7: {
                    ArrayList<City> cities = city.getCities();
                    city.printCities(cities);
                    break;
                }
                case 8: {
                    System.out.print("Enter continent: ");
                    String cont = scanner.nextLine();
                    ArrayList<City> continentCities = city.getCitiesContinent(cont);
                    city.printCities(continentCities);
                    break;
                }
                case 9: {
                    System.out.print("Enter Region: ");
                    String reg = scanner.nextLine();
                    ArrayList<City> regionCities = city.getCitiesRegion(reg);
                    city.printCities(regionCities);
                    break;
                }
                case 10: {
                    System.out.print("Enter Country: ");
                    String cou = scanner.nextLine();
                    ArrayList<City> countryCities = city.getCitiesCountry(cou);
                    city.printCities(countryCities);
                    break;
                }
                case 11: {
                    System.out.print("Enter District: ");
                    String dis = scanner.nextLine();
                    ArrayList<City> districtCities = city.getCitiesDistrict(dis);
                    city.printCities(districtCities);
                    break;
                }
                case 12: {
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<City> topCitiesWorld = city.getCitiesPopulation(n);
                    city.printCities(topCitiesWorld);
                    break;
                }
                case 13: {
                    System.out.print("Enter continent: ");
                    String continent = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<City> topCitiesInContinent = city.getTopCitiesContinent(continent, n);
                    city.printCities(topCitiesInContinent);
                    break;
                }
                case 14: {
                    System.out.print("Enter region: ");
                    String region = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<City> topCitiesInRegion = city.getTopCitiesRegion(region, n);
                    city.printCities(topCitiesInRegion);
                    break;
                }
                case 15: {
                    System.out.print("Enter country: ");
                    String country = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<City> topCitiesInCountry = city.getTopCitiesCountry(country, n);
                    city.printCities(topCitiesInCountry);
                    break;
                }
                case 16: {
                    System.out.print("Enter district: ");
                    String district = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<City> topCitiesInDistrict = city.getTopCitiesDistrict(district, n);
                    city.printCities(topCitiesInDistrict);
                    break;
                }
                case 17: {
                    ArrayList<Capital> cities = capital.getCapitals();
                    capital.printCapitals(cities);
                    break;
                }
                case 18: {
                    System.out.print("Enter continent: ");
                    String continent = scanner.nextLine();
                    ArrayList<Capital> capitalsContinent = capital.getCapitalsContinent(continent);
                    capital.printCapitals(capitalsContinent);
                    break;
                }
                case 19: {
                    System.out.print("Enter region: ");
                    String region = scanner.nextLine();
                    ArrayList<Capital> capitalsRegion = capital.getCapitalsRegion(region);
                    capital.printCapitals(capitalsRegion);
                    break;
                }
                case 20: {
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<Capital> capitalsPopulation = capital.getCapitalsPopulation(n);
                    capital.printCapitals(capitalsPopulation);
                    break;
                }
                case 21: {
                    System.out.print("Enter continent: ");
                    String continent = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<Capital> topCapitalsContinent = capital.topCapitalsContinent(continent, n);
                    capital.printCapitals(topCapitalsContinent);
                    break;
                }
                case 22: {
                    System.out.print("Enter region: ");
                    String region = scanner.nextLine();
                    System.out.print("Enter N: ");
                    int n = scanner.nextInt();
                    ArrayList<Capital>  topCapitalsRegion = capital.topCapitalsRegion(region, n);
                    capital.printCapitals(topCapitalsRegion);
                    break;
                }
                case 23: {
                    ArrayList<Population> populationsContinent = population.continentPopulation();
                    population.printPopulation(populationsContinent);
                    break;
                }
                case 24: {
                    ArrayList<Population> populationsRegion = population.regionPopulation();
                    population.printPopulation(populationsRegion);
                    break;
                }
                case 25: {
                    ArrayList<Population> populationsCountry = population.countryPopulation();
                    population.printPopulation(populationsCountry);
                    break;
                }
                case 26: {
                    ArrayList<Population> worldPopulation = population.populationWorld();
                    population.printSinglePopulation(worldPopulation);
                    break;
                }
                case 27: {
                    System.out.print("Enter continent: ");
                    String continent = scanner.nextLine();
                    ArrayList<Population> continentPopulation = population.populationContinent(continent);
                    population.printSinglePopulation(continentPopulation);
                    break;
                }
                case 28: {
                    System.out.print("Enter region: ");
                    String region = scanner.nextLine();
                    ArrayList<Population> regionPopulation = population.populationRegion(region);
                    population.printSinglePopulation(regionPopulation);
                    break;
                }
                case 29: {
                    System.out.print("Enter country: ");
                    String country = scanner.nextLine();
                    ArrayList<Population> countryPopulation = population.populationCountry(country);
                    population.printSinglePopulation(countryPopulation);
                    break;
                }
                case 30: {
                    System.out.print("Enter district: ");
                    String district = scanner.nextLine();
                    ArrayList<Population>  districtPopulation = population.populationDistrict(district);
                    population.printSinglePopulation(districtPopulation);
                    break;
                }
                case 31: {
                    System.out.print("Enter city: ");
                    String city = scanner.nextLine();
                    ArrayList<Population> cityPopulation = population.populationCity(city);
                    population.printSinglePopulation(cityPopulation);
                    break;
                }
                case 32: {
                    ArrayList<Language> getLanguage = language.getLanguages();
                    language.printLanguages(getLanguage);
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
