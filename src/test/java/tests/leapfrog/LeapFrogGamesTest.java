package tests.leapfrog;

import org.jsoup.nodes.Document;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tvd.projects.leapfrog.model.GameData;
import org.tvd.projects.leapfrog.utils.GameDataReader;
import org.tvd.projects.leapfrog.utils.GameVerifier;
import org.tvd.utilities.HtmlDownloader;
import org.tvd.utilities.HtmlParser;

import java.io.IOException;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LeapFrogGamesTest {
	// URL to download the HTML from
	private static final String URL = "https://store.leapfrog.com/en-us/apps/c?p=1&platforms=197&product_list_dir=asc&product_list_order=name";
	// Local file path to save the downloaded HTML
	private static final String HTML_FILE_PATH = "downloaded_page.html";
	// Excel file path containing the expected game data
	private static final String EXCEL_FILE_PATH = "src/test/resources/data/Content Testing_LeapFrog-games.xlsx";
	// Sheet index to read from the Excel file (0-based index)
	private static final int SHEET_INDEX = 1;

	@BeforeClass
	public void setUp() {
		// Open the specified URL
		open(URL);
	}

	@AfterClass
	public void tearDown() {
		// Close the WebDriver after tests complete
		closeWebDriver();
	}

	@BeforeMethod
	public void closePopups() {
		// Close popup if it is displayed
		if ($(".geo-ip-mismatch-warning-popup__content button").isDisplayed()) {
			$(".geo-ip-mismatch-warning-popup__content button").shouldBe(visible).click();
		}
	}

	@Test
	public void downloadHtmlAndVerifyContent() throws IOException {
		// Download the HTML and save to a local file
		HtmlDownloader.downloadHtml(URL, HTML_FILE_PATH);
		// Parse the downloaded HTML file
		Document doc = HtmlParser.parseHtml(HTML_FILE_PATH);
		// Read the expected game data from the Excel file
		List<GameData> gameDataList = GameDataReader.readGameData(EXCEL_FILE_PATH, SHEET_INDEX);
		// Assert that all games in the HTML match the expected data (all logic is encapsulated in the utility)
		GameVerifier.assertAllGames(doc, gameDataList);
	}
}

