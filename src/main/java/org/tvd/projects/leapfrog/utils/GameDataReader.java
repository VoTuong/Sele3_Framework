package org.tvd.projects.leapfrog.utils;

import io.qameta.allure.Step;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tvd.projects.leapfrog.model.GameData;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameDataReader {

	/**
	 * Reads game data from an Excel file.
	 *
	 * @param excelFilePath the path to the Excel file.
	 * @param sheetIndex    the index of the sheet to read (0-based).
	 * @return a list of GameData objects.
	 * @throws IOException if an error occurs while reading the file.
	 */
	@Step
	public static List<GameData> readGameData(String excelFilePath, int sheetIndex) throws IOException {
		List<GameData> gameDataList = new ArrayList<>();
		FileInputStream fis = new FileInputStream(excelFilePath);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(sheetIndex);

		// Iterate over each row in the sheet (skipping the header row)
		for (Row row : sheet) {
			if (row == null || row.getRowNum() == 0) {
				continue;
			}
			Cell titleCell = row.getCell(0);
			Cell ageCell = row.getCell(1);
			Cell priceCell = row.getCell(2);

			// Skip rows with missing cell data
			if (titleCell == null || ageCell == null || priceCell == null) {
				System.out.println("Skipping row " + row.getRowNum() + " due to missing cell data.");
				continue;
			}

			String title = titleCell.getStringCellValue().trim();
			String age = ageCell.getStringCellValue().trim();
			String price = priceCell.getStringCellValue().trim();
			gameDataList.add(new GameData(title, age, price));
		}

		workbook.close();
		fis.close();
		return gameDataList;
	}
}
