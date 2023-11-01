package PantryPal;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
class FileReadBehavior implements ReadBehavior {
	private String filename;
	public FileReadBehavior(String filename) {
		this.filename = filename;
	}
	public List<Recipe> read() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		FileReader f;
		BufferedReader reader;
		try {
			f = new FileReader(this.filename);
			reader = new BufferedReader(f);
			for (String line1 = reader.readLine(), line2 = reader.readLine();
			     line1 != null && line2 != null;
			     line1 = reader.readLine(), line2 = reader.readLine()) {
				recipes.add(new Recipe(line1, line2));
			}
		} catch (IOException E) {
			return null;
		}
		try {
			reader.close();
			f.close();
		} catch (IOException E) {
		}
		return recipes;
	}
}
