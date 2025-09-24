package iThink.Automation.modules.forwardOrders;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import iThink.Automation.components.DataTable;
import iThink.Automation.pages.Dashboard;
import iThink.Automation.utils.CommonActions;
import iThink.Automation.utils.WaitUtils;

public class AllTab {
	
	private WebDriver driver;
	private WaitUtils wait;
	private CommonActions comm;
	private Actions action;
	private DataTable datatable;
	private static final Logger logger = LogManager.getLogger(AllTab.class);
	
	public AllTab(WebDriver driver) {
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		this.comm = new CommonActions(driver);
		this.action = new Actions(driver);
		this.datatable = new DataTable(driver);
		PageFactory.initElements(driver, this);
	}
//	
//	public void load() {
//		logger.info("Waiting for Order All tab to load");
//		wait.waitForPageLoads(10);
//		wait.waitForVisibility(By.xpath("//div[@class='d-inline-block gray-800']"));
//		logger.info("Dashboard loads successfully...");		
//	}
	
	public List<String> getAllTabsActiveColumns() {
		return datatable.getListOfActiveTableHeaders();
	}
	
	public String universalAWBSearch(String AWB) {
		datatable.setUniversalAwbSearch(AWB);
		
		if(datatable.getEntryRowCount() >= 1) {
			return datatable.getAwbFromDatatable();
			
		} else if (datatable.isNoRecordDisplayed()){
			return "No Record Found is Displayed.";
		}
		throw new IllegalStateException("Unexpected state: Neither rows found nor 'No Record Found' displayed.");
	}
	
	
	
	

}
