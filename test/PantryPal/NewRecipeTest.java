package PantryPal;

import static org.junit.Assert.*;

import java.io.*;
import org.junit.Test;

class RecipeCreatorStub implements RecipeCreator {
  String recipe;

  public String makeRecipe(String meal, String ingredients) {
    return recipe;
  }

  public void setRecipe(String recipe) {
    this.recipe = recipe;
  }
}

class VoiceToTextStub implements VoiceToText {
  public Boolean waitingForMeal = true;
  String mealType;
  String ingredients;

  public void startRecording() {}

  public void stopRecording() {}

  public String getTranscript() {
    String response = waitingForMeal ? mealType : ingredients;
    waitingForMeal = false;
    return response;
  }

  public void setMealType(String mealType) {
    this.mealType = mealType;
  }

  public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
  }
}

public class NewRecipeTest {

  RecipeCreatorStub recipeCreatorTest = new RecipeCreatorStub();
  VoiceToTextStub voiceToTextTest = new VoiceToTextStub();

  NewRecipeCreator newRecipeCreatorTest = new NewRecipeCreator(voiceToTextTest, recipeCreatorTest);

  @Test
  public void testHandleMealType() {
    /* Ensuring calling with improper meal type does not advance state */
    newRecipeCreatorTest.handleMeal("snack");
    assertEquals(newRecipeCreatorTest.waitingForMeal, true);
    /* Ensuring calling with proper meal type advances state */
    newRecipeCreatorTest.handleMeal("dinner");
    assertEquals(newRecipeCreatorTest.waitingForMeal, false);
  }

  @Test
  public void testInterpretRecipeResponse() {
    /* Checking against expected format received from ChatGPT */
    assertEquals(
        newRecipeCreatorTest
            .interpretRecipeResponse("Cereal\nPut milk into cereal")
            .getDescription(),
        new Recipe("Cereal", "Put milk into cereal").getDescription());
    /* when there is extra line at the end */
    assertEquals(
        newRecipeCreatorTest
            .interpretRecipeResponse("Tea\nPut teabag into hot water\n")
            .getDescription(),
        new Recipe("Tea", "Put teabag into hot water").getDescription());
    /*single line comment*/
    assertEquals(
        newRecipeCreatorTest.interpretRecipeResponse("Tea:Put teabag into hot water"), null);
  }

  @Test
  public void testNewRecipe() {
    /*Mocking and whole class test for New Recipe*/
    voiceToTextTest.setMealType("dinner");
    newRecipeCreatorTest.handleMeal(voiceToTextTest.mealType);
    assertEquals(false, newRecipeCreatorTest.waitingForMeal);
    voiceToTextTest.setIngredients("instant noodles, water");
    recipeCreatorTest.setRecipe("Instant Noodles\nAdd noodles to boiling water for two minutes");
    assertEquals(
        new Recipe("Instant Noodles", "Add noodles to boiling water for two minutes")
            .getDescription(),
        newRecipeCreatorTest.interpretRecipeResponse(recipeCreatorTest.recipe).getDescription());
  }
}
