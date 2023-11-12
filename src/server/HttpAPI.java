package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

abstract class HttpAPI implements HttpHandler {

  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
    try {
      if (method.equals("GET")) {
        response = handleGet(httpExchange);
      } else if (method.equals("POST")) {
        response = handlePost(httpExchange);
      } else if (method.equals("DELETE")) {
        response = handleDelete(httpExchange);
      } else if (method.equals("PUT")) {
        response = handlePut(httpExchange);
      } else {
        throw new Exception("Not Valid Request Method");
      }
    } catch (Exception e) {
      System.out.println("An erroneous request error: " + e.getMessage());
      response = e.toString();
      e.printStackTrace();
    }
    // Sending back response to the client
    httpExchange.sendResponseHeaders(200, response.length());
    OutputStream outStream = httpExchange.getResponseBody();
    outStream.write(response.getBytes());
    outStream.close();
  }

  String handleGet(HttpExchange httpExchange) throws IOException {
    throw new IOException("Request type not supported");
  }

  String handlePost(HttpExchange httpExchange) throws IOException {
    throw new IOException("Request type not supported");
  }

  String handlePut(HttpExchange httpExchange) throws IOException {
    throw new IOException("Request type not supported");
  }

  String handleDelete(HttpExchange httpExchange) throws IOException {
    throw new IOException("Request type not supported");
  }
}
