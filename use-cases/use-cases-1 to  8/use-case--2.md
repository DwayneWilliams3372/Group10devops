# USE CASE: 2 View Countries in a Continent by Population

## CHARACTERISTIC INFORMATION

### Goal in Context

As a data analyst I want to view all countries in a continent organized by population (largest to smallest) so that I can compare population sizes within that continent.

### Scope

Global population database.

### Level

Primary task.

### Preconditions

• System is running.
• Population data for countries is available.
• Analyst is logged in if required.

### Success End Condition

A sorted list of countries in the selected continent is displayed and exportable.

### Failed End Condition

No data displayed.

### Primary Actor

Data Analyst

### Trigger

Analyst selects a continent.

## MAIN SUCCESS SCENARIO
1. Analyst navigates to the “Continent Countries” section.
2. Analyst selects a continent.
3. System retrieves countries in that continent.
4. System sorts countries by population descending.
5. System displays country names and populations.

## EXTENSIONS

• Invalid continent: display error message.
• No data available: display “no data available”.
• Cancel selection: return to main menu.

## SUB-VARIATIONS

None.

## SCHEDULE

Sorted country list displayed and exportable.
