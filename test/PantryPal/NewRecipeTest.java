package PantryPal;
import static org.junit.Assert.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;


public class NewRecipeTest {

    RecipeCreatorStub recipeCreatorTest = new RecipeCreatorStub();
    VoiceToTextStub voiceToTextTest = new VoiceToTextStub();

    NewRecipeCreator newRecipeCreatorTest = new NewRecipeCreator(voiceToTextTest, recipeCreatorTest);

    @Test
	public void testHandleMealType() {
		newRecipeCreatorTest.handleMeal("snack");
		assertEquals(newRecipeCreatorTest.waitingForMeal, true);
        newRecipeCreatorTest.handleMeal("dinner");
		assertEquals(newRecipeCreatorTest.waitingForMeal, false);
	}
    @Test
	public void testInterpretRecipeResponse() {
        assertEquals(newRecipeCreatorTest.interpretRecipeResponse("Cereal\nPut milk into cereal").getDescription(), new Recipe("Cereal","Put milk into cereal").getDescription());
	}
}
