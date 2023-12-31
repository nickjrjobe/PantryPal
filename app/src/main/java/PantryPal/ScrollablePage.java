/* Code initially adapted from Lab1 */

package PantryPal;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/** Generic footer UI element to be used on different UI pages. Supports buttons */
class Footer extends HBox {
  private static final String buttonStyle =
      "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11"
          + " arial;";

  private void format() {
    this.setSpacing(5); // sets spacing between tasks
    this.setPrefSize(500, 560);
    this.setStyle("-fx-background-color: #F0F8FF;");
  }

  /**
   * add a button to footer
   *
   * @param buttontext Text which button should display
   * @param callback event handler to call when button is pressed
   */
  public void addButton(String buttontext, EventHandler<ActionEvent> callback) {
    Button button = new Button(buttontext);
    button.setStyle(buttonStyle);
    this.getChildren().add(button);
    button.setOnAction(callback);
  }

  /**
   * Add a choice selection button to footer
   *
   * @param options List of options to display in the drop down
   * @param callback event handler to call when button is pressed
   */
  public void addDropDown(List<String> options, EventHandler<ActionEvent> callback) {
    ChoiceBox<String> dropDown = new ChoiceBox<>();
    for (String option : options) {
      dropDown.getItems().add(option);
    }
    dropDown.setOnAction(callback);
    this.getChildren().add(dropDown);
  }

  /**
   * Add a choice selection button to footer with default
   *
   * @param options List of options to display in the drop down
   * @param def default option to display
   * @param callback event handler to call when button is pressed
   */
  public void addDropDown(List<String> options, String def, EventHandler<ActionEvent> callback) {
    ChoiceBox<String> dropDown = new ChoiceBox<>();
    for (String option : options) {
      dropDown.getItems().add(option);
    }
    dropDown.setValue(def);
    dropDown.setOnAction(callback);
    this.getChildren().add(dropDown);
  }

  // Overloaded method that takes a pre-configured button
  public void addButton(Button button) {
    this.getChildren().add(button); // Assuming 'this' is a type of Pane
  }

  Footer() {
    format();
    this.setAlignment(Pos.CENTER); // aligning the buttons to center
  }

  public void deleteButton(String buttontext) {
    int i = 0;
    for (Node buttonNode : this.getChildren()) {
      Button button = (Button) buttonNode;
      if (button.getText() == buttontext) {
        this.getChildren().remove(i);
        return;
      }
      i++;
    }
  }
}

/** Generic header UI element which includes title */
class Header extends HBox {
  private static final String buttonStyle =
      "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11"
          + " arial;";
  private Text titleText;

  private void format() {
    this.setPrefSize(500, 60); // Size of the header
    this.setStyle("-fx-background-color: #F0F8FF;");
    this.setPadding(new Insets(10)); // Add padding around
  }

  Header(String title) {
    format();
    titleText = new Text(title);
    titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

    // Set alignment to center for the HBox
    this.setAlignment(Pos.CENTER);

    // Create spacers
    Region leftSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);

    Region rightSpacer = new Region();
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    // Add the spacers and titleText to the HBox
    this.getChildren().addAll(leftSpacer, titleText, rightSpacer);
  }
}

/**
 * Abstract class which provides features to create a page which can scroll its center element, and
 * has a header and footer.
 */
abstract class ScrollablePage extends BorderPane {
  protected Header header;
  protected Footer footer;
  protected Node center;
  private Scene wrapperScene;

  ScrollablePage(String title, Node center) {
    // Initialise the header Object
    header = new Header(title);
    // Initialise the Footer Object
    footer = new Footer();

    this.center = center;

    // Make center scrollable
    ScrollPane sp = new ScrollPane(center);
    sp.setPrefWidth(400);
    sp.setPrefHeight(400);
    sp.setMinSize(ScrollPane.USE_PREF_SIZE, ScrollPane.USE_PREF_SIZE);
    sp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    sp.setFitToWidth(true);
    sp.setFitToHeight(true);

    // Add header to the top of the BorderPane
    this.setTop(header);
    // Add scroller to the centre of the BorderPane
    this.setCenter(sp);
    // Add footer to the bottom of the BorderPane
    this.setBottom(footer);
    // Create a wrapper Scene to ensure correct sizing of the page
    this.wrapperScene = new Scene(this, 600, 800);
  }

  /** returns a JavaFX Scene which contains this page */
  public Scene getWrapperScene() {
    return this.wrapperScene;
  }
}
