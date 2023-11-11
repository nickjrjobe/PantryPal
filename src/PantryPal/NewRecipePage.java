package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.event.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;

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

/**
 * Page wrapper for NewRecipeUI
 */
public class NewRecipePage extends ScrollablePage {
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
