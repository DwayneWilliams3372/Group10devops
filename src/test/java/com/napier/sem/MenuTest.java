package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * MenuTest - drives Menu through all cases to obtain full coverage on Menu.start()
 *
 * Approach:
 * - For each menu option we create an input stream containing:
 *   "<choice>\n" + (any extra inputs required by that choice) + "0\n" (so menu will exit)
 * - Construct Menu after setting System.in so Menu's scanner picks up the stream.
 * - Replace private collaborator fields (country, city, capital, population, language) with mocks.
 * - Configure the mock method called by that option to return an empty ArrayList<> (or list as needed).
 * - Run menu.start() and verify the expected mock method was invoked.
 */
public class MenuTest {

    private final InputStream systemIn = System.in;
    private Connection mockConnection;

    @BeforeEach
    public void setUp() {
        mockConnection = mock(Connection.class);
    }

    @AfterEach
    public void tearDown() {
        System.setIn(systemIn); // restore
    }

    /**
     * Utility to replace a private final field on Menu with a supplied mock instance.
     */
    private void setMenuField(Menu menu, String fieldName, Object value) throws Exception {
        Field f = Menu.class.getDeclaredField(fieldName);
        f.setAccessible(true);

        // Remove final modifier if necessary (works in most test environments).
        // On some JVMs you may need additional tricks or mockito-inline.
        f.set(menu, value);
    }

    /**
     * Helper to create Menu, replace collaborators with mocks and run the menu with a provided input.
     *
     * @param input the simulated stdin (must contain choice, any extra inputs, and then 0 to exit)
     * @return constructed Menu (after replacing fields)
     */
    private Menu buildMenuWithMocks(String input,
                                    Country countryMock,
                                    City cityMock,
                                    Capital capitalMock,
                                    Population populationMock,
                                    Language languageMock) throws Exception {
        // Set System.in before constructing Menu so its Scanner uses our stream
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Menu menu = new Menu(mockConnection);

        // Replace internal collaborators with mocks
        if (countryMock != null) setMenuField(menu, "country", countryMock);
        if (cityMock != null) setMenuField(menu, "city", cityMock);
        if (capitalMock != null) setMenuField(menu, "capital", capitalMock);
        if (populationMock != null) setMenuField(menu, "population", populationMock);
        if (languageMock != null) setMenuField(menu, "language", languageMock);

        return menu;
    }

    @Test
    public void testAllMenuOptionsCovered() throws Exception {
        // We'll loop through options 1..32. For each option we craft the required input tokens,
        // create mocks that answer the calls the Menu expects, run the menu and verify the right method got called.

        for (int choice = 1; choice <= 32; choice++) {

            // Prepare mocks and stubbing
            Country countryMock = mock(Country.class);
            City cityMock = mock(City.class);
            Capital capitalMock = mock(Capital.class);
            Population populationMock = mock(Population.class);
            Language languageMock = mock(Language.class);

            // Default returns to avoid NPEs when methods are invoked
            when(countryMock.getAllCountriesByPopulation()).thenReturn(new ArrayList<>());
            when(countryMock.getCountriesByContinent(anyString())).thenReturn(new ArrayList<>());
            when(countryMock.getCountriesByRegion(anyString())).thenReturn(new ArrayList<>());
            when(countryMock.getTopCountriesInWorld(anyInt())).thenReturn(new ArrayList<>());
            when(countryMock.getTopCountriesInContinent(anyString(), anyInt())).thenReturn(new ArrayList<>());
            when(countryMock.getTopCountriesInRegion(anyString(), anyInt())).thenReturn(new ArrayList<>());

            when(cityMock.getCities()).thenReturn(new ArrayList<>());
            when(cityMock.getCitiesContinent(anyString())).thenReturn(new ArrayList<>());
            when(cityMock.getCitiesRegion(anyString())).thenReturn(new ArrayList<>());
            when(cityMock.getCitiesCountry(anyString())).thenReturn(new ArrayList<>());
            when(cityMock.getCitiesDistrict(anyString())).thenReturn(new ArrayList<>());
            when(cityMock.getCitiesPopulation(anyInt())).thenReturn(new ArrayList<>());
            when(cityMock.getTopCitiesContinent(anyString(), anyInt())).thenReturn(new ArrayList<>());
            when(cityMock.getTopCitiesRegion(anyString(), anyInt())).thenReturn(new ArrayList<>());
            when(cityMock.getTopCitiesCountry(anyString(), anyInt())).thenReturn(new ArrayList<>());
            when(cityMock.getTopCitiesDistrict(anyString(), anyInt())).thenReturn(new ArrayList<>());

            when(capitalMock.getCapitals()).thenReturn(new ArrayList<>());
            when(capitalMock.getCapitalsContinent(anyString())).thenReturn(new ArrayList<>());
            when(capitalMock.getCapitalsRegion(anyString())).thenReturn(new ArrayList<>());
            when(capitalMock.getCapitalsPopulation(anyInt())).thenReturn(new ArrayList<>());
            when(capitalMock.topCapitalsContinent(anyString(), anyInt())).thenReturn(new ArrayList<>());
            when(capitalMock.topCapitalsRegion(anyString(), anyInt())).thenReturn(new ArrayList<>());

            when(populationMock.continentPopulation()).thenReturn(new ArrayList<>());
            when(populationMock.regionPopulation()).thenReturn(new ArrayList<>());
            when(populationMock.countryPopulation()).thenReturn(new ArrayList<>());
            when(populationMock.populationWorld()).thenReturn(new ArrayList<>());
            when(populationMock.populationContinent(anyString())).thenReturn(new ArrayList<>());
            when(populationMock.populationRegion(anyString())).thenReturn(new ArrayList<>());
            when(populationMock.populationCountry(anyString())).thenReturn(new ArrayList<>());
            when(populationMock.populationDistrict(anyString())).thenReturn(new ArrayList<>());
            when(populationMock.populationCity(anyString())).thenReturn(new ArrayList<>());

            when(languageMock.getLanguages()).thenReturn(new ArrayList<>());

            // Build input string for this choice followed by any additional required tokens and then 0 to exit
            StringBuilder inputBuilder = new StringBuilder();
            inputBuilder.append(choice).append("\n");

            // Cases that require additional inputs:
            switch (choice) {
                // Country cases:
                case 2 -> inputBuilder.append("Asia\n");
                case 3 -> inputBuilder.append("Southern Europe\n");
                case 4 -> inputBuilder.append("5\n");
                case 5 -> inputBuilder.append("Africa\n").append("3\n");
                case 6 -> inputBuilder.append("Caribbean\n").append("4\n");

                // City cases:
                case 8 -> inputBuilder.append("Asia\n");
                case 9 -> inputBuilder.append("Eastern Europe\n");
                case 10 -> inputBuilder.append("France\n");
                case 11 -> inputBuilder.append("California\n");
                case 12 -> inputBuilder.append("10\n");
                case 13 -> inputBuilder.append("Asia\n").append("7\n");
                case 14 -> inputBuilder.append("Eastern Europe\n").append("6\n");
                case 15 -> inputBuilder.append("United Kingdom\n").append("3\n");
                case 16 -> inputBuilder.append("California\n").append("2\n");

                // Capital cases:
                case 18 -> inputBuilder.append("Europe\n");
                case 19 -> inputBuilder.append("Southern Europe\n");
                case 20 -> inputBuilder.append("5\n");
                case 21 -> inputBuilder.append("Africa\n").append("4\n");
                case 22 -> inputBuilder.append("Caribbean\n").append("3\n");

                // Population cases:
                case 27 -> inputBuilder.append("Asia\n");
                case 28 -> inputBuilder.append("Northern Europe\n");
                case 29 -> inputBuilder.append("France\n");
                case 30 -> inputBuilder.append("California\n");
                case 31 -> inputBuilder.append("Mumbai\n");

                // Language case doesn't require extra
                default -> {
                    // Many options already covered above or don't require additional input
                }
            }

            // For cases that specifically use scanner.nextInt() inside the case (not only for choice),
            // the integer tokens are already included above where needed.

            // After finishing choice's inputs, add 0 to exit
            inputBuilder.append("0\n"); // cause the menu to exit next loop

            String input = inputBuilder.toString();

            // Construct menu and replace fields
            Menu menu = buildMenuWithMocks(input, countryMock, cityMock, capitalMock, populationMock, languageMock);

            // Run the menu; it will process the provided choice then exit when it sees 0
            menu.start();

            // Verify the expected method for this choice was invoked at least once.
            // Use switch to map choice -> expected method invocation
            switch (choice) {
                case 1 -> verify(countryMock, times(1)).getAllCountriesByPopulation();
                case 2 -> verify(countryMock, times(1)).getCountriesByContinent("Asia");
                case 3 -> verify(countryMock, times(1)).getCountriesByRegion("Southern Europe");
                case 4 -> verify(countryMock, times(1)).getTopCountriesInWorld(5);
                case 5 -> verify(countryMock, times(1)).getTopCountriesInContinent("Africa", 3);
                case 6 -> verify(countryMock, times(1)).getTopCountriesInRegion("Caribbean", 4);

                case 7 -> verify(cityMock, times(1)).getCities();
                case 8 -> verify(cityMock, times(1)).getCitiesContinent("Asia");
                case 9 -> verify(cityMock, times(1)).getCitiesRegion("Eastern Europe");
                case 10 -> verify(cityMock, times(1)).getCitiesCountry("France");
                case 11 -> verify(cityMock, times(1)).getCitiesDistrict("California");
                case 12 -> verify(cityMock, times(1)).getCitiesPopulation(10);
                case 13 -> verify(cityMock, times(1)).getTopCitiesContinent("Asia", 7);
                case 14 -> verify(cityMock, times(1)).getTopCitiesRegion("Eastern Europe", 6);
                case 15 -> verify(cityMock, times(1)).getTopCitiesCountry("United Kingdom", 3);
                case 16 -> verify(cityMock, times(1)).getTopCitiesDistrict("California", 2);

                case 17 -> verify(capitalMock, times(1)).getCapitals();
                case 18 -> verify(capitalMock, times(1)).getCapitalsContinent("Europe");
                case 19 -> verify(capitalMock, times(1)).getCapitalsRegion("Southern Europe");
                case 20 -> verify(capitalMock, times(1)).getCapitalsPopulation(5);
                case 21 -> verify(capitalMock, times(1)).topCapitalsContinent("Africa", 4);
                case 22 -> verify(capitalMock, times(1)).topCapitalsRegion("Caribbean", 3);

                case 23 -> verify(populationMock, times(1)).continentPopulation();
                case 24 -> verify(populationMock, times(1)).regionPopulation();
                case 25 -> verify(populationMock, times(1)).countryPopulation();
                case 26 -> verify(populationMock, times(1)).populationWorld();
                case 27 -> verify(populationMock, times(1)).populationContinent("Asia");
                case 28 -> verify(populationMock, times(1)).populationRegion("Northern Europe");
                case 29 -> verify(populationMock, times(1)).populationCountry("France");
                case 30 -> verify(populationMock, times(1)).populationDistrict("California");
                case 31 -> verify(populationMock, times(1)).populationCity("Mumbai");

                case 32 -> verify(languageMock, times(1)).getLanguages();

                default -> { /* no-op */ }
            }

            // Reset invocations so mocks do not accumulate calls for the next iteration
            clearInvocations(countryMock, cityMock, capitalMock, populationMock, languageMock);
        }
    }
}
