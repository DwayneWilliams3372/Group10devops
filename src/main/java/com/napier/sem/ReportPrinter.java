package com.napier.sem;

import java.util.ArrayList;

public class ReportPrinter {
    public void printCountries(ArrayList<Country> countries){
        if (countries == null || countries.isEmpty()){
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
