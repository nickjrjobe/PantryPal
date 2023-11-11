/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.json.JSONObject;

/** UI element which displays list of recipes */
class RecipeListUI extends VBox {
  private PageTracker pageTracker;

  RecipeListUI(PageTracker pt) {
    this.pageTracker = pt;
    format();
    update();
  }

  private void format() {
    this.setSpacing(5); // sets spacing between tasks
    this.setPrefSize(500, 560);
    this.setStyle("-fx-background-color: #F0F8FF;");
  }
  private JSONObject performRequest() {
    try {
      String urlString = "http://localhost:8100/recipes";
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setDoOutput(true);
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response = in.readLine();
      System.out.println("response " + response);
      in.close();
      return new JSONObject(response);
    } catch (Exception ex) {
      System.err.println("HTTP request failed");
      return null;
    }
  }
  public void addDetailedRecipeButton(RecipeEntryUI entry,String title) {
    entry.addButton("details", e -> {
      RecipeController rc = new RecipeController();
      RecipeDetailPage drp = new RecipeDetailPage(new RecipeDetailUI(rc.read(title)));
      drp.footer.addButton("home", eprime -> { pageTracker.goHome(); });
      pageTracker.swapToPage(drp);
    });
  }

  /** Synchronize recipe List UI element with application's internal recipe list */
  public void update() {
    this.getChildren().clear();
    JSONObject titleObject = performRequest();
    if (titleObject == null) {
      return;
    }
    Iterator<String> titles = performRequest().keys();
    while (titles.hasNext()) {
      String title = titles.next();
      RecipeEntryUI entry = new RecipeEntryUI(title);
      addDetailedRecipeButton(entry, title);
      /* TODO add recipe entry UI */
      this.getChildren().add(entry);
    }
  }
}

/** UI Page containing recipe list, and accompanying header and footer */
public class RecipeListPage extends ScrollablePage {
        private RecipeListUI recipeList;

        RecipeListPage(PageTracker pt) {
          super("Recipe List",
              new RecipeListUI(pt));
          this.recipeList = (RecipeListUI) this.center;
          this.footer.addButton("update", e -> { recipeList.update(); });
        }
}
