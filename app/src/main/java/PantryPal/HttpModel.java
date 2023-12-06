/* Code initially adapted from Lab1 */

package PantryPal;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.*;
import javafx.event.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import utils.ConfigReader;

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
  private String ip;
  private String urlString;
  private List<ServerObserver> observers = new ArrayList<>();

  HttpRequestModel() {
    setPath(""); // default path, should avoid using
    ConfigReader configReader = new ConfigReader();
    ip = configReader.getRemoteServerIP();
  }

  public void setPath(String path) {
    // ip = "132.239.10.221";
    System.err.println("ip is " + ip);
    this.urlString = "http://" + ip + ":" + port + "/" + path;
  }
  public String getURL() {
    return "http://" + ip + ":" + port + "/";
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
    System.err.println("Tried to tryconnect");
    try {
      Socket s = new Socket(this.ip, Integer.parseInt(port));
      return true;
    } catch (Exception e) {
      System.err.println("failed to tryconnect");
      notifyServerStatus(false);
      return false;
    }
  }
    /*
    public boolean tryConnect() {
      try {
        URL url = new URL(getURL() + "tryconnect");
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
          // Other errors with server available
          notifyServerStatus(false);
          return false;
        }
      } catch (IOException ex) {
        // only called when refresh error page, so just handle connection refused error
        notifyServerStatus(false);
        return false;
      }
    }
    */

    public String performRawRequest(String method, InputStream in) {
      if (!tryConnect()) {
        notifyServerStatus(false);
      } // Implement your HTTP request logic here and return the response
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

        BufferedReader fromServer =
            new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = fromServer.readLine();
        fromServer.close();
        System.out.println("performRawRequest Response :" + response);
        if (response.contains("Connection refused")) {
          notifyServerStatus(false);
        }
        return response;
      } catch (Exception ex) {
        ex.printStackTrace();
        return "Error: " + ex.getMessage();
      }
    }

    /**
     * Perform an HTTP request
     *
     * @param method the HTTP method to use
     * @param query the query string to use
     * @param request the request body to use
     * @return the response from the server
     */
    public String performRequest(String method, String query, String request) {
      if (!tryConnect()) {
        notifyServerStatus(false);
      }
      // Implement your HTTP request logic here and return the response
      if (request != null) {
        System.out.println("Request :" + request);
      }
      try {
        String requestUrlString = urlString;
        if (query != null) {
          requestUrlString += "?" + query;
        }
        URL url = new URI(requestUrlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);

        if (request != null && (method.equals("POST") || method.equals("PUT"))) {
          OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
          out.write(request);
          out.flush();
          out.close();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();
        in.close();
        System.out.println("Response :" + response);
        if (response.contains("Connection refused")) {
          notifyServerStatus(false);
        }
        return response;
      } catch (Exception ex) {
        if (ex.getMessage().contains("Connection refused")) {
          notifyServerStatus(false);
        }
        ex.printStackTrace();
        return "Error: " + ex.getMessage();
      }
    }
  }
