package utils;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import common.BaseUI;

public class ExcelData {
	BaseUI obj = new BaseUI();
	static XSSFWorkbook TestData;
	static XSSFSheet DataSheet;
	static XSSFRow row;
	XSSFCell cell;

	public String[] getExcelData(String sheetname) {
		String[] data = new String[12];
		try {
			File file = new File("C:\\Users\\2081002\\Documents\\FormTestData.xlsx");
			//System.out.println(file.getAbsolutePath());
			//System.out.println(sheetname);
			
			FileInputStream inputStream = new FileInputStream(file);
			TestData = new XSSFWorkbook(inputStream);
			//System.out.println(TestData);
			
			if(sheetname.equals("ValidData")) {
				DataSheet = TestData.getSheetAt(0);
				}else if(sheetname.equals("InvalidData")) {
					DataSheet = TestData.getSheetAt(1);
				}
			//System.out.println(DataSheet);
			//System.out.println(TestData.getSheet(sheetname));
			//System.out.println();
			
			row = DataSheet.getRow(1);
			//System.out.println(row);
			DataFormatter formatter = new DataFormatter();
			for (int i = 0; i < 12; i++) {
				 cell = row.getCell(i);
				switch (cell.getCellTypeEnum()) {
				case STRING:
					data[i] = cell.getStringCellValue();

					break;

				case NUMERIC:
					data[i] = formatter.formatCellValue(cell);
					break;
				default:
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			obj.reportFail("exception reading excel file");
			Assert.fail("Exception reading excel file");
;
		}
		return data;
	}
}
