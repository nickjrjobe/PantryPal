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
  Recipe testRecipe = new Recipe("brocolli", "boil");
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
    Recipe testRecipe = new Recipe("brocolli", "boil");
    data.put("recipe", testRecipe);
    assertEquals(recipeDescription, currentFileContents());
  }
}
