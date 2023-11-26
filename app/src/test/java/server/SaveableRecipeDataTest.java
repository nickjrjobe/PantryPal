package server;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Recipe;

public class SaveableRecipeDataTest {
  SaveableRecipeData data;
  Recipe testRecipe = new Recipe("brocolli", "lunch", "boil");
  String recipeDescription = "{\"brocolli\":{\"description\":\"boil\",\"title\":\"brocolli\"}}";

  public static void deleteRecipeDataFile() {
    File f = new File(SaveableRecipeData.path);
    f.delete();
  }

  @After
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

  @Before
  public void setup() {
    deleteRecipeDataFile();
    System.out.println("\nWARNING: tests delete persistent recipe data!!!!\n");
    data = new SaveableRecipeData();
  }

  @Test
  public void testPut() {
    Recipe testRecipe = new Recipe("brocolli", "lunch", "boil");
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
    data.put("sandwich", new Recipe("sandwich", "lunch", "sloopy joes"));
    data.put("pancakes", new Recipe("pancakes", "breakfast", "mix batter"));
    /* create new data object to simulate starting and stopping server */
    data = new SaveableRecipeData();
    assertEquals(false, data.toJSON().isEmpty());
  }
}
