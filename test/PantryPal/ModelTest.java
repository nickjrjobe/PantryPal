package PantryPal;

import java.io.IOException;
import utils.Recipe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

class NewRecipeModelTest {
  private MockHttpModel httpModel;
  private NewRecipeModel newRecipeModel;

  @Before
  public void setUp() {
    httpModel = new MockHttpModel();
    newRecipeModel = new NewRecipeModel(httpModel);
  }

  @Test
  public void testSendTranscript() {
    TranscriptResults transcriptResults;
    httpModel.setMockResponse("{\"transcript\":[\"test prompt\"],\"recipe\":{\"name\":\"test recipe\"}}");

    try {
      transcriptResults = newRecipeModel.sendTranscript(response);
    } catch (IOException e) {
      fail("IOException thrown");
    }


    // Get recipe details
    Recipe recipe = transcriptResults.recipe;
    assertEquals("test response", recipe.getTitle());
    
  }

  @Test
  public void testGetInitialTranscript() throws IOException {
    
  }

  @Test
  public void testReset() {
    
  }
}
