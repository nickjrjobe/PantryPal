/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import utils.Account;
import utils.AudioRecorder;
import utils.VoiceToText;
import utils.WhisperUtils;

/** Communication model for making API requests to get Recipe List. */
public class WhisperModel implements VoiceToText {
  HttpModel httpModel;
  Account account;
  AudioRecorder audioRecorder;

  WhisperModel(HttpModel httpModel, Account account) {
    this.audioRecorder = new AudioRecorder();
    this.httpModel = httpModel;
    // httpModel.setPath("whisper/" + account.getUsername() + "/");
    httpModel.setPath("whisper/" + account.getUsername() + "/");
  }

  public String getTranscript() {
    InputStream in = WhisperUtils.getClientInStream();
    String response = httpModel.performRawRequest("PUT", in);
    System.err.println("Whisper Model responded: " + response);
    return response;
  }

  public void startRecording() {
    audioRecorder.start();
  }

  public void stopRecording() {
    audioRecorder.stop();
  }
}
