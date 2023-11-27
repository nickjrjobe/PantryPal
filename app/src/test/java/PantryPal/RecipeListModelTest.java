package PantryPal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecipeListModelTest {
  private MockHttpModel httpModel;
  private RecipeListModel recipeListModel;

  @BeforeEach
  public void setUp() {
    httpModel = new MockHttpModel();
    recipeListModel = new RecipeListModel(httpModel);
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
    List<String> result = recipeListModel.getRecipeList();
    for (int i = 0; i < result.size(); i++) {
      assertEquals("title" + (i + 1), result.get(i));
    }
  }

  @Test
  public void testProcessResponse() {
    String response =
        "{"
            + "\"title1\": \"value1\","
            + "\"title2\": \"value2\","
            + "\"title3\": \"value3\""
            + "}";

    // Test the method
    List<String> result = recipeListModel.processResponse(response);
    for (int i = 0; i < result.size(); i++) {
      assertEquals("title" + (i + 1), result.get(i));
    }
  }
}
