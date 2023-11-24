package server;

import org.json.JSONObject;
import utils.Account;
/** Interface of object which stores data for Accounts */
interface AccountData {
  public JSONObject toJSON();

  public Account put(String key, Account value);

  public Account get(String key);

  public Account remove(String key);
}

class AccountDB implements AccountData {
  JSONDB db;

  AccountDB(JSONDB db) {
    this.db = db;
  }

  public Account put(String key, Account value) {
    db.create(value.toJSON());
    return get(key);
  }

  public Account get(String key) {
    JSONObject j = db.read(key);
    if (j == null) {
      return null;
    } else {
      return new Account(j);
    }
  }

  public Account remove(String key) {
    JSONObject j = db.remove(key);
    if (j == null) {
      return null;
    } else {
      return new Account(j);
    }
  }

  public JSONObject toJSON() {
    return db.toJSON();
  }
}
