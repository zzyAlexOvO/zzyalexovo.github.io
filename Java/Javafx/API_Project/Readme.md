# User Instruction

## Run the application
To run the application, use **"Gradle run --args="online online"**
The first argument represents the input API while the second argument represents the output API.
To run the test suites, use **Gradle test**

## Top menu
The menu on the top for the whole time contains two features. 
**Music** is for controlling the background music. 
**Language** is for controlling the displaying language.

## Loading screen
The loading screen initially has 15-second loading time.
However, you can click the **skip** button to avoid waiting.
Either way would lead you to the **search** page.

## Search screen
**Search screen** is the main page of this application.
Here you can type your search pattern in the text field, then click **search** button or simply press **enter** from keyboard. This would let the application to search for the character based on your pattern on the background. When it finishes, the downside **table** would show the result. To further navigate a specific character, please press the corresponding **view** button on its right column of the table.
**Clear cache** button is used for clearing the cache.
**Breadcrumb** is at the bottom for you to navigate backwards.

## Character page
**Left part** of this page contains the character's name, image and two buttons.
**back** button can make you go back to the search page.
**send report** button can make you send a report to a email address you named.
>**PS:** The image loading is concurrent, please wait a short period of time if it does not immediately show up.

**Right part** of this page is a table containing all the comics this character appears.
Pressing **view** button of one comic makes you go to that comic's detail page.

## Comic page
This page is very similar to the character page. Except, it has no image showing and the right part is the table of its characters instead of comics.

# Features declaration (Distinction)

## Credit
Data is cached to a database as required.
Database will be created if it does not exist.
### Two optional features
- Loading page including a "splash" image and a bottom process bar

- Background music which can be turned off/on

## Distinction
Concurrency is implemented but for only two locations.
> The character's image loading, and the search function.

Advanced feature: **String localisation**
> Currently, there are two languages to be chosen, Chinese and English.
> You may find the language configuration files in the "resources/languagePack" folder, and feel free to add more language file using the same structure.
