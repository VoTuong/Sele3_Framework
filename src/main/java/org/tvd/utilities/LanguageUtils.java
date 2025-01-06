package org.tvd.utilities;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;


public class LanguageUtils {

	private static final LanguageDetector languageDetector = LanguageDetectorBuilder
			.fromLanguages(Language.ENGLISH, Language.VIETNAMESE, Language.THAI).build();

	public static String detectLanguage(String text) {
		if (text == null || text.isEmpty()) {
			return "unknown";
		}

		Language detectedLanguage = languageDetector.detectLanguageOf(text);
		return detectedLanguage.getIsoCode639_1().toString(); // Trả về mã ISO 639-1 (en, vi)
	}

}
