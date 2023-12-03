package server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Recipe;

class InteractiveRecipeMakerStub implements InteractiveRecipeMaker {
  Recipe recipe = null;
  int resetCount = 0;
  List<String> prompts = new ArrayList<String>();
  String response;
  boolean shouldThrowException = false;
  String requestObserved;

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

  public void update(Observable o, Object data) {
    requestObserved = (String) data;
  }

  public Recipe regenerateRecipe(){
    return new Recipe("new " + recipe.getTitle(), recipe.getMealType(), recipe.getDescription());
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

  @BeforeEach
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
    assertEquals(oldResets, makerstub.resetCount, "should not have reset");

    makerstub.prompts = examplePrompts;
    json = api.makeResponseFromPrompts();
    assertEquals(
        new JSONObject(exampleExpectedResponse).getJSONArray("transcript").toString(),
        json.getJSONArray("transcript").toString());
    assertEquals(false, json.has("recipe"));
    assertEquals(oldResets, makerstub.resetCount, "should not have reset");

    /* set recipe and ensure proper state transitions occur */
    makerstub.recipe = new Recipe("Food", "breakfast", "steps");
    oldResets = makerstub.resetCount;
    json = api.makeResponseFromPrompts();
    assertEquals(new Recipe(json.getJSONObject("recipe")), makerstub.recipe);
    assertEquals(oldResets + 1, makerstub.resetCount, "should have reset");
  }
  
  @Test
  public void testMakeRegenerateResponse() {
    JSONObject json;
    int oldResets; // don't care about absolute value just that one occured or didnt
    makerstub.recipe = new Recipe("PB&J Sandwich", "breakfast", "Add peanut butter and jelly to sandwich bread.");

    /* ensure transcripts are properly converted */
    makerstub.prompts = emptyList;
    oldResets = makerstub.resetCount;
    json = api.makeRegenerateResponse();
    assertEquals(true, json.has("recipe"));

    makerstub.prompts = examplePrompts;
    json = api.makeResponseFromPrompts();
    assertEquals(
        new JSONObject(exampleExpectedResponse).getJSONArray("transcript").toString(),
        json.getJSONArray("transcript").toString());
    assertEquals(true, json.has("recipe"));
    assertEquals(makerstub.regenerateRecipe().getTitle(), new Recipe("new PB&J Sandwich", "breakfast", "Add peanut butter and jelly to sandwich bread.").getTitle());
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
    assertEquals(true, exceptionHappened, "invalid json should throw exception");

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
    assertEquals(false, exceptionHappened, "invalid meal should NOT throw exception");
  }

  @Test
  public void testGet() {
    makerstub.prompts = examplePrompts;


    try {
      assertEquals(api.handleGet("?prompts", ""), exampleExpectedResponse);
    } catch (Exception e) {
      fail("Delete should never throw exception");
    }

    /*You cannot call without specifying query */
     try {
      assertEquals(api.handleGet("", ""), "400 Bad Request");
    } catch (Exception e) {
      fail("Delete should never throw exception");
    }

    /* Checking whether the code works using regenerate prompt*/
    try {
      assertEquals(api.handleGet("?regenerate", ""), exampleExpectedResponse);
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
