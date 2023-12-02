package PantryPal;

import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import utils.Account;

public class ServerError {
  /**
   * Shows an error message pops up when the server is unavailable ServerError serverError = new
   * ServerError(); serverError.showError();
   */
  private AuthorizationModel authorizationModel;

  private Account account;

  public ServerError(AuthorizationModel authorizationModel) {
    this.authorizationModel = authorizationModel;
  }

  public void showError() {
    Platform.runLater(
        () -> {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Server Error");
          alert.setHeaderText(null);
          alert.setContentText("Server is temporarily unavailable, please check later");

          // Remove the default buttons
          alert.getButtonTypes().clear();

          // "Refresh" button
          ButtonType refreshButtonType = new ButtonType("Refresh", ButtonBar.ButtonData.OTHER);
          alert.getButtonTypes().add(refreshButtonType);

          Optional<ButtonType> result = alert.showAndWait();

          // Handle the Refresh button action
          if (result.isPresent() && result.get() == refreshButtonType) {
            boolean serverResponse = serverRequest(account);
            if (serverResponse) {
              alert.close(); // Close the alert only if serverResponse is true
            } else {
              // keep the alert open by showing it again if serverResponse is false
              showError();
            }
          }
        });
  }

  public boolean mockFailedServerRequest(Account account) {
    return false;
  }

  public boolean serverRequest(Account account) {
    return authorizationModel.ifConnected(account);
  }
}
