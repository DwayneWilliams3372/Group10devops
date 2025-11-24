package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Language {
    private String Language;
    private long Population;
    private double Percentage;

    private Connection con;
    public Language(Connection con){
        this.con = con;
    }

    public Language(){
        //
    }

    public ArrayList<Language> getLanguages() {
        String query = "SELECT cl.Language, SUM(ROUND(c.Population * cl.Percentage / 100)) AS Speakers, " +
                "ROUND((SUM(c.Population * cl.Percentage / 100) / (SELECT SUM(Population) FROM country) ) * 100, 2 ) AS WorldPercentage " +
                "FROM country c " +
                "JOIN countrylanguage cl ON c.Code = cl.CountryCode " +
                "WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                "GROUP BY cl.Language " +
                "ORDER BY Speakers DESC ";
        return executeQuery(query);
    }

    private ArrayList<Language> executeQuery(String query) {
        ArrayList<Language> languages = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();) {
            while (rs.next()) {
                Language l = extractQuery(rs);
                languages.add(l);
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
        return languages;
    }

    private Language extractQuery(ResultSet rs) throws SQLException {
        Language l = new Language();
        l.Language = rs.getString("Language");
        l.Population = rs.getLong("Speakers");
        l.Percentage = rs.getDouble("WorldPercentage");
        return l;
    }

    public void printLanguages(ArrayList<Language> languages) {
        if (languages == null || languages.isEmpty()) {
            System.out.println("No Languages found");
            return;
        }

        // Define column headers
        String[] headers = {"Language", "Population", "PercentageOfWorld"};

        // Compute max width for each column
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (Language l : languages) {
            widths[0] = Math.max(widths[0], l.Language.length());
            widths[1] = Math.max(widths[1], String.valueOf(l.Population).length());
            widths[2] = Math.max(widths[2], String.valueOf(l.Percentage).length());
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
        for (Language l : languages) {
            System.out.printf("| %-" + widths[0] + "s ", l.Language);
            System.out.printf("| %-" + widths[1] + "s ", l.Population);
            System.out.printf("| %-" + widths[2] + "s ", l.Percentage);
            System.out.println("|");
        }

        printBorder.run();
    }

    public void outputLanguage(ArrayList<Language> languages, String filename) {
        System.out.println("Starting outputLanguage method...");
        if (languages == null || languages.isEmpty()) {
            System.out.println("No languages to output");
            return;
        }
        System.out.println("Language list has " + languages.size() + " items. Generating Markdown...");

        StringBuilder sb = new StringBuilder();
        sb.append("| Name | Population | Percentage of the World |\r\n");
        sb.append("| --- | --- | --- |\r\n");
        for (Language l : languages) {
            if (l == null) continue;
            sb.append(" | " + l.Language + " | " + l.Population + " | " +  l.Percentage + "|\r\n");
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
