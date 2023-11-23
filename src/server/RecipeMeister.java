package lab6;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.not;
import static com.mongodb.client.model.Updates.*;

import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
interface JSONDatabase {
  void delete(JSONObject json);
  void create(JSONObject json);
  JSONObject read(JSONObject json);
  void update(JSONObject json);
}

class JsonDB {
  String uri =
      "mongodb+srv://rkosta:fbd4cDHOPsDB5Svm@cluster0.3nqvbn7.mongodb.net/?retryWrites=true&w=majority";
  String lookupkey = "rname";
  MongoClient mongoClient;
  MongoDatabase database;
  MongoCollection<Document> recipeCollection;
  Database(String collection, String lookupKey) {
    self.lookupkey = lookupKey;
    this.mongoClient = MongoClients.create(uri);
    this.database = mongoClient.getDatabase("mongo_db");
    this.recipeCollection = database.getCollection(collection);
  }
  void delete(JSONObject json) {
    delete(json.getString(lookupkey));
  }
  void delete(String key) {
    Bson filter = eq(lookupkey, key);
    Document doc = recipeCollection.findOneAndDelete(filter);
  }
  void create(JSONObject json) {
    Document d = Document.parse(json.toString());
    recipeCollection.insertOne(d);
  }
  void update(JSONObject json) {
    self.delete(json);
    self.create(json);
  }
  JSONObject read(JSONObject json) {
    read(json.getString(lookupkey));
  }
  JSONObject read(String key) {
    // TODO this will throw exception if doesnt exist
    Document lookup = recipeCollection.find(eq(lookupkey, name)).first();
    return lookup.toJSON();
  }
  void clear() {
    Bson filter = not(eq(lookupkey, "kjanfo;ifijo;ijqwpqwejpqwejqwipeqjweqw"));
    recipeCollection.deleteMany(filter);
  }
}
