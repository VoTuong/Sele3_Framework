package org.tvd.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class HtmlParser {

	/**
	 * Parses an HTML file from the specified file path and returns a Jsoup Document.
	 *
	 * @param filePath the path of the HTML file.
	 * @return the parsed Document.
	 * @throws IOException if an error occurs while reading the file.
	 */
	public static Document parseHtml(String filePath) throws IOException {
		File inputFile = new File(filePath);
		return Jsoup.parse(inputFile, "UTF-8");
	}
}
