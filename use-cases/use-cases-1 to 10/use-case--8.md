# USE CASE: 8 View Cities in a Country by Population

## CHARACTERISTIC INFORMATION

### Goal in Context

As a researcher I want to view all cities in a country organized by population (largest to smallest) so that I can identify major urban centers and analyze population distribution.

### Scope

Country-level population database.

### Level

Primary task.

### Preconditions

• System is running.
• Population data for cities available.
• Researcher logged in if required.

### Success End Condition

Sorted city list displayed and exportable.

### Failed End Condition

No data displayed.

### Primary Actor

Researcher

### Trigger

Researcher selects a country.

## MAIN SUCCESS SCENARIO

1. Navigate to “Country Cities”.
2. Select country.
3. Retrieve and sort cities by population.
4. Display city names and populations.

   ## EXTENSIONS

   • Invalid country: display error.
   • No data: display “no data available”.
   • Cancel: return to main menu.

   ## SUB-VARIATIONS

   None.

   ## SCHEDULE

   Sorted city list displayed and exportable.