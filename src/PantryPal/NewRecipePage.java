package PantryPal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.event.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.json.JSONArray;
import org.json.JSONObject;

class TranscriptResults {
  public final Recipe recipe;
  public final List<String> prompts;

  TranscriptResults(JSONObject json) throws IllegalArgumentException {
    try {

      /* read prompts from jsonArray */
      this.prompts = new ArrayList<String>();
      JSONArray jsonArray = json.getJSONArray("transcript");
      for (int i = 0; i < jsonArray.length(); i++) {
        this.prompts.add(jsonArray.getString(i));
      }
      if (json.has("recipe")) {
        JSONObject recipeJson = json.getJSONObject("recipe");

        this.recipe = new Recipe(recipeJson);
      } else {
        this.recipe = null;
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Input json was not valid (Error: " + e.getMessage() + ")");
    }
  }

  TranscriptResults() {
    this.recipe = null;
    this.prompts = new ArrayList<String>();
  }
}

class NewRecipeModel extends AbstractModel {
  NewRecipeModel() {
    super("newrecipe");
  }

  TranscriptResults sendTranscript(String response) throws IOException {
    JSONObject responseJson = new JSONObject();
    responseJson.put("response", response);

    String transcript = super.performRequest("POST", null, responseJson.toString());
    try {
      return new TranscriptResults(new JSONObject(transcript));
    } catch (Exception e) {
      throw new IOException("New recipe response from server malformed, error " + e.getMessage());
    }
  }

  void reset() {
    super.performRequest("DELETE", "", null);
  }
}

class NewRecipeController {
  NewRecipeUI newRecipeUI;
  NewRecipePage newRecipePage;
  NewRecipeModel newRecipeModel;
  PageTracker pt;
  VoiceToText voiceToText;

  NewRecipeController(
      NewRecipeUI newRecipeUI,
      NewRecipePage newRecipePage,
      NewRecipeModel newRecipeModel,
      PageTracker pt,
      VoiceToText voicetotext) {
    this.newRecipeUI = newRecipeUI;
    this.newRecipePage = newRecipePage;
    this.newRecipeModel = newRecipeModel;
    this.pt = pt;
    this.voiceToText = voicetotext;
    newRecipePage.footer.addButton(
        "start",
        e -> {
          this.start();
        });
    newRecipePage.footer.addButton(
        "home",
        e -> {
          this.exit();
        });
  }

  ScrollablePage getPage() {
    return this.newRecipePage;
  }

  void start() {
    /* set correct button layout for this state */
    newRecipePage.footer.addButton(
        "stop",
        e -> {
          this.stop();
        });
    newRecipePage.footer.deleteButton("start");

    voiceToText.startRecording();
  }

  void stop() {
    /* set correct button layout for this state */
    newRecipePage.footer.deleteButton("stop");

    voiceToText.stopRecording();

    String transcript = voiceToText.getTranscript();
    TranscriptResults results;
    try {
      results = newRecipeModel.sendTranscript(transcript);
    } catch (Exception e) {
      System.err.println("error: " + e.getMessage());
      results = new TranscriptResults();
    }
    newRecipeUI.setPrompts(results.prompts);

    /* finish setting up buttons based on state */
    if (results.recipe != null) {
      Recipe recipe = results.recipe;
      newRecipePage.footer.addButton(
          "view details",
          e -> {
            this.done(recipe);
          });
    } else {
      newRecipePage.footer.addButton(
          "start",
          e -> {
            this.start();
          });
    }
  }

  void done(Recipe recipe) {
    newRecipeModel.reset();
    NewRecipeDetailPage drp = new NewRecipeDetailPage(new RecipeDetailUI(recipe));
    drp.footer.addButton(
        "home",
        e -> {
          pt.goHome();
        });
    pt.swapToPage(drp);
  }

  void exit() {
    newRecipeModel.reset();
    pt.goHome();
  }

  void setup() {
    newRecipePage.footer.addButton(
        "start",
        e -> {
          this.start();
        });
  }
}

class NewRecipeUI extends VBox {
  private void format() {
    this.setSpacing(20); // sets spacing between tasks
    this.setPrefSize(500, 560);
    this.setStyle("-fx-background-color: #F0F8FF;");
  }

  void addPrompt(String text) {
    Label label = new Label(text);
    label.setPrefSize(500, 20); // sets size of task
    label.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
    this.getChildren().add(label);
  }

  NewRecipeUI() {}

  void setPrompts(List<String> prompts) {
    this.getChildren().clear();
    for (String prompt : prompts) {
      addPrompt(prompt);
    }
  }
}

/** Page wrapper for NewRecipeUI */
public class NewRecipePage extends ScrollablePage {
  private NewRecipeUI newRecipeUI;

  NewRecipePage(NewRecipeUI newRecipeUI) {
    super("New Recipe", newRecipeUI);
  }
}
