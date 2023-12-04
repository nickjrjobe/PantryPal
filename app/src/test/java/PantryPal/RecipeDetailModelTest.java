package PantryPal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Account;
import utils.Recipe;

public class RecipeDetailModelTest {
  private MockHttpModel httpModel;
  private RecipeDetailModel recipeDetailModel;
  private Account account = new Account("", "");

  @BeforeEach
  public void setUp() {
    httpModel = new MockHttpModel();
    recipeDetailModel = new RecipeDetailModel(httpModel, account);
  }

  @Test
  public void testCreate() {
    Recipe recipe = new Recipe("test title", "test meal type", "test description");
    recipeDetailModel.create(recipe);
  }

  @Test
  public void testRead() {
    // Set the expected response
    String title = "testTitle";
    String mealType = "testMealType";
    String description = "testDescription";
    int creationTimestamp = 123456789;
    String mockResponse =
        "{\"title\":\""
            + title
            + "\",\"mealtype\":\""
            + mealType
            + "\",\"description\":\""
            + description
            + "\",\"creationTimestamp\":"
            + creationTimestamp
            + "}";
    httpModel.setMockResponse(mockResponse);

    // Verify the output
    Recipe recipe = recipeDetailModel.read(title);
    assertEquals(recipe.getTitle(), title);
    assertEquals(recipe.getMealType(), mealType);
    assertEquals(recipe.getDescription(), description);
    assertEquals(recipe.getCreationTimestamp(), creationTimestamp);
  }

  @Test
  public void testUpdate() {
    Recipe recipe = new Recipe("test title", "test meal type", "test description");
    recipeDetailModel.create(recipe);
  }

  @Test
  public void testDelete() {
    Recipe recipe = new Recipe("test title", "test meal type", "test description");
    recipeDetailModel.create(recipe);
  }
}
