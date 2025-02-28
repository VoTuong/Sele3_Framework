package tests;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.codeborne.selenide.Selenide.*;

public class LeapFrogGamesTest {

		// ExecutorService để xử lý song song nếu cần
		private static final ExecutorService executor = Executors.newFixedThreadPool(5);

		@BeforeClass
		public void setUp() {
			// Cấu hình Selenide nếu cần (ví dụ: browser, timeout,...)
			// Ví dụ: Configuration.browser = "chrome";
		}

		@AfterClass
		public void tearDown() {
			// Đóng WebDriver và giải phóng thread pool
			closeWebDriver();
			executor.shutdown();
		}

		@Test
		public void verifyGamesInfo() throws IOException {
			// Đường dẫn đến file Excel
			String excelFilePath = "src/test/resources/data/Content Testing_LeapFrog-games.xlsx";
			FileInputStream fileInputStream = new FileInputStream(excelFilePath);
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0);

			// Mở trang web cần test
			open("https://store.leapfrog.com/en-us/apps/c?p=1&platforms=197&product_list_dir=asc&product_list_order=name");

			// Giả sử mỗi game được hiển thị trong div có class "game-item"
			ElementsCollection gameElements = $$(".resultList .product-inner");

			// Duyệt qua các hàng trong Excel, bỏ qua hàng header (hàng đầu tiên)
			for (Row row : sheet) {
				if (row.getRowNum() == 0) continue; // Bỏ qua header

				// Giả sử dữ liệu ở Excel: cột 0: title, cột 1: age, cột 2: price
				String excelTitle = row.getCell(0).getStringCellValue().trim();
				String excelAge = row.getCell(1).getStringCellValue().trim();
				String excelPrice = row.getCell(2).getStringCellValue().trim();

				// Sử dụng ExecutorService để kiểm tra mỗi game song song
				executor.submit(() -> {
					// Tìm phần tử game có tiêu đề khớp với excelTitle (chỉnh sửa selector cho phù hợp)
					SelenideElement gameElement = gameElements.stream()
							.filter(el -> el.$("p.heading > a").getText().trim().equalsIgnoreCase(excelTitle))
							.findFirst()
							.orElse(null);

					// Kiểm tra phần tử không null
					Assert.assertNotNull(gameElement, "Không tìm thấy game có tiêu đề: " + excelTitle);

					// Lấy thông tin tuổi và giá từ phần tử (chỉnh sửa selector cho phù hợp)
					String actualAge = gameElement.$("p.ageDisplay").getText().trim();
					String actualPrice = gameElement.$("p.prices").getText().trim();

					// So sánh với dữ liệu từ file Excel
					Assert.assertEquals(actualAge, excelAge, "Sai tuổi cho game: " + excelTitle);
					Assert.assertEquals(actualPrice, excelPrice, "Sai giá cho game: " + excelTitle);
				});
			}

			// Đợi cho các task hoàn thành
			executor.shutdown();
			while (!executor.isTerminated()) {
				sleep(100);
			}

			workbook.close();
			fileInputStream.close();
		}
	}

