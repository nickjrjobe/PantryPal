package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import utils.WhisperBot;

class WhisperSubject extends Observable {
  @Override
  public void addObserver(Observer o) {
    System.err.println("adding observer");
    super.addObserver(o);
  }
  void set(String data) {
    System.err.println("notifying observers");
    setChanged();
    notifyObservers(data);
  }
}

class WhisperAPIFactory implements HttpUserAPIFactory {
  Map<String, WhisperSubject> perUserWhisperSubject;

  WhisperAPIFactory(Map<String, WhisperSubject> perUserWhisperSubject) {
    this.perUserWhisperSubject = perUserWhisperSubject;
  }

  public RawHttpAPI makeAPI(String username) {
    WhisperSubject whisperSubject = perUserWhisperSubject.get(username);
    if (whisperSubject == null) {
      whisperSubject = new WhisperSubject();
      perUserWhisperSubject.put(username, whisperSubject);
    }
    System.err.println("WhisperFactory subject: " + whisperSubject.toString());
    return new WhisperAPI(whisperSubject);
  }
}

class WhisperAPI extends RawHttpAPI {
  WhisperBot whisperBot;
  WhisperSubject whisperSubject;

  WhisperAPI(WhisperSubject whisperSubject) {
    whisperBot = new WhisperBot();
    this.whisperSubject = whisperSubject;
  }

  String handlePut(String query, HttpExchange httpExchange) throws IOException {
    String transcript = whisperBot.getTranscript(httpExchange.getRequestBody());
    whisperSubject.set(transcript);
    return transcript;
  }
}
