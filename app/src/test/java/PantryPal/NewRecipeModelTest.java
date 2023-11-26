package PantryPal;

import java.io.IOException;
import utils.Recipe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewRecipeModelTest {
  private MockHttpModel httpModel;
  private NewRecipeModel newRecipeModel;

  @Before
  public void setUp() {
    httpModel = new MockHttpModel();
    newRecipeModel = new NewRecipeModel(httpModel);
  }

  @Test
  public void testSendTranscript() {
    // Set up the input and mock output
    TranscriptResults transcriptResults = null;
    String response = "Test response";
    httpModel.setMockResponse("{\"transcript\":[\"test prompt\"],\"recipe\":{\"title\":\"test recipe\", \"description\":\"test description\"}}");

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
    assertEquals("test description", recipe.getDescription());
    
    // Verify transcript details
    assertEquals(1, transcriptResults.prompts.size());
    assertEquals("test prompt", transcriptResults.prompts.get(0));
  }

  @Test
  public void testGetInitialTranscript() throws IOException {
    // Set up the input and mock output
    TranscriptResults transcriptResults = null;
    String response = "Test response";
    httpModel.setMockResponse("{\"transcript\":[\"test prompt\"],\"recipe\":{\"title\":\"test recipe\", \"description\":\"test description\"}}");

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
    assertEquals("test description", recipe.getDescription());
    
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
