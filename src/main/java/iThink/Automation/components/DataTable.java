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
	
	@FindBy(xpath = "//div[@class='datepicker-outer']")
	private WebElement pageDateFilter;
	
	@FindBy(xpath = "//div/p")
	private List<WebElement> dateFilterOptions;
	
	@FindBy(xpath = "//span[@class='filters nest-hub-max-inner-filter']")
	private WebElement filtersButton;
	
	@FindBy(xpath = "//div/p[@class='header']")
	private WebElement allFiltersHeader;
	
	@FindBy(xpath = "//div[@class='w-[168px] p-2 lists hover:cursor-pointer']")
	private List<WebElement> filtersList;
	
	@FindBy(xpath = "//div/span[@class='right-filter-header pl-[14px]']")
	private List<WebElement> allFiltersHeaders;
	
	@FindBy(xpath = "//div/div/img[@alt='Search']")
	private List<WebElement> allSearchIconsInFilters;	//captures all search icons in filter modalbox
	
	@FindBy(xpath = "//div[@id='order_id']/div/*")
	private List<WebElement> orderIDFilterField;	//captures both search icon and inputbox in filter modalbox
	
	@FindBy(xpath = "//div[@id='order_id']/div/input")
	private WebElement orderIDFilter;
	
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
	
	/** Checks the is AWB displayed with logo**/
	public boolean isAwbDisplayedWithLogo() {
		wait.waitForAllElementsVisibility(By.xpath("//table/tbody/tr/td[@class='awb_no_logistics data-table-header']/div/div"));
		WebElement logisticLogo = awbNoWithLogo.getFirst();
		WebElement awbNumber = awbNoWithLogo.getLast();
		return logisticLogo.isDisplayed() && awbNumber.isDisplayed();
	}
	
	public String getAwbFromDatatable() {
		wait.waitForVisibility(awbNoWithLogo.getFirst());
		return awbNoWithLogo.getLast().getText().trim();
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
	
	//pending to add to checkpoint for data displayed or not after loading the page.
	
	/*** Click on Date Filter displayed on Page ***/
	public void clickPageDateFilter() {
		wait.waitForVisibility(pageDateFilter);
		comm.clickButton(pageDateFilter);
	}
	
	/*** Get List of Date Options 
	 * @return  List of All Date Options in Date Filter displayed on page**/
	public List<String> getDateOptions() {
		wait.waitForAllElementsVisibility(By.xpath("//div/p"));	//this method throws stale element exeception
		List<String> dateOptions = new ArrayList<>();
		try {
			for(WebElement option : dateFilterOptions) {
				String text = option.getText().trim();
				dateOptions.add(text);
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return dateOptions;
	}
	
	/*** Get the Selected Date Option in Date Filter**/
	public String getSelectedDateOption() {
		wait.waitForAllElementsVisibility(dateFilterOptions);
		for(WebElement option : dateFilterOptions) {
			String selectedOption = option.getAttribute("class");
			if(selectedOption.contains("m-0 text-sm text-black dark:text-white")) {
				return option.getText().trim();
			}
		} return "Any Date is not selected.";
	}
	
	public boolean isFilterButtonDisplayed() {
		wait.waitForVisibility(filtersButton);
		return comm.isElementDisplayed(filtersButton);
	}
	
	public void clickOnFiltersButton() {
		try {
			if(isFilterButtonDisplayed()) {
				comm.clickButton(filtersButton);
			}
			else {
				wait.waitForVisibility(By.xpath("//span[@class='filters nest-hub-max-inner-filter']")).click();
			}
		} catch(Exception e) {
			throw new RuntimeException("Filter button is not displayed.", e);
		}
	}
	
	/** Check Filters Modalbox is opened or not**/
	public boolean isFiltersModalboxOpens() {
		wait.waitForVisibility(By.xpath("//div/p[@class='header']"));
		return comm.isElementDisplayed(allFiltersHeader);
	}
	
	/** Get the list of Filters List displayed at Left side in Order ID modalbox.
	 * In this method subfilter names are not captured.
	 * eg. Customer Details (captured)
	 *  Customer Name, Email, Mobile. (ommitted)
	 * @return List of String of Filters displayed at left side.
	 */
	public List<String> getListOfFilters() {
		wait.waitForAllElementsVisibility(By.xpath("//div[@class='w-[168px] p-2 lists hover:cursor-pointer']"));
		List<String> filterList = new ArrayList<>();
		for(WebElement filter : filtersList) {
			String name = comm.getElementText(filter);
			filterList.add(name);
		}
		return filterList;
	}
	
	/** This Method return all the Filters which headers are displayed.
	 * This method omitts Main Filter header, if sub filter is present.
	 * eg. Customer Details (ommitted)
	 *  - Customer Name, Customer Email, Customer Mobile No. (captured).
	 * @return List of String of filters displayed at right side.
	 */
	public List<String> getFiltersHeaders() {
		wait.waitForVisibility(By.xpath("//div/span[@class='right-filter-header pl-[14px]']"));
		List<String> filterHeaders = new ArrayList<>();
		for(WebElement filter : allFiltersHeaders) {
			String header = comm.getElementText(filter);
			filterHeaders.add(header);
		}
		return filterHeaders;
	}
	
	public void setOrderIDFilter(String orderID) {
		
	}
	
	public boolean isAllSearchIconsAreDisplayed() {
		wait.waitForVisibility(By.xpath("//div/div/img[@alt='Search']"));
		for(WebElement icon : allSearchIconsInFilters) {
			return icon.isDisplayed();
		}
		return false;
	}
	
	
}
