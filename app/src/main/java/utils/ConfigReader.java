package utils;

import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import org.json.JSONObject;
import org.json.JSONTokener;

/** This class is responsible for reading configuration data from a JSON file. */
public class ConfigReader {
  private String openAiApiKey = "";
  private String mongoDBURI = "";
  private String mongoDBDatabase = "";
  private String hostName = "";
  private String remoteServerIP = "";

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
      try {
        this.hostName = jsonObject.getString("HostName");
      } catch (Exception e) {
      }
      if (hostName == "") {
        resolveHostName();
      }
      try {
        this.remoteServerIP = jsonObject.getString("RemoteServerIP");
        if (remoteServerIP == null) {
          System.err.println("Warn: RemoteServerIP not provided in config.json");
        }
      } catch (Exception e) {
        System.err.println("Warn: RemoteServerIP not provided in config.json");
      }
      // Close the file reader
      fileReader.close();
      System.err.println("Your IP is: " + hostName);
      System.err.println("The server you are trying to connect to is: \"" + remoteServerIP + "\"");
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

  public String getRemoteServerIP() {
    return this.remoteServerIP;
  }

  public void resolveHostName() {
    try {
      this.hostName = Inet4Address.getLocalHost().getHostAddress();
    } catch (Exception e) {
      System.err.println(
          "Warn: HostName not provided in config.json and could not be automatically configured");
    }
  }

  public String getHostName() {
    System.err.println("Your IP is: " + hostName);
    return this.hostName;
  }
}
