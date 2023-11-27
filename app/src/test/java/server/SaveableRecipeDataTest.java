package server;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Recipe;

public class SaveableRecipeDataTest {
  SaveableRecipeData data;
  Recipe testRecipe = new Recipe("brocolli", "boil");
  String recipeDescription = "{\"brocolli\":{\"description\":\"boil\",\"title\":\"brocolli\"}}";

  public static void deleteRecipeDataFile() {
    File f = new File(SaveableRecipeData.path);
    f.delete();
  }

  @AfterEach
  public void cleanup() {
    deleteRecipeDataFile();
  }

  public String currentFileContents() {
    try {
      return Files.readString(Path.of(SaveableRecipeData.path));
    } catch (Exception e) {
      return "";
    }
  }

  @BeforeEach
  public void setup() {
    deleteRecipeDataFile();
    System.out.println("\nWARNING: tests delete persistent recipe data!!!!\n");
    data = new SaveableRecipeData();
  }

  @Test
  public void testPut() {
    Recipe testRecipe = new Recipe("brocolli", "boil");
    data.put("recipe", testRecipe);
    assertEquals(recipeDescription, currentFileContents());
  }

  /* BSD Story 1 scenario 2 */
  @Test
  public void testInitNewUser() {
    assertEquals(true, data.toJSON().isEmpty());
  }

  /* BSD Story 1 scenario 1 */
  @Test
  public void testInitPreviousUser() {
    /* add previous recipes of user */
    data.put("sandwich", new Recipe("sandwich", "sloopy joes"));
    data.put("pancakes", new Recipe("pancakes", "mix batter"));
    /* create new data object to simulate starting and stopping server */
    data = new SaveableRecipeData();
    assertEquals(false, data.toJSON().isEmpty());
  }
}
