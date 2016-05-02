package mweirich.mcrenamer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.ParseException;
import com.eclipsesource.json.WriterConfig;

public class Renamer {

	private JsonObject rootJsonObject, authDB;
	private File profile;

	public Renamer() {
		String appdata = System.getenv("APPDATA");
		profile = new File(appdata + "\\.minecraft\\launcher_profiles.json");
	}

	public String load() throws IOException, ParseException {

		FileReader fr = new FileReader(profile);

		rootJsonObject = Json.parse(fr).asObject();
		authDB = rootJsonObject.get("authenticationDatabase").asObject();

		fr.close();

		return getRandomObject().getString("displayName", null);

	}

	public void write(String newName) throws ParseException, IOException {

		JsonObject random = getRandomObject();
		random.set("displayName", newName);
		authDB.set(getObjectName(), random);
		rootJsonObject.set("authenticationDatabase", authDB);

		FileWriter writer = new FileWriter(profile);
		rootJsonObject.writeTo(writer, WriterConfig.PRETTY_PRINT);

		writer.close();
	}

	private String getObjectName() {
		Iterator<Member> i = authDB.iterator();
		return i.next().getName();
	}

	private JsonObject getRandomObject() {

		String randomName = getObjectName();
		JsonObject random = authDB.get(randomName).asObject();

		return random;
	}

}
