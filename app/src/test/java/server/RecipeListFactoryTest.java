package server;

import org.junit.jupiter.api.Test;

import utils.RecipeListFactory;

import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

import utils.Recipe;

public class RecipeListFactoryTest {

    @Test
    public void testJSONObjectConstructor() {
        String rExampleResponse =
        "{\"recipe\":{\"mealtype\":\"breakfast\","
        + "\"description\":\"Add peanut butter and jelly to sandwich bread.\","
        + "\"title\":\"PB&J Sandwich\"}}";

        JSONObject jsonResponse = new JSONObject(rExampleResponse);

        // Test instantiation with JSONObject
        RecipeListFactory rlf = new RecipeListFactory(jsonResponse);
        List<Recipe> recipeList = rlf.buildList();
        assertEquals(1, recipeList.size(), "The list should contain exactly one recipe");
        
        // Test the recipes in the instantiation
        Recipe recipe = recipeList.get(0);
        assertEquals("PB&J Sandwich", recipe.getTitle(), "The title of the recipe should match");
        assertEquals("breakfast", recipe.getMealType(), "The meal type of the recipe should match");
        assertEquals("Add peanut butter and jelly to sandwich bread.", recipe.getDescription(), "The description of the recipe should match");
    }

    @Test
    public void testJSONArrayConstructor() {
        // Create a JSON array representing a list of recipes
        JSONArray jsonArray = new JSONArray();

        // Create JSON objects following the structure of rExampleResponse
        JSONObject details1 = new JSONObject();
        details1.put("mealtype", "breakfast");
        details1.put("description", "Recipe 1 description");
        details1.put("title", "Recipe 1 title");
        
        JSONObject details2 = new JSONObject();
        details2.put("mealtype", "lunch");
        details2.put("description", "Recipe 2 description");
        details2.put("title", "Recipe 2 title");

        // Add the recipe JSONObjects to the JSONArray
        jsonArray.put(details1);
        jsonArray.put(details2);

        RecipeListFactory rlf = new RecipeListFactory(jsonArray);
        List<Recipe> recipeList = rlf.buildList();

        // Assertions
        assertNotNull(recipeList, "The list of recipes should not be null");
        assertEquals(2, recipeList.size(), "The list should contain two recipes");

        // Check the details of the first recipe in the list
        Recipe recipe1 = recipeList.get(0);
        assertEquals("Recipe 1 title", recipe1.getTitle(), "The title of the first recipe should match");
        assertEquals("breakfast", recipe1.getMealType(), "The meal type of the first recipe should match");
        assertEquals("Recipe 1 description", recipe1.getDescription(), "The description of the first recipe should match");

        // Check the details of the second recipe in the list
        Recipe recipe2 = recipeList.get(1);
        assertEquals("Recipe 2 title", recipe2.getTitle(), "The title of the second recipe should match");
        assertEquals("lunch", recipe2.getMealType(), "The meal type of the second recipe should match");
        assertEquals("Recipe 2 description", recipe2.getDescription(), "The description of the second recipe should match");
    }

    @Test
    public void testBuildJSON() {
        String rExampleResponse =
            "{\"recipe\":{\"mealtype\":\"breakfast\","
            + "\"description\":\"Add peanut butter and jelly to sandwich bread.\","
            + "\"title\":\"PB&J Sandwich\"}}";

        JSONObject jsonResponse = new JSONObject(rExampleResponse);

        // Test instantiation with JSONObject
        RecipeListFactory rlf = new RecipeListFactory(jsonResponse);

        // Call buildJSON
        JSONObject result = rlf.buildJSON();

        // Verify the structure of the resulting JSON
        assertTrue(result.has("recipes"), "JSON object should have a 'recipes' key");
        JSONArray recipesArray = result.getJSONArray("recipes");
        assertNotNull(recipesArray, "The 'recipes' key should not be null");
        assertEquals(rlf.buildList().size(), recipesArray.length(), "The JSONArray should have the same number of elements as the recipe list");
    
        // Verify that the JSONObjects within the JSONArray match the recipes in the list
        for (int i = 0; i < recipesArray.length(); i++) {
            JSONObject recipeJSON = recipesArray.getJSONObject(i);
            Recipe recipe = rlf.buildList().get(i);

            // Example assertion, assuming Recipe class has a toJSON() method that is used in buildJSON()
            assertEquals(recipe.toJSON().toString(), recipeJSON.toString(), "The JSON object should match the expected recipe JSON");
        }
    }

    @Test

    public void testSort() {
        JSONArray jsonArray = new JSONArray();

        // Create JSON objects following the structure of rExampleResponse
        JSONObject details1 = new JSONObject();
        details1.put("mealtype", "breakfast");
        details1.put("description", "Recipe 1 description");
        details1.put("title", "Banana");
        details1.put("creationTimestamp", 0);

        JSONObject details2 = new JSONObject();
        details2.put("mealtype", "lunch");
        details2.put("description", "Recipe 2 description");
        details2.put("title", "Apple");
        details1.put("creationTimestamp", 10);

        // Add the recipe JSONObjects to the JSONArray
        jsonArray.put(details1);
        jsonArray.put(details2);

        RecipeListFactory rlf = new RecipeListFactory(jsonArray);

        String sortSelection = "A-Z";
        RecipeListFactory rlfNew = rlf.sort(sortSelection);
        List<Recipe> sortedRecipes = rlfNew.buildList();
        assertEquals("Apple", sortedRecipes.get(0).getTitle());
        assertEquals("Banana", sortedRecipes.get(1).getTitle());

        sortSelection = "Z-A";
        rlfNew = rlf.sort(sortSelection);
        sortedRecipes = rlfNew.buildList();
        assertEquals("Banana", sortedRecipes.get(0).getTitle());
        assertEquals("Apple", sortedRecipes.get(1).getTitle());

        sortSelection = "Oldest";
        rlfNew = rlf.sort(sortSelection);
        sortedRecipes = rlfNew.buildList();
        assertEquals("Banana", sortedRecipes.get(0).getTitle());
        assertEquals("Apple", sortedRecipes.get(1).getTitle());

        sortSelection = "Newest";
        rlfNew = rlf.sort(sortSelection);
        sortedRecipes = rlfNew.buildList();
        assertEquals("Apple", sortedRecipes.get(0).getTitle());
        assertEquals("Banana", sortedRecipes.get(1).getTitle());

        sortSelection = "invalid input";
        List<Recipe> originalList = rlf.buildList();
        rlfNew = rlf.sort(sortSelection);
        sortedRecipes = rlfNew.buildList();

        // Assert that no exception is thrown and the list remains unchanged
        assertEquals(originalList.size(), sortedRecipes.size(), "List size should remain unchanged");
        for (int i = 0; i < originalList.size(); i++) {
            assertEquals(originalList.get(i).getTitle(), sortedRecipes.get(i).getTitle(), "List order should remain unchanged");
        }
    }
}
