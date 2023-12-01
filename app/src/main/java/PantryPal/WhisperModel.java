/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import utils.Account;
import utils.VoiceToText;
import utils.WhisperBot;

/** Communication model for making API requests to get Recipe List. */
public class WhisperModel implements VoiceToText {
  HttpModel httpModel;
  Account account;
  WhisperBot whisperBot;

  WhisperModel(HttpModel httpModel, Account account) {
    this.whisperBot = new WhisperBot();
    this.httpModel = httpModel;
    // httpModel.setPath("whisper/" + account.getUsername() + "/");
    httpModel.setPath("whisper/" + account.getUsername() + "/");
  }

  public String getTranscript() {
    InputStream in = whisperBot.getClientInStream();
    String response = httpModel.performRawRequest("PUT", in);
    System.err.println("Whisper Model responded: " + response);
    return response;
  }

  public void startRecording() {
    whisperBot.startRecording();
  }

  public void stopRecording() {
    whisperBot.stopRecording();
  }
}
