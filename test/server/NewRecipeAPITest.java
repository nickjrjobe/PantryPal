package server;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import utils.Recipe;

class InteractiveRecipeMakerStub implements InteractiveRecipeMaker {
  Recipe recipe = null;
  int resetCount = 0;
  List<String> prompts = new ArrayList<String>();
  String response;
  boolean shouldThrowException = false;

  public Recipe getRecipe() {
    return recipe;
  }

  public void reset() {
    resetCount += 1;
  }

  public List<String> getPrompts() {
    return prompts;
  }

  public void readResponse(String response) throws IllegalArgumentException {
    this.response = response;
    if (shouldThrowException) throw new IllegalArgumentException("example exception");
  }
}

public class NewRecipeAPITest {
  InteractiveRecipeMakerStub makerstub;
  NewRecipeAPI api;
  ArrayList<String> emptyList = new ArrayList<String>();
  ArrayList<String> examplePrompts = new ArrayList<String>(Arrays.asList("fee", "fi", "fo", "fum"));
  String invalidJson = "this/is-definitely.not;json";
  String validJson = "{\"response\":\"breakfast\"}";
  String exampleExpectedResponse = "{\"transcript\":[\"fee\",\"fi\",\"fo\",\"fum\"]}";

  @Before
  public void setup() {
    makerstub = new InteractiveRecipeMakerStub();
    api = new NewRecipeAPI(makerstub);
  }

  @Test
  public void testMakeResponseFromPrompts() {
    JSONObject json;
    int oldResets; // don't care about absolute value just that one occured or didnt
    makerstub.recipe = null;

    /* ensure transcripts are properly converted */
    makerstub.prompts = emptyList;
    oldResets = makerstub.resetCount;
    json = api.makeResponseFromPrompts();
    assertEquals(0, json.getJSONArray("transcript").length());
    assertEquals(false, json.has("recipe"));
    assertEquals("should not have reset", oldResets, makerstub.resetCount);

    makerstub.prompts = examplePrompts;
    json = api.makeResponseFromPrompts();
    assertEquals(
        new JSONObject(exampleExpectedResponse).getJSONArray("transcript").toString(),
        json.getJSONArray("transcript").toString());
    assertEquals(false, json.has("recipe"));
    assertEquals("should not have reset", oldResets, makerstub.resetCount);

    /* set recipe and ensure proper state transitions occur */
    makerstub.recipe = new Recipe("Food", "steps");
    oldResets = makerstub.resetCount;
    json = api.makeResponseFromPrompts();
    assertEquals(makerstub.recipe, new Recipe(json.getJSONObject("recipe")));
    assertEquals("should have reset", oldResets + 1, makerstub.resetCount);
  }

  @Test
  public void testPut() {
    boolean exceptionHappened = false;
    try {
      api.handlePut("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(HttpAPITest.exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }

  @Test
  public void testDelete() {
    try {
      /* ensure delete calls reset and is always succesfull*/
      int oldResets = makerstub.resetCount;
      assertEquals("200 OK", api.handleDelete("", ""));
      assertEquals(oldResets + 1, makerstub.resetCount);
      /* test twice to ensure idempotency */
      assertEquals("200 OK", api.handleDelete("", ""));
      assertEquals(oldResets + 2, makerstub.resetCount);
      ;
    } catch (Exception e) {
      fail("delete should never throw exception");
    }
  }

  @Test
  public void testPost() {
    makerstub.prompts = examplePrompts;
    /* test invalid json string */
    boolean exceptionHappened = false;
    try {
      api.handlePost("", invalidJson);
    } catch (Exception e) {
      exceptionHappened = true;
    }
    assertEquals("invalid json should throw exception", true, exceptionHappened);

    /* test exception in meal creation logic not passed */
    makerstub.shouldThrowException = true;
    exceptionHappened = false;
    try {
      assertEquals(exampleExpectedResponse, api.handlePost("", validJson));
    } catch (Exception e) {
      exceptionHappened = true;
    }
    makerstub.shouldThrowException = false;

    /* test invalid meal */
    assertEquals("invalid meal should NOT throw exception", false, exceptionHappened);
  }

  @Test
  public void testGet() {
    makerstub.prompts = examplePrompts;
    try {
      assertEquals(api.handleGet("", ""), exampleExpectedResponse);
    } catch (Exception e) {
      fail("Delete should never throw exception");
    }
  }

  @Test
  public void testGetJSONRequest() {
    /* test invalid json string */
    boolean exceptionHappened = false;
    try {
      api.getJSONRequest(invalidJson);
    } catch (Exception e) {
      assertEquals("Response was not JSON", e.getMessage());
      exceptionHappened = true;
    }
    assertEquals(true, exceptionHappened);

    /* test valid json string */
    exceptionHappened = false;
    try {
      api.getJSONRequest(validJson);
    } catch (Exception e) {
      assertEquals("Response was not JSON", e.getMessage());
      exceptionHappened = true;
    }
  }
}
