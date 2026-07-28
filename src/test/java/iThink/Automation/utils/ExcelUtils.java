package iThink.Automation.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
//	private static Workbook workbook;
	
	public static Object[][] getTestData(String sheetName) {
		try(FileInputStream fis = new FileInputStream(ConfigReader.getProperty("testDataPath"));
			Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheet(sheetName);

			if(sheet == null) {
				throw new RuntimeException("Sheet '" + sheetName + "' not found in Excel file");
			}

			int rowCount = sheet.getPhysicalNumberOfRows();

			if(rowCount <=1) {
				throw new RuntimeException("No test data found in sheet '"+ sheetName +"'.");
			}

			int colCount = sheet.getRow(0).getLastCellNum();
			
			//skip header and Test Case ID
			Object[][] data = new Object[rowCount - 1][colCount - 1];
			
			for(int i = 1; i < rowCount; i++) {
				Row row = sheet.getRow(i);
				for(int j = 1; j < colCount; j++) {
					Cell cell = row.getCell(j);
					data[i - 1][j - 1] = (cell != null) ? cell.toString().trim() : "";
				}
			}
			return data;
		} catch (IOException e) {
			throw new RuntimeException("Error Reading Excel file.", e);
		}
	}
}
