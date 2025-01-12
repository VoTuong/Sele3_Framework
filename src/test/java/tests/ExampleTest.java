package tests;

import org.tvd.utilities.LanguageUtils;

public class ExampleTest {
	public static void main(String[] args) {
		// Văn bản mẫu để phát hiện ngôn ngữ
		String text = "Việt Nam";

		String result = LanguageUtils.detectLanguage(text);

		// In ra ngôn ngữ được phát hiện
		System.out.println("Detected language: " + result);
	}
}
