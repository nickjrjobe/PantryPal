package server;

import static org.junit.Assert.*;

import java.io.*;
import org.junit.Test;
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
      data.put("Scrambled eggs", new Recipe("Scrambled eggs", "eggs"));
      data.put("Mac and cheese", new Recipe("Mac and cheese", "step 1. mac\nstep 2.cheese"));

      /* test empty data */
      assertEquals("get failed for empty data", data.toJSON().toString(), api.handleGet("", ""));
      assertEquals(
          "get failed for empty data,empty list",
          data2.toJSON().toString(),
          api2.handleGet("", ""));

      /* test null data */
      assertEquals("get failed for null data", data.toJSON().toString(), api.handleGet(null, null));
      assertEquals(
          "get failed for null data, empty list",
          data2.toJSON().toString(),
          api2.handleGet(null, null));

      /* test valid data */
      assertEquals(
          "get failed for valid data", data.toJSON().toString(), api.handleGet("hi", "hello"));
      assertEquals(
          "get failed for valid data, empty list",
          data2.toJSON().toString(),
          api2.handleGet("hi", "hello"));
    } catch (Exception e) {
      assertEquals("Got unexpected IO exception", false, true);
    }
  }
}
