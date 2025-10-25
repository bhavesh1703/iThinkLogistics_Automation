package iThink.Automation.components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;

import iThink.Automation.utils.WaitUtils;

public class WebTable {
	
	private WebDriver driver;
	private WaitUtils wait;
	private JavascriptExecutor js;
	
	public WebTable(WebDriver driver) {
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		this.js =  (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//table/tbody//tr[1]/td")
	private List<WebElement> cellLocator;
	

	public void getClassNameOfEachCell() {
		wait.sleep(3);
//		List<String> cellClassNames = new ArrayList<String>();
		for(WebElement cellClass : cellLocator) {
			String name = cellClass.getAttribute("class");
			System.out.println("["+name+"]");
		}
	}
	
	 // Generic method to extract text from column div
	private String extractDivText(WebElement element) {
		String text = element.getText().trim();
		if(!text.isEmpty()) return text;
		
		//Use JavaScriptExecutor of inner text
		text = (String) js.executeScript("return argument[0].innerText;", element);
		return text.trim();
	}
	
	//Method to extract data from whole table
	public List<Map<String, String>> getTableData() {
		 List<Map<String, String>> tableData = new ArrayList<>();
		 
		//handle each element with class name
		 String[] columns = { "order_id", "order_customer_details", "order_date", "order_amount",
				 "tracking_status", "itl_order_type", "order_product_quantity", "pickup_address",
				 "awb_no_logistics", "pickup_cutoff", "order_weight", "order_edd", "order_pickup_date",
				 "order_delivered_date", "order_return_delivered_date", "order_last_update_date", 
				 "latest_courier_remark", "no_of_attempts"
		 };
		 
		 List<WebElement> rows = driver.findElements(By.xpath("//table/tbody//tr"));
		 
		 for(int i = 0;i< rows.size(); i++) {
			 Map<String, String> rowData = new LinkedHashMap<>();
			 WebElement row = driver.findElements(By.xpath("//table/tbody//tr")).get(i);
			 
			for(String col : columns) {
				try {
					WebElement cell = row.findElement(By.cssSelector("div." + col));
					String text = extractDivText(cell);
					rowData.put(col, text);
			 }
				catch(StaleElementReferenceException e) {
					 System.out.println("Stale element at row " + i + " col " + col + " — retrying...");
					 driver.navigate().refresh();
					 i--;
					 break;
			}
			 
		 }
		 tableData.add(rowData);
		 
	} 
		 return tableData;
	}
}
