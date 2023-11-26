package utils;

import java.io.FileReader;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONTokener;

/** This class is responsible for reading configuration data from a JSON file. */
public class ConfigReader {
  private String openAiApiKey = "";
  private String mongoDBURI = "";
  private String mongoDBDatabase = "";

  /** Initializes a new instance of the ConfigReader class and reads the configuration. */
  public ConfigReader() {
    readConfig();
  }

  /** Reads the OpenAI API key from the JSON configuration file. */
  public void readConfig() {
    try {
      String filePath = "config.json";

      // Create a FileReader to read the JSON file
      FileReader fileReader = new FileReader(filePath);

      // Parse the JSON data using JSONTokener
      JSONTokener tokener = new JSONTokener(fileReader);
      JSONObject jsonObject = new JSONObject(tokener);

      // Get the OpenAiApiKey value from the JSON object
      try {
        this.openAiApiKey = jsonObject.getString("OpenAiApiKey");
      } catch (Exception e) {
        System.err.println("Warn: OpenAiApiKey not provided in config.json");
      }
      try {
        this.mongoDBDatabase = jsonObject.getString("MongoDBDatabase");
      } catch (Exception e) {
        System.err.println("Warn: MongoDBDatabase not provided in config.json");
      }
      try {
        this.mongoDBURI = jsonObject.getString("MongoDBURI");
      } catch (Exception e) {
        System.err.println("Warn: MongoDBURI not provided in config.json");
      }

      // Close the file reader
      fileReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the OpenAI API key.
   *
   * @return The OpenAI API key as a String.
   */
  public String getOpenAiApiKey() {
    return this.openAiApiKey;
  }

  public String getMongoDBURI() {
    return this.mongoDBURI;
  }

  public String getMongoDBDatabase() {
    return this.mongoDBDatabase;
  }
}
