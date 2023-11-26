# cse-110-project-team-40

## How to Build
1. Install Java JDK - tested on versions 11 and 17
    - Verify by typing `java --version`
2. Install [gradle](https://gradle.org/install/)
    - Verify by typing `gradle --version`
3. Clone this repository
    - `git clone https://github.com/ucsd-cse110-fa23/cse-110-project-team-40`
    - Clone a branch: `git clone -b new-build-system https://github.com/ucsd-cse110-fa23/cse-110-project-team-40`
4. Set up MongoDB (Cloud Instructions, adapted from Lab 6)
    - Create a MongoDB account
    - Create a (Shared) database cluster of any name
    - On database, Connect -> Drivers -> Java 4.3 -> copy the connection string
    - Paste it into config.json - MongoDBURI
    - Go to Database Acccess on the left pane
    - Add a new user of any name/password
    - Replace the \<username> and \<password> fields in the MongoDBURI config
    - Go to Network Access, add your IP (may need to do whenever your wifi changes)
    - Set config.json - MongoDBDatabase to the *name of your database*
5. Set up OpenAI API
    - Create an OpenAI account
    - Go to https://platform.openai.com/api-keys
    - Create an API key
    - Copy the key into config.json - OpenAiApiKey
6. Do not commit your modified config.json to Github

## How to run
- Run the GUI app `./gradlew run`
- Run the server (must be running for the app to work) `./gradlew runServer`
- Run all tests `./gradlew test`
- Delete all build files `./gradlew clean`

## Style
- Uses [Google Style Guide](https://google.github.io/styleguide/javaguide.html)
- Indentation is 2 spaces
- View style issues `./gradlew lint`
- Automatically fix style issues `./gradlew format`

## Example config
```json
{
  "OpenAiApiKey": "sk-abcdefghijklmnopqrstuvwxyz",
  "MongoDBDatabase": "cluster0",
  "MongoDBURI":"mongodb+srv://username:securepass@cluster0.something7.mongodb.net/"
}
```
