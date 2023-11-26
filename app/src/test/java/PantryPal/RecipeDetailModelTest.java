package PantryPal;

import static org.junit.Assert.*;

import org.junit.*;
import utils.Account;
import utils.Recipe;

public class RecipeDetailModelTest {
  private MockHttpModel httpModel;
  private RecipeDetailModel recipeDetailModel;
  private Account account = new Account("", "");

  @Before
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
    String mockResponse = "{\"title\":\"" + title
      + "\",\"meal-type\":\"" + mealType 
        + "\",\"description\":\"" + description + "\"}";
    httpModel.setMockResponse(mockResponse);

    // Verify the output
    Recipe recipe = recipeDetailModel.read(title);
    assertEquals(recipe.getTitle(), title);
    assertEquals(recipe.getMealType(), mealType);
    assertEquals(recipe.getDescription(), description);
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
