package PantryPal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Account;
import utils.Recipe;

public class NewRecipeModelTest {
  private MockHttpModel httpModel;
  private NewRecipeModel newRecipeModel;
  private Account account = new Account("", "");

  @BeforeEach
  public void setUp() {
    httpModel = new MockHttpModel();
    newRecipeModel = new NewRecipeModel(httpModel, account);
  }

  @Test
  public void testSendTranscript() {
    // Set up the input and mock output
    TranscriptResults transcriptResults = null;
    String response = "Test response";
    httpModel.setMockResponse(
        "{\"transcript\":[\"test prompt\"],\"recipe\":{\"title\":\"test recipe\","
            + "\"mealtype\":\"test meal type\","
            + "\"description\":\"test description\","
            + "\"creationTimestamp\":-1}}");

    // Call the method
    try {
      transcriptResults = newRecipeModel.sendTranscript(response);
    } catch (IOException e) {
      fail("IOException thrown");
    }

    // Verify transcriptResults is not null
    assertNotNull(transcriptResults);

    // Verify recipe details
    Recipe recipe = transcriptResults.recipe;
    assertEquals("test recipe", recipe.getTitle());
    assertEquals("test meal type", recipe.getMealType());
    assertEquals("test description", recipe.getDescription());
    assertEquals(-1, recipe.getCreationTimestamp());

    // Verify transcript details
    assertEquals(1, transcriptResults.prompts.size());
    assertEquals("test prompt", transcriptResults.prompts.get(0));
  }

  @Test
  public void testGetInitialTranscript() throws IOException {
    // Set up the input and mock output
    TranscriptResults transcriptResults = null;
    String response = "Test response";
    httpModel.setMockResponse(
        "{\"transcript\":[\"test prompt\"],\"recipe\":{\"title\":\"test recipe\","
            + "\"mealtype\":\"test meal type\","
            + "\"description\":\"test description\","
            + "\"creationTimestamp\":-1}}");

    // Call the method
    try {
      transcriptResults = newRecipeModel.sendTranscript(response);
    } catch (IOException e) {
      fail("IOException thrown");
    }

    // Verify transcriptResults is not null
    assertNotNull(transcriptResults);

    // Verify recipe details
    Recipe recipe = transcriptResults.recipe;
    assertEquals("test recipe", recipe.getTitle());
    assertEquals("test meal type", recipe.getMealType());
    assertEquals("test description", recipe.getDescription());
    assertEquals(-1, recipe.getCreationTimestamp());

    // Verify transcript details
    assertEquals(1, transcriptResults.prompts.size());
    assertEquals("test prompt", transcriptResults.prompts.get(0));
  }

  @Test
  public void testReset() {
    httpModel.setMockResponse("");
    newRecipeModel.reset();
  }
}
