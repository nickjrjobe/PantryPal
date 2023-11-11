package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.event.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/* Uses a state machine approach to pass control between methods when creating new recipe */
class NewRecipeCreator {
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

  /* Checks whether meal type is valid before changing state to accept ingredients or reasking for meal type */
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
    if (responseLines.length == 0) return null;

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

class NewRecipeUI extends VBox {
  NewRecipeCreator newRecipeCreator;

  private void format() {
    this.setSpacing(20); // sets spacing between tasks
    this.setPrefSize(500, 560);
    this.setStyle("-fx-background-color: #F0F8FF;");
  }

  Recipe getRecipe() {
    return newRecipeCreator.getRecipe();
  }

  void addPrompt(String text) {
    Label label = new Label(text);
    label.setPrefSize(500, 20); // sets size of task
    label.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
    this.getChildren().add(label);
  }

  NewRecipeUI() {
    this.newRecipeCreator = new NewRecipeCreator(new WhisperBot(), new ChatGPTBot());
    getNewPrompts();
  }

  void start() {
    newRecipeCreator.start();
  }

  void stop() {
    newRecipeCreator.stop();
    getNewPrompts();
  }

  void getNewPrompts() {
    Recipe recipe = newRecipeCreator.getRecipe();
    this.getChildren().clear();
    for (int i = 0; i < newRecipeCreator.prompts.size(); i++) {
      addPrompt(newRecipeCreator.prompts.get(i));
    }
  }
}

class NewRecipePage extends ScrollablePage {
  private NewRecipeUI newRecipeUI;

  private void addStopButton() {
    footer.addButton(
        "Stop Recording",
        e -> {
          newRecipeUI.stop();
          footer.deleteButton("Stop Recording");
          addStartButton();
        });
  }

  private void addStartButton() {
    footer.addButton(
        "Start Recording",
        e -> {
          newRecipeUI.start();
          footer.deleteButton("Start Recording");
          addStopButton();
        });
  }

  NewRecipePage(NewRecipeUI newRecipeUI) {
    super("New Recipe", newRecipeUI);
    this.newRecipeUI = newRecipeUI;
    addStartButton();
  }
}
