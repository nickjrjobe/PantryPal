package PantryPal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/** An interface for creating recipes. */
interface RecipeCreator {
  /**
   * Method for generating a recipe.
   *
   * @param meal The type of meal for the recipe.
   * @param ingredients The list of ingredients for the recipe.
   * @return A string response containing the generated recipe, or null if there is an error.
   */
  public String makeRecipe(String meal, String ingredients);
}

/**
 * The ChatGPTBot class implements the RecipeCreator interface and provides a way to generate
 * recipes using the OpenAI API.
 */
public class ChatGPTBot implements RecipeCreator {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
  private static final String MODEL = "text-davinci-003";
  private String apiKey;
  public String chatGptBotOutput;
  private static final String PROMPT =
      "Give me a %s recipe using any subset of the"
          + "  following ingredients. It does not need to use all of them: %s. Please"
          + " make the first line of your response the title of recipe, and the"
          + " following lines the recipe itself Thank you.";
  private static final int MAX_TOKENS = 500;

  public ChatGPTBot() {
    ConfigReader configReader = new ConfigReader();
    apiKey = configReader.getOpenAiApiKey();
  }

  /**
   * Generates a recipe based on the given meal and ingredients.
   *
   * @param meal The type of meal for the recipe.
   * @param ingredients The list of ingredients for the recipe.
   * @return A string containing the generated recipe, or null if there is an error.
   */
  public String makeRecipe(String meal, String ingredients) {
    try {
      String prompt = String.format(PROMPT, meal, ingredients);

      // Setup Request
      JSONObject requestBody = new JSONObject();
      requestBody.put("model", MODEL);
      requestBody.put("prompt", prompt);
      requestBody.put("max_tokens", MAX_TOKENS);
      requestBody.put("temperature", 1.0);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(new URI(API_ENDPOINT))
              .header("Content-Type", "application/json")
              .header("Authorization", String.format("Bearer %s", apiKey))
              .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
              .build();

      // Perform request
      HttpResponse<String> responseBody =
          client.send(request, HttpResponse.BodyHandlers.ofString());

      // Parse response
      JSONObject responseJson = new JSONObject(responseBody.body());
      JSONArray choices = responseJson.getJSONArray("choices");
      String generatedText = choices.getJSONObject(0).getString("text");
      chatGptBotOutput = generatedText;

    } catch (Exception e) {
      return null;
    }

    return chatGptBotOutput;
  }
}

/**
 * The MockChatGPTBot class implements the RecipeCreator interface and provides a mock
 * implementation for testing purposes.
 */
class MockChatGPTBot implements RecipeCreator {
  // Instance variables
  private String mockOutput = "";

  /**
   * A mock implementation of the makeRecipe method.
   *
   * @param meal The type of meal for the mock recipe.
   * @param ingredients The list of ingredients for the mock recipe.
   * @return A string containing a mock recipe output.
   */
  public String makeRecipe(String meal, String ingredients) {
    return this.mockOutput;
  }

  /**
   * Sets the mockMeal and mockIngredients instance variables.
   *
   * @param mockOutput The string that will be returned when calling makeRecipe
   */
  public void setOutput(String mockOutput) {
    this.mockOutput = mockOutput;
  }
}
