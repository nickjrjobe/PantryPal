package server;

import static org.junit.Assert.*;

import java.io.*;
import org.junit.Test;

public class HttpAPITest {
  HttpAPI dut = new HttpAPI();
  public static final String exceptionMessage = "Request type not supported";

  @Test
  public void testPut() {
    boolean exceptionHappened = false;
    try {
      dut.handlePut("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }

  @Test
  public void testDelete() {
    boolean exceptionHappened = false;
    try {
      dut.handleDelete("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }

  @Test
  public void testPost() {
    boolean exceptionHappened = false;
    try {
      dut.handlePost("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }

  @Test
  public void testGet() {
    boolean exceptionHappened = false;
    try {
      dut.handlePost("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }

  @Test
  public void testGetJSONRequest() {
    String invalidJson = "this/is-definitely.not;json";
    String validJson = "{\"this\":\"is json\"}";

    /* test invalid json string */
    boolean exceptionHappened = false;
    try {
      dut.getJSONRequest(invalidJson);
    } catch (Exception e) {
      assertEquals("Response was not JSON", e.getMessage());
      exceptionHappened = true;
    }
    assertEquals(true, exceptionHappened);

    /* test valid json string */
    exceptionHappened = false;
    try {
      dut.getJSONRequest(validJson);
    } catch (Exception e) {
      assertEquals("Response was not JSON", e.getMessage());
      exceptionHappened = true;
    }
  }
}
