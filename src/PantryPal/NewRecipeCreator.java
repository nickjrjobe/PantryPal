package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.event.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/**
 * Uses a state machine approach to pass control between methods 
 * when creating new recipe 
 * */
public class NewRecipeCreator {
  String mealType;
  Boolean waitingForMeal = true;
  VoiceToText voiceToText;
  RecipeCreator recipeCreator;
  public List<String> prompts;
  private Recipe recipe;

  NewRecipeCreator(VoiceToText voiceToText, RecipeCreator recipeCreator) {
    recipe = null;
    this.voiceToText = voiceToText;
    this.recipeCreator = recipeCreator;
    this.prompts = new ArrayList<String>();
    prompts.add("Would you like Breakfast, Lunch, or Dinner?");
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public void start() {
    voiceToText.startRecording();
  }

  public void stop() {
    voiceToText.stopRecording();
    String response = voiceToText.getTranscript();
    if (response == null) {
      return;
    }
    if (waitingForMeal) {
      handleMeal(response);
    } else {
      handleIngredients(response);
    }
  }

  /**
   *  Checks whether meal type is valid before changing state to accept
   *  ingredients or reasking for meal type
   */
  public void handleMeal(String response) {
    response = response.toLowerCase();
    waitingForMeal = true;
    if (response.contains("breakfast")) {
      mealType = "breakfast";
    } else if (response.contains("lunch")) {
      mealType = "lunch";
    } else if (response.contains("dinner")) {
      mealType = "dinner";
    } else {
      return;
    }
    // Transition to waiting for ingredients state
    prompts.add(mealType);
    prompts.add("What ingredients do you have");
    waitingForMeal = false;
  }

  /*
   * Divides recipes into title and body given a multi
   * line recipe
   * Drops extra new lines at end!
   * */
  public Recipe interpretRecipeResponse(String response) {
    String[] responseLines = response.split("\n");
    String instructions = "";
    int titleLineIndex = 0;
    /* if their is no content, return null */
    if (responseLines.length == 0) {
      return null;
    }

    /* title is first nonempty line */
    for (; titleLineIndex < responseLines.length; titleLineIndex++) {
      if (responseLines[titleLineIndex] != "") {
        break;
      }
    }
    /* if their is no description, return null */
    if (titleLineIndex == responseLines.length - 1) {
      return null;
    }
    /* any further lines are description */
    for (int i = titleLineIndex + 1; i < responseLines.length; i++) {
      instructions += responseLines[i] + "\n";
    }
    return new Recipe(responseLines[titleLineIndex].trim(), instructions.trim());
  }

  /*Hands the ingredients to chat gpt and creates a recipe object using text response from chatgpt*/
  public void handleIngredients(String response) {
    String recipeResponse = recipeCreator.makeRecipe(mealType, response);
    recipe = interpretRecipeResponse(recipeResponse);
  }
}
