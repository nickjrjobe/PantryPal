package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;

class HttpAPI implements HttpHandler {
  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    try {
      URI uri = httpExchange.getRequestURI();
      String query = uri.getRawQuery();
      String request = getRequestString(httpExchange);
      if (method.equals("GET")) {
        response = handleGet(query, request);
      } else if (method.equals("POST")) {
        response = handlePost(query, request);
      } else if (method.equals("DELETE")) {
        response = handleDelete(query, request);
      } else if (method.equals("PUT")) {
        response = handlePut(query, request);
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
    String postData = scanner.nextLine();
    System.out.println("request: " + postData);
    scanner.close();
    return postData;
  }

  JSONObject getJSONRequest(String request) throws IOException {
    try {
      return new JSONObject(request);
    } catch (Exception e) {
      throw new IOException("Response was not JSON");
    }
  }

  String handleGet(String query, String request) throws IOException {
    throw new IOException("Request type not supported");
  }

  String handlePost(String query, String request) throws IOException {
    throw new IOException("Request type not supported");
  }

  String handlePut(String query, String request) throws IOException {
    throw new IOException("Request type not supported");
  }

  String handleDelete(String query, String request) throws IOException {
    throw new IOException("Request type not supported");
  }
}
