package PantryPal;
import static org.junit.Assert.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

class RecipeCreatorStub implements RecipeCreator {
    public String makeRecipe(String meal, String ingredients){
        return "Sandwich\n Add mayonaise";
    }
}
class VoiceToTextStub implements VoiceToText{
    public Boolean waitingForMeal = true;
    public void startRecording(){}
    public void stopRecording(){}
    public String getTranscript(){String response = waitingForMeal ? "breakfast" : "mayonaise";
        waitingForMeal = false;
        return response;
    }
}


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
