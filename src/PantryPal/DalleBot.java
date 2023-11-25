package PantryPal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.ConfigReader;

/** Takes in a prompt and generates an image using the OpenAI DALL-E API. */
public class DalleBot {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
  private static final String MODEL = "dall-e-2";
  private static String token;
  private String prompt;

  public DalleBot(String prompt) {
    ConfigReader configReader = new ConfigReader();
    token = configReader.getOpenAiApiKey();
    this.prompt = prompt;
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    DalleBot bot = new DalleBot("Your prompt here");
    // replace the prompt to take in the chatgpt output
    JSONObject requestBody = bot.createRequestBody(1, "128x128");
    HttpClient client = bot.initializeHttpClient();
    HttpResponse<String> response = bot.sendRequest(client, requestBody);
    String generatedImageURL = bot.processResponse(response);
    bot.downloadImage(generatedImageURL);
  }

  private JSONObject createRequestBody(int n, String size) {
    JSONObject requestBody = new JSONObject();
    requestBody.put("model", MODEL);
    requestBody.put("prompt", prompt);
    requestBody.put("n", n);
    requestBody.put("size", size);
    return requestBody;
  }

  private HttpClient initializeHttpClient() {
    return HttpClient.newHttpClient();
  }

  private HttpResponse<String> sendRequest(HttpClient client, JSONObject requestBody)
      throws IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(API_ENDPOINT))
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s", token))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();

    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private String processResponse(HttpResponse<String> response) {
    JSONObject responseJson = new JSONObject(response.body());
    JSONArray dataArray = responseJson.getJSONArray("data");
    return dataArray.getJSONObject(0).getString("url");
  }

  private void downloadImage(String imageURL) {
    try {
      InputStream in = new URI(imageURL).toURL().openStream();
      Files.copy(in, Paths.get("recipeImage.jpg"));
      in.close();
    } catch (URISyntaxException e) {
      System.err.println("Invalid URL syntax: " + imageURL);
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("I/O Error when saving the image");
      e.printStackTrace();
    }
  }
}
