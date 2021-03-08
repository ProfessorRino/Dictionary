# Dictionary
## JMDict Android App using Jetpack Compose
#### 1. Outline
A Dictionary app for Android using Compose (beta01) based on [The JMDict Project](https://www.edrdg.org/jmdict/j_jmdict.html) database.  
The Japanese, English and German entries are made available for SQL queries. 
#### 2. Setup
- Clone the project to Android Studio Canary (Arctic Fox). 
- Download the SQLite database file from [here](https://drive.google.com/file/d/1kMeppgWQuqW6S_65F0T1xYtthY0reZng/view?usp=sharing) or run [this script](https://github.com/ProfessorRino/JMDict2SQLite/blob/master/parseBuild.py) to build your own based on the latest JMDict file.
- Create a folder named *assets* in *app/src/main* and a folder *databases* in *assets*, then put the .db file inside. 
- You might have to delete *.idea/gradle.xml* and *.idea/workspace.xml* to start building.
#### 3. Functionality
##### a) First Row
- Input Field: automatically starts a query if not empty and unchanged for 3 seconds
- Clear Button (X): only visible when input field not empty
- Help: Drops help menu
- Help menu: selection displays the respective text in list area
##### b) Second Row
- Search Button: starts a query
- Checkboxes: If set Japanese, annotations, English or German are displayed respectively.
- Radioboxes: Set the type of SQL query for the next search and trigger a query if input field is not empty. 
  * **☆**   EQUALS something (full match): efficient because of indexing
  * **☆…**  LIKE something% (starts with): still efficient because the table is indexed and LIKE is set to case sensitive mode.
  * **…☆**  LIKE %something (ends with): much slower
  * **…☆…** LIKE %something% (includes): much slower
##### c) List Area (Lazy Column)
- Displays search results
- Displays help text etc.

![alt text](https://github.com/ProfessorRino/Dictionary/blob/master/ScreenshotDictionary.png "screenshot")

  



