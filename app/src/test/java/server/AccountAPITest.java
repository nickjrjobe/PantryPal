package server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import org.junit.jupiter.api.Test;
import utils.Account;

public class AccountAPITest {
  MockAccountData data = new MockAccountData();
  AccountAPI dut = new AccountAPI(data);
  String exceptionMessage = "Request type not supported";
  Account john = new Account("john", "password");
  Account john2 = new Account("john", "password2");

  @Test
  public void testDelete() {
    try {
      data.data.clear();
      data.data.put("john", john);
      /* test delete of nonexistant user */
      assertEquals("404 Not Found", dut.handleDelete("danny", ""));
      assertEquals(1, data.data.size());
      assertEquals(data.data.get("john"), john);
      /* test delete of existant user */
      assertEquals("200 OK", dut.handleDelete("john", ""));
      assertEquals(0, data.data.size());
    } catch (Exception e) {
      fail("should not have thrown exception");
    }
  }

  @Test
  public void testPost() {
    try {
      data.data.clear();
      /* test with nonexisting user */
      assertEquals(dut.handlePost("", john.toJSON().toString()), "200 OK");
      /* ensure john properly added */
      assertEquals(true, data.data.containsKey("john"));
      assertEquals("password", data.data.get("john").getPassword());
      assertEquals("john", data.data.get("john").getUsername());
      /* test with existing user */
      assertEquals(dut.handlePost("", john2.toJSON().toString()), "406 Not Acceptable");
      /* ensure user object not modified */
      assertEquals("password", data.data.get("john").getPassword());
    } catch (Exception e) {
      fail("should not have thrown exception");
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
