package org.tvd.utilities;

import com.codeborne.selenide.WebDriverRunner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.open;

public class HtmlDownloader {

	/**
	 * Opens the given URL, downloads the HTML source,
	 * and saves it to the specified file path.
	 *
	 * @param url      the URL to open.
	 * @param filePath the local file path to save the HTML.
	 * @throws IOException if an error occurs during file writing.
	 */
	public static void downloadHtml(String url, String filePath) throws IOException {
		open(url);
		String pageSource = WebDriverRunner.source();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			assert pageSource != null;
			writer.write(pageSource);
		}
	}
}
