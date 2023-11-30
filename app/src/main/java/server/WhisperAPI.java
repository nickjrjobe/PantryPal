package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import utils.WhisperBot;

class WhisperAPI implements HttpHandler {
  WhisperBot whisperBot;

  WhisperAPI() {
    whisperBot = new WhisperBot();
  }

  public String readQuery(URI uri, String base) {
    if (base.length() == uri.toString().length()) {
      /*handle no query case */
      return "";
    } else if (uri.toString().substring(base.length(), base.length() + 1).equals("/")) {
      /* handle subpage case */
      return uri.toString().substring(base.length() + 1);
    } else {
      /* handle regular query case */
      return uri.getQuery();
    }
  }

  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    try {
      URI uri = httpExchange.getRequestURI();
      String base = httpExchange.getHttpContext().getPath();
      String query = readQuery(uri, base);
      System.err.println("QUERY WAS " + query);
      if (method.equals("PUT")) {
        response = whisperBot.getTranscript(httpExchange.getRequestBody());
      } else {
        throw new Exception("Not Valid Request Method");
      }
    } catch (Exception e) {
      System.out.println("An erroneous request error: " + e.getMessage());
      response = "400 Bad Request";
      e.printStackTrace();
    }
    // Sending back response to the client
    sendResponse(httpExchange, response);
  }

  void sendResponse(HttpExchange httpExchange, String response) throws IOException {
    httpExchange.sendResponseHeaders(200, response.length());
    OutputStream outStream = httpExchange.getResponseBody();
    outStream.write(response.getBytes());
    outStream.close();
  }

  String getRequestString(HttpExchange httpExchange) throws IOException {
    InputStream inStream = httpExchange.getRequestBody();
    Scanner scanner = new Scanner(inStream);
    /* Request body optional, so simply return null if not given */
    String postData;
    try {
      postData = scanner.nextLine();
    } catch (Exception e) {
      System.out.println("request was empty");
      scanner.close();
      return null;
    }
    System.out.println("request: " + postData);
    scanner.close();
    return postData;
  }
}
