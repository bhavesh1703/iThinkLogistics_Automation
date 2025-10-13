package iThink.Automation.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	private static Workbook workbook;
	
	public static Object[][] getTestData(String sheetName) {
		try {
			FileInputStream fis = new FileInputStream(ConfigReader.getProperty("testdataPath")+"/Login_Data.xlsx");
//			FileInputStream fis = new FileInputStream("C:\\Users\\bhara\\git\\iThinkLogistics_Automation\\src\\test\\resources\\testdata\\Login_Data.xlsx");
			workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheet(sheetName);
			
			int rowCount = sheet.getPhysicalNumberOfRows();
			int colCount = sheet.getRow(0).getLastCellNum();
			
			//skip header and Test Case ID
			Object[][] data = new Object[rowCount - 1][colCount - 1];
			
			for(int i = 1; i < rowCount; i++) {
				Row row = sheet.getRow(i);
				for(int j = 1; j < colCount; j++) {
					Cell cell = row.getCell(j);
					data[i - 1][j - 1] = cell != null ? cell.toString().trim() : "";
				}
			}
			return data;
		} catch (IOException e) {
			throw new RuntimeException("Error Reading Excel file. ", e);
		}
	}
	
	 public void close() {
	        try {
	            if (workbook != null ) {
	                workbook.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
