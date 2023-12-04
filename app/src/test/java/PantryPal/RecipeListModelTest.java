package PantryPal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Account;
import utils.Recipe;

public class RecipeListModelTest {
  private MockHttpModel httpModel;
  private RecipeListModel recipeListModel;
  private Account account = new Account("", "");

  @BeforeEach
  public void setUp() {
    httpModel = new MockHttpModel();
    recipeListModel = new RecipeListModel(httpModel, account);
  }

  @Test
  public void testGetRecipeList() {
    // Set the expected response from the mock http model
    String response =
        "{"
            + "\"title1\": \"value1\","
            + "\"title2\": \"value2\","
            + "\"title3\": \"value3\""
            + "}";
    httpModel.setMockResponse(response);

    // Test the method
    List<Recipe> result = recipeListModel.getRecipeList("A-Z");
    for (int i = 0; i < result.size(); i++) {
      assertEquals("title" + (i + 1), result.get(i));
    }
  }

  @Test
  public void testProcessResponse() {
    String response =
        "{\"recipe\":"
            + "{\"title\":\"test recipe\","
            + "\"mealtype\":\"test meal type\","
            + "\"description\":\"test description\","
            + "\"creationTimestamp\":-1"
            + "}}";

    // Test the method
    List<Recipe> result = recipeListModel.processResponse(response);
    Recipe recipe = result.get(0);
    assertEquals(recipe.getTitle(), "test recipe");
    assertEquals(recipe.getMealType(), "test meal type");
    assertEquals(recipe.getDescription(), "test description");
    assertEquals(recipe.getCreationTimestamp(), -1);
  }
}
