package PantryPal;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import org.json.JSONObject;

class NewRecipeModel {
  HttpModel httpModel;

  NewRecipeModel(HttpModel httpModel) {
    this.httpModel = httpModel;
    httpModel.setPath("newrecipe");
  }

  TranscriptResults sendTranscript(String response) throws IOException {
    JSONObject responseJson = new JSONObject();
    responseJson.put("response", response);

    String transcript = httpModel.performRequest("POST", null, responseJson.toString());
    try {
      return new TranscriptResults(new JSONObject(transcript));
    } catch (Exception e) {
      throw new IOException("New recipe response from server malformed, error " + e.getMessage());
    }
  }

  TranscriptResults getInitialTranscript() throws IOException {

    String transcript = httpModel.performRequest("GET", null, null);
    try {
      return new TranscriptResults(new JSONObject(transcript));
    } catch (Exception e) {
      throw new IOException("New recipe response from server malformed, error " + e.getMessage());
    }
  }

  void reset() {
    httpModel.performRequest("DELETE", "", null);
  }
}
