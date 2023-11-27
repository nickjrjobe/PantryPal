package server;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.Test;
import utils.Recipe;

public class RecipeListAPITest {
  RecipeListAPI dut = new RecipeListAPI(new SaveableRecipeData());
  String exceptionMessage = "Request type not supported";

  @Test
  public void testPut() {
    boolean exceptionHappened = false;
    try {
      dut.handlePut("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }

  @Test
  public void testDelete() {
    boolean exceptionHappened = false;
    try {
      dut.handleDelete("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }

  @Test
  public void testPost() {
    boolean exceptionHappened = false;
    try {
      dut.handlePost("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }

  @Test
  public void testGet() {
    try {
      // Setup data
      SaveableRecipeData data = new SaveableRecipeData();
      SaveableRecipeData data2 = new SaveableRecipeData();
      SaveableRecipeDataTest.deleteRecipeDataFile();
      RecipeListAPI api = new RecipeListAPI(data);
      SaveableRecipeDataTest.deleteRecipeDataFile();
      RecipeListAPI api2 = new RecipeListAPI(data2);
      SaveableRecipeDataTest.deleteRecipeDataFile();
      data.put("Scrambled eggs", new Recipe("Scrambled eggs", "breakfast", "eggs"));
      data.put(
          "Mac and cheese", new Recipe("Mac and cheese", "dinner", "step 1. mac\nstep 2.cheese"));

      /* test empty data */
      assertEquals(data.toJSON().toString(), api.handleGet("", ""), "get failed for empty data");
      assertEquals(
          data2.toJSON().toString(),
          api2.handleGet("", ""), 
          "get failed for empty data,empty list");

      /* test null data */
      assertEquals(data.toJSON().toString(), api.handleGet(null, null), "get failed for null data");
      assertEquals(
          data2.toJSON().toString(),
          api2.handleGet(null, null),
          "get failed for null data, empty list");

      /* test valid data */
      assertEquals(
          data.toJSON().toString(), api.handleGet("hi", "hello"), "get failed for valid data");
      assertEquals(
                    data2.toJSON().toString(),
          api2.handleGet("hi", "hello"),
          "get failed for valid data, empty list");
    } catch (Exception e) {
      fail("Got unexpected IO exception");
    }
  }
}
