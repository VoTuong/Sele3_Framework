package org.tvd.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

	public static List<String> toStringList(String jsonPath) {
		LOGGER.debug("Load json from {}", jsonPath);
		JsonReader reader = getJsonReader(jsonPath);
		Gson gson = new Gson();
		Type type = new TypeToken<List<String>>() {
		}.getType();
		return gson.fromJson(reader, type);
	}

	private static JsonReader getJsonReader(String jsonPath) {
		try {
			JsonReader reader;
			reader = new JsonReader(new FileReader(jsonPath));
			return reader;
		} catch (FileNotFoundException e) {
			LOGGER.error("Cannot find file: {}", jsonPath, e);
		}
		throw new RuntimeException("Cannot read json file from " + jsonPath);
	}

	public static JsonObject getJsonObjects(String filePath) {

		JsonObject jsonObject;
		Gson gson = new Gson();
		JsonReader jsonReader = getJsonReader(filePath);
		jsonObject = gson.fromJson(jsonReader, JsonObject.class);

		return jsonObject;
	}

	public static <T> T convertJsonToObjects(JsonObject jsonObject, Class<T> tClass) {
		return new Gson().fromJson(jsonObject, tClass);
	}

}
