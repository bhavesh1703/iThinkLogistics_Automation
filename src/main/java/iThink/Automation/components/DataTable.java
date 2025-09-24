package iThink.Automation.components;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import iThink.Automation.utils.CommonActions;
import iThink.Automation.utils.WaitUtils;

public class DataTable {
	
	private WebDriver driver;
	private WaitUtils wait;
	private CommonActions comm;
	private Actions action;
	
	public DataTable(WebDriver driver) {
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		this.comm = new CommonActions(driver);
		this.action = new Actions(driver);
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//div[@class='search-outer']/input")
	private WebElement universalAwbSearchInput;
	
	@FindBy(xpath = "//table/thead/tr/th/div/div/div/div[@class='text-light-800 p-column-title']")
	private List<WebElement> tableHeaders;
	
	@FindBy(xpath = "//div[@class='no-record-outer flex flex-col justify-center items-center gap-1']/img")
	private WebElement noRecordFoundImage;
	
	@FindBy(xpath = "//div[@class='no-record-outer flex flex-col justify-center items-center gap-1']/div")
	private List<WebElement> noRecordFoundContext;
	
	@FindBy(xpath = "//table/tbody/tr[@role='row']")
	private List<WebElement> tableEntryRow;
	
	@FindBy(xpath = "//table/tbody/tr/td[@class='awb_no_logistics data-table-header']/div/div/*")
	private List<WebElement> awbNoWithLogo;
	
	@FindBy(xpath = "//table/tbody/tr/td[@class='order_id data-table-header']/div/div/div")
	private List<WebElement> orderIDText;
	
	/** Search the Single AWB in Universal Search
	 * 
	 * @param AWB Number
	 */
	public void setUniversalAwbSearch(String AWB) {
		wait.waitForVisibility(universalAwbSearchInput);
		comm.setInput(universalAwbSearchInput, AWB);
		action.sendKeys(Keys.ENTER).perform();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
	}
	
	/** Get the All Active Column Names 
	 * 
	 * @return Column Headers in List of String.
	 */
	public List<String> getListOfActiveTableHeaders() {
		wait.waitForAllElementsVisibility(tableHeaders);
		List<String> headers = new ArrayList<>();
		
		for(WebElement header : tableHeaders) {
			wait.waitForVisibility(header);
			String text = header.getText().trim();
			headers.add(text);
		}
		return headers;
	}
	
	/** Private Method to check the No Record Found Image is Displayed or not.
	 * 
	 * @return True or False
	 */
	private boolean isNoRecordFoundImageDisplayed() {
		wait.waitForVisibility(By.xpath("//div[@class='no-record-outer flex flex-col justify-center items-center gap-1']/img"));
		return noRecordFoundImage.isDisplayed();
	}
	
	/** Private Method to check the No Record Found context is Displayed or not.
	 * 
	 * @return True or False
	 */
	private boolean isNoRecordFoundContextDisplayed() {
		wait.waitForAllElementsVisibility(noRecordFoundContext);
		return noRecordFoundContext.isEmpty();
	}
	
	/** This Method checks the No Record Found Image and Context are Displayed or not.
	 * 
	 * @return True or False
	 */
	public boolean isNoRecordDisplayed() {
		return isNoRecordFoundImageDisplayed() && isNoRecordFoundContextDisplayed();
	}
	
	/** Get the currently displayed row or entry count
	 * 
	 * @return count of current displaying table entries in integer.
	 */
	public int getEntryRowCount() {
		wait.waitForAllElementsVisibility(By.xpath("//table/tbody/tr[@role='row']"));
		return tableEntryRow.size();
	}
	
	public boolean isAwbDisplayedWithLogo() {
		wait.waitForAllElementsVisibility(By.xpath("//table/tbody/tr/td[@class='awb_no_logistics data-table-header']/div/div"));
		WebElement logisticLogo = awbNoWithLogo.getFirst();
		WebElement awbNumber = awbNoWithLogo.getLast();
		return logisticLogo.isDisplayed() && awbNumber.isDisplayed();
	}
	
	public String getAwbFromDatatable() {
		wait.waitForVisibility(awbNoWithLogo.getFirst());
		return awbNoWithLogo.getFirst().getText().trim();
//		WebElement awbNumber = awbNoWithLogo.getLast();
//		List<String> awbs = new ArrayList<>();
//		for(WebElement awb : awbNumber) {
//			comm.getElementText(awb);
//			awbs.add()
//		}
	}
	
	public List<String> getOrderIDList() {
		wait.waitForAllElementsVisibility(orderIDText);
		List<String> orderIDList = new ArrayList<>();
		try {
			for(WebElement id : orderIDText) {
				String text = comm.getElementText(id);
				orderIDList.add(text);
			}
			return orderIDList;
		}catch (Exception e) {
			throw new RuntimeException("Order ID not captured.", e);
		}
	}
	
	//pending to add to check is data displayed or not after loading the page.
	
}
