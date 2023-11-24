package server;

import static org.junit.Assert.*;

import java.io.*;
import org.junit.Test;
import utils.Account;

public class AuthorizationAPITest {
  MockAccountData data = new MockAccountData();
  AuthorizationAPI dut = new AuthorizationAPI(data);
  String exceptionMessage = "Request type not supported";

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
    try {
      data.data.clear();
      Account john = new Account("john", "password");
      Account johnBadPassword = new Account("john", "incorrectpassword");
      /* try non-existant account */
      assertEquals("404 Not Found", dut.handlePost("", john.toJSON().toString()));
      data.data.put("john", john);
      /* try existing account incorrect password */
      assertEquals("401 Unauthorized", dut.handlePost("", johnBadPassword.toJSON().toString()));
      /* try existing account correct password */
      assertEquals("200 OK", dut.handlePost("", john.toJSON().toString()));
    } catch (Exception e) {
      assertEquals("Should not have thrown exception", true, false);
    }
  }
  @Test
  public void testGet() {
    boolean exceptionHappened = false;
    try {
      dut.handleGet("", "");
    } catch (IOException e) {
      exceptionHappened = true;
      assertEquals(exceptionMessage, e.getMessage());
    }
    assertEquals(true, exceptionHappened);
  }
}
