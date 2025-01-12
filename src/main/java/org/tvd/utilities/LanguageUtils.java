package org.tvd.utilities;

import lombok.SneakyThrows;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;


public class LanguageUtils {

	@SneakyThrows
	public static String detectLanguage(String text) {
		if (text == null || text.isEmpty()) {
			return "unknown";
		}
		LanguageDetector detectedLanguage = LanguageDetector.getDefaultLanguageDetector().loadModels();
		detectedLanguage.addText(text);
		LanguageResult languageResult = detectedLanguage.detect();
		return languageResult.getLanguage();
	}


}
