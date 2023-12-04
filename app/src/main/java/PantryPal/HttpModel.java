/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public interface HttpModel {
  void setPath(String path);


  
  // Methods for Observer pattern
  void registerObserver(ServerObserver observer);

  void removeObserver(ServerObserver observer);

  void notifyServerStatus(boolean connected);

  boolean tryConnect();
  
  public String performRequest(String method, String query, String request);

  public String performRawRequest(String method, InputStream in);

}

class HttpRequestModel implements HttpModel {
  private static final String port = "8100";
  private static final String ip = "localhost";
  private String urlString;
  private List<ServerObserver> observers = new ArrayList<>();

  HttpRequestModel() {
    setPath(""); // default path, should avoid using
  }

  public void setPath(String path) {
    this.urlString = "http://" + ip + ":" + port + "/" + path;
  }

  @Override
  public void registerObserver(ServerObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(ServerObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyServerStatus(boolean connected) {
    for (ServerObserver observer : observers) {
      observer.updateServer(connected);
    }
  }

  public boolean tryConnect() {
    try {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.connect(); // Open a connection to the server

      int responseCode = conn.getResponseCode();
      // Check if the response code indicates a successful connection
      if (responseCode == HttpURLConnection.HTTP_OK
          || responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
        // The server is reachable, even if the specific page/resource is not found
        notifyServerStatus(true);
        return true;
      } else {
        // Server returned an error response code
        return false;
      }
    } catch (IOException ex) {
      // An IOException is thrown if there is a network error or the server is unreachable
      System.err.println("Error: " + ex.getMessage());
      notifyServerStatus(false);
      return false;

  public String performRawRequest(String method, InputStream in) {
    // Implement your HTTP request logic here and return the response
    try {
      if (!method.equals("POST") && !method.equals("PUT")) {
        throw new IllegalArgumentException("must call with post or put");
      }
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod(method);
      conn.setDoOutput(true);
      OutputStream out = conn.getOutputStream();

      in.transferTo(out);
      out.flush();
      out.close();

      BufferedReader fromServer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response = fromServer.readLine();
      fromServer.close();
      System.out.println("Response :" + response);
      return response;
    } catch (Exception ex) {
      ex.printStackTrace();
      return "Error: " + ex.getMessage();

    }
  }

  public String performRequest(String method, String query, String request) {
    // Implement your HTTP request logic here and return the response
    if (request != null) {
      System.out.println("Request :" + request);
    }
    try {
      if (query != null) {
        urlString += "?" + query;
      }
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod(method);
      conn.setDoOutput(true);

      if (method.equals("POST") || method.equals("PUT")) {
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(request);
        out.flush();
        out.close();
      }

      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String response = in.readLine();
      in.close();
      System.out.println("Response :" + response);
      return response;
    } catch (Exception ex) {
      tryConnect();
      // ex.printStackTrace();
      return "Error: " + ex.getMessage();
    }
  }
}
