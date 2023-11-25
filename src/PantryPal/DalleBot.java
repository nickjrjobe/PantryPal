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
import org.json.JSONException;
import org.json.JSONObject;
import utils.ConfigReader;

/**generates an image using the OpenAI DALL-E API., called by .... */
public class DalleBot {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
  private static final String MODEL = "dall-e-2";
  private static String token;

  public DalleBot() {
    ConfigReader configReader = new ConfigReader();
    token = configReader.getOpenAiApiKey();
  }

  public String mockGenerateImage(String recipeName, String recipeDescription){
    System.out.println("Generating image for " + recipeName + "...");
    System.out.println("Image generated!");
    String imagePath = "src/PantryPal/recipeImages/" + recipeName + ".jpg";
    System.out.println("Image saved to " + imagePath);
    return imagePath;

  }
  public String generateImage(String recipeName, String recipeDescription) throws IOException, InterruptedException {
    String prompt = "a photo of a " + recipeName + " recipe.\n\n" + recipeDescription;
    JSONObject requestBody = this.createRequestBody(1, "256x256", prompt);
    HttpClient client = this.initializeHttpClient();
    // add error handling
    String imagePath = "recipeImages/" + recipeName + ".jpg";

    HttpResponse<String> response = this.sendRequest(client, requestBody);
    String generatedImageURL = this.processResponse(response);
    this.downloadImage(generatedImageURL,imagePath);
    return imagePath;
  }
  private JSONObject createRequestBody(int n, String size, String prompt) {
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
        String responseBody = response.body();
        System.out.println("Response: " + responseBody); // Log the entire response

        JSONObject responseJson = new JSONObject(responseBody);

        // Check if the response contains the "data" key
        if (!responseJson.has("data")) {
            throw new JSONException("Response does not contain 'data' key");
        }

        JSONArray dataArray = responseJson.getJSONArray("data");
        return dataArray.getJSONObject(0).getString("url");
    }


  private void downloadImage(String imageURL, String path) {
    try {
      InputStream in = new URI(imageURL).toURL().openStream();
      Files.copy(in, Paths.get(path));
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

