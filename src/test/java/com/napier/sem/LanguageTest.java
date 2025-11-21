package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LanguageTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private Language language;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException {
        closeable = MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        language = new Language(mockConnection);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testDefaultConstructor() {
        Language defaultLanguage = new Language();
        assertNotNull(defaultLanguage);
    }

    @Test
    void testConnectionConstructor() {
        assertNotNull(language);
    }

    @Test
    void testGetLanguages() throws SQLException {
        // Setup mock result set behavior for major world languages
        when(mockResultSet.next()).thenReturn(true, true, true, false);
        when(mockResultSet.getString("Language")).thenReturn("Chinese", "English", "Hindi");
        when(mockResultSet.getLong("Speakers")).thenReturn(1200000000L, 1000000000L, 800000000L);
        when(mockResultSet.getDouble("WorldPercentage")).thenReturn(16.5, 13.8, 11.1);

        ArrayList<Language> languages = language.getLanguages();

        assertNotNull(languages);
        assertEquals(3, languages.size());

        // Use reflection to verify private fields
        Language firstLanguage = languages.get(0);
        assertEquals("Chinese", getPrivateField(firstLanguage, "Language"));
        assertEquals(1200000000L, getPrivateField(firstLanguage, "Population"));
        assertEquals(16.5, (Double) getPrivateField(firstLanguage, "Percentage"), 0.001);

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetLanguagesWithAllFiveMajorLanguages() throws SQLException {
        // Test with all five languages specified in the query
        when(mockResultSet.next()).thenReturn(true, true, true, true, true, false);
        when(mockResultSet.getString("Language")).thenReturn("Chinese", "English", "Hindi", "Spanish", "Arabic");
        when(mockResultSet.getLong("Speakers")).thenReturn(1200000000L, 1000000000L, 800000000L, 500000000L, 300000000L);
        when(mockResultSet.getDouble("WorldPercentage")).thenReturn(16.5, 13.8, 11.1, 6.9, 4.1);

        ArrayList<Language> languages = language.getLanguages();

        assertNotNull(languages);
        assertEquals(5, languages.size());

        // Verify all languages are present and in correct order using reflection
        assertEquals("Chinese", getPrivateField(languages.get(0), "Language"));
        assertEquals("English", getPrivateField(languages.get(1), "Language"));
        assertEquals("Hindi", getPrivateField(languages.get(2), "Language"));
        assertEquals("Spanish", getPrivateField(languages.get(3), "Language"));
        assertEquals("Arabic", getPrivateField(languages.get(4), "Language"));

        // Verify ordering by population (descending)
        long pop1 = (Long) getPrivateField(languages.get(0), "Population");
        long pop2 = (Long) getPrivateField(languages.get(1), "Population");
        long pop3 = (Long) getPrivateField(languages.get(2), "Population");
        long pop4 = (Long) getPrivateField(languages.get(3), "Population");
        long pop5 = (Long) getPrivateField(languages.get(4), "Population");

        assertTrue(pop1 >= pop2);
        assertTrue(pop2 >= pop3);
        assertTrue(pop3 >= pop4);
        assertTrue(pop4 >= pop5);
    }

    @Test
    void testGetLanguagesWithLargePopulationNumbers() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Language")).thenReturn("Chinese");
        when(mockResultSet.getLong("Speakers")).thenReturn(Long.MAX_VALUE);
        when(mockResultSet.getDouble("WorldPercentage")).thenReturn(99.99);

        ArrayList<Language> languages = language.getLanguages();

        assertNotNull(languages);
        assertEquals(1, languages.size());
        assertEquals(Long.MAX_VALUE, getPrivateField(languages.get(0), "Population"));
        assertEquals(99.99, (Double) getPrivateField(languages.get(0), "Percentage"), 0.001);
    }

    @Test
    void testGetLanguagesWithPrecisePercentages() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Language")).thenReturn("English");
        when(mockResultSet.getLong("Speakers")).thenReturn(1000000000L);
        when(mockResultSet.getDouble("WorldPercentage")).thenReturn(13.75);

        ArrayList<Language> languages = language.getLanguages();

        assertNotNull(languages);
        assertEquals(1, languages.size());
        assertEquals(13.75, (Double) getPrivateField(languages.get(0), "Percentage"), 0.001);
    }

    @Test
    void testPrintLanguagesEmptyList() {
        ArrayList<Language> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> language.printLanguages(emptyList));
    }

    @Test
    void testPrintLanguagesNull() {
        assertDoesNotThrow(() -> language.printLanguages(null));
    }

    @Test
    void testPrintLanguagesWithData() throws Exception {
        ArrayList<Language> languages = new ArrayList<>();

        // Create test language data using reflection
        Language lang1 = new Language();
        setPrivateField(lang1, "Language", "English");
        setPrivateField(lang1, "Population", 1000000000L);
        setPrivateField(lang1, "Percentage", 13.8);
        languages.add(lang1);

        Language lang2 = new Language();
        setPrivateField(lang2, "Language", "Spanish");
        setPrivateField(lang2, "Population", 500000000L);
        setPrivateField(lang2, "Percentage", 6.9);
        languages.add(lang2);

        assertDoesNotThrow(() -> language.printLanguages(languages));
    }

    @Test
    void testPrintLanguagesWithLongNames() throws Exception {
        ArrayList<Language> languages = new ArrayList<>();

        Language lang = new Language();
        setPrivateField(lang, "Language", "ThisIsAVeryLongLanguageNameThatExceedsNormalLength");
        setPrivateField(lang, "Population", 123456789L);
        setPrivateField(lang, "Percentage", 1.23);
        languages.add(lang);

        assertDoesNotThrow(() -> language.printLanguages(languages));
    }

    @Test
    void testSQLExceptionHandling() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database connection failed"));

        ArrayList<Language> languages = language.getLanguages();

        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    void testSingleLanguageResult() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Language")).thenReturn("Arabic");
        when(mockResultSet.getLong("Speakers")).thenReturn(300000000L);
        when(mockResultSet.getDouble("WorldPercentage")).thenReturn(4.1);

        ArrayList<Language> languages = language.getLanguages();

        assertNotNull(languages);
        assertEquals(1, languages.size());

        assertEquals("Arabic", getPrivateField(languages.get(0), "Language"));
        assertEquals(300000000L, getPrivateField(languages.get(0), "Population"));
        assertEquals(4.1, (Double) getPrivateField(languages.get(0), "Percentage"), 0.001);
    }

    @Test
    void testEmptyResultSet() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        ArrayList<Language> languages = language.getLanguages();

        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    void testLanguageDataConsistency() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Language")).thenReturn("Japanese");
        when(mockResultSet.getLong("Speakers")).thenReturn(125000000L);
        when(mockResultSet.getDouble("WorldPercentage")).thenReturn(1.7);

        ArrayList<Language> languages = language.getLanguages();

        String langName = (String) getPrivateField(languages.get(0), "Language");
        long population = (Long) getPrivateField(languages.get(0), "Population");
        double percentage = (Double) getPrivateField(languages.get(0), "Percentage");

        assertEquals("Japanese", langName);
        assertEquals(125000000L, population);
        assertEquals(1.7, percentage, 0.001);

        // Verify the data makes sense
        assertTrue(percentage >= 0 && percentage <= 100);
        assertTrue(population > 0);
        assertNotNull(langName);
        assertFalse(langName.isEmpty());
    }

    @Test
    void testLanguageSortingOrder() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("Language")).thenReturn("LanguageA", "LanguageB");
        when(mockResultSet.getLong("Speakers")).thenReturn(900000000L, 400000000L);
        when(mockResultSet.getDouble("WorldPercentage")).thenReturn(12.0, 5.5);

        ArrayList<Language> languages = language.getLanguages();

        assertEquals(2, languages.size());
        long pop1 = (Long) getPrivateField(languages.get(0), "Population");
        long pop2 = (Long) getPrivateField(languages.get(1), "Population");

        assertTrue(pop1 >= pop2);
        assertEquals(900000000L, pop1);
        assertEquals(400000000L, pop2);
    }

    // Test for zero percentage edge case
    @Test
    void testLanguageWithZeroPercentage() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Language")).thenReturn("Esperanto");
        when(mockResultSet.getLong("Speakers")).thenReturn(1000000L);
        when(mockResultSet.getDouble("WorldPercentage")).thenReturn(0.0);

        ArrayList<Language> languages = language.getLanguages();

        assertNotNull(languages);
        assertEquals(1, languages.size());
        assertEquals(0.0, (Double) getPrivateField(languages.get(0), "Percentage"), 0.001);
    }

    // Helper method to get private field values using reflection
    private Object getPrivateField(Language language, String fieldName) {
        try {
            Field field = Language.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(language);
        } catch (Exception e) {
            fail("Failed to access private field: " + fieldName + ", error: " + e.getMessage());
            return null;
        }
    }

    // Helper method to set private field values using reflection
    private void setPrivateField(Language language, String fieldName, Object value) {
        try {
            Field field = Language.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(language, value);
        } catch (Exception e) {
            fail("Failed to set private field: " + fieldName + ", error: " + e.getMessage());
        }
    }
}