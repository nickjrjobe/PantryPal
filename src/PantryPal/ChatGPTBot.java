package PantryPal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An interface for creating recipes.
 */
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
 * The ChatGPTBot class implements the RecipeCreator interface and provides a way to generate recipes
 * using the OpenAI API.
 */
public class ChatGPTBot implements RecipeCreator {
    // Constants for API access
    private static String API_ENDPOINT;
    private static String API_KEY;
    private static String MODEL;
    public String ChatGPTBotOutput;  // Store the generated recipe

    public ChatGPTBot() {
        API_ENDPOINT = "https://api.openai.com/v1/completions";
        ConfigReader configReader = new ConfigReader();
        API_KEY = configReader.getOpenAiApiKey();
        MODEL = "text-davinci-003";
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
            // Create a prompt for generating a recipe based on the meal and ingredients
            String prompt = "Give me a " + meal + " recipe using the following ingredients: " + ingredients + ". Thank you.";
            System.out.println(prompt);

            // Maximum number of tokens for the response
            int maxTokens = 100;

            // Create a JSON request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", MODEL);
            requestBody.put("prompt", prompt);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("temperature", 1.0);

            // Create the HTTP Client
            HttpClient client = HttpClient.newHttpClient();

            // Create the request object
            HttpRequest request = HttpRequest
            .newBuilder()
            .uri(new URI(API_ENDPOINT))
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s", API_KEY))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();

            // Send the request and receive the response
            HttpResponse<String> responseBody = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
            );

            // Parse the JSON response
            JSONObject responseJson = new JSONObject(responseBody.body());
            JSONArray choices = responseJson.getJSONArray("choices");
            String generatedText = choices.getJSONObject(0).getString("text");
            ChatGPTBotOutput = generatedText;

        } catch(Exception e) {
            // How can u possibly screw this up HmmHMHmmMMMmMHMmmmMmMMmmmm 🤔
            return null;  // Return null in case of an error
        }

        return ChatGPTBotOutput;
    }
}

/**
 * The mockChatGPTBot class implements the RecipeCreator interface and provides a mock implementation
 * for testing purposes.
 */
class mockChatGPTBot implements RecipeCreator {
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
