package PantryPal;

import java.io.IOException;
import org.json.JSONObject;
import utils.Account;
import utils.Recipe;

class NewRecipeModel {
  HttpModel httpModel;

  NewRecipeModel(HttpModel httpModel, Account account) {
    this.httpModel = httpModel;
    httpModel.setPath("newrecipe/" + account.getUsername() + "/");
  }

  TranscriptResults sendTranscript(String response) throws IOException {
    JSONObject responseJson = new JSONObject();
    responseJson.put("response", response);

    String transcript = httpModel.performRequest("POST", null, responseJson.toString());
    try {
      return new TranscriptResults(new JSONObject(transcript));
    } catch (Exception e) {
      System.err.println(
          "(Trying again) New recipe response from server malformed, error " + e.getMessage());
      return getInitialTranscript();
    }
  }

  TranscriptResults getInitialTranscript() throws IOException {

    String transcript = httpModel.performRequest("GET", "prompts", null);
    try {
      return new TranscriptResults(new JSONObject(transcript));
    } catch (Exception e) {
      throw new IOException("New recipe response from server malformed, error " + e.getMessage());
    }
  }

  /** resets state of remote New recipe creator */
  void reset() {
    httpModel.performRequest("DELETE", "", null);
  }

  Recipe regenerate() throws IOException {
    try {
      JSONObject jObject = new JSONObject(httpModel.performRequest("GET", "regenerate", null));
      return new Recipe(jObject.getJSONObject("recipe"));
    } catch (Exception e) {
      throw new IOException("New recipe response from server malformed, error " + e.getMessage());
    }
  }
}
