package server;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import utils.Account;
public class MockAccountData implements AccountData {
  /* publically accessible data for r/w modifications in tests */
  public JSONObject json;
  public Map<String, Account> data;
  public MockAccountData() {
    this.data = new HashMap<>();
  }
  public Account put(String key, Account value) {
    return data.put(key, value);
  }
  public Account get(String key) {
    return data.get(key);
  }
  public Account remove(String key) {
    return data.remove(key);
  }
  public JSONObject toJSON() {
    return json;
  }
}
