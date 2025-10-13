# USE CASE: 1 View Capital Cities by Population in a Region

## CHARACTERISTIC INFORMATION

### Goal in Context

As a data analyst I want to view all capital cities in a region organized by population (largest to smallest) so that I can study population trends among regional capitals.

### Scope

Global population database.

### Level

Primary task.

### Preconditions

• System is running and accessible.
• Population data for countries and capital cities is available.
• Analyst is logged in if required.

### Success End Condition

A sorted list of regional capitals is displayed and exportable.

### Failed End Condition

No data displayed.

### Primary Actor

Data Analyst

### Trigger

Analyst selects a region to view capital cities.

## MAIN SUCCESS SCENARIO

1. Analyst navigates to the “Regional Capitals” section.
2. Analyst selects a region.
3. System retrieves capital cities in that region.
4. System sorts cities by population descending.
5. System displays city names and populations.
6. Analyst may export or analyze the data.

## EXTENSIONS

• Invalid region: display error message.
• No cities in region: display “no data available”.
• Cancel selection: return to main menu.

## SUB-VARIATIONS

None.

## SCHEDULE

Sorted and readable capital city list displayed, exportable.
