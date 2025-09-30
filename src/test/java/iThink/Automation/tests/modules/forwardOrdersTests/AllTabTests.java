package iThink.Automation.tests.modules.forwardOrdersTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.log4testng.Logger;

import iThink.Automation.base.BaseTest;
import iThink.Automation.modules.forwardOrders.AllTab;
import iThink.Automation.pages.Dashboard;
import iThink.Automation.pages.LoginPage;

public class AllTabTests extends BaseTest{
	
	private static Logger logger = Logger.getLogger(AllTabTests.class);
	private static LoginPage loginpage;
	private static Dashboard dashboard;
	private static AllTab all;
	
	
//	@BeforeTest
//    public void setUpAndLogin() {
//        loginpage = new LoginPage(driver);
//        dashboard = loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234")
//                             .goToDashboard();
//        all = dashboard.goToOrderAll();
//    }
	
	
	@Test
	public void testUniversalAwbSearch() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.universalAWBSearch(all.getFirstAWBFromDatatable());
	}
	
	@Test
	public void testActiveColumns() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
//		List<String> expectedColumns = new ArrayList<>();
		List<String> expectedColumns = Arrays.asList("Order Id", "Shipping AWB", "Status", "Customer", "Risk",
				"Buyer Intent", "Order Date", "Amount", "Payment", "Items", "Pickup Address","Pickup Cutoff",
				"Weight", "EDD Date", "Pickup Date", "Delivered Date", "Return Delivered Date", "Last Updated", "Latest Courier Remark",
				"No. Of Attempts");
		
		Assert.assertEquals(all.getAllTabsActiveColumns(), expectedColumns, "Table Headers mismatched!");
		
	}
	
	@Test
	public void testDateFilterOptions() {	//need to check this test.
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		List<String> expectedDateOptions = Arrays.asList("Today", "Yesterday", "Last 7 Days", "Last 30 Days", "This Month", 
				"Last Month", "Custom Range");
		for(String text : all.getDateOptionsOnPage()) {
			System.out.println(text);
		}
		Assert.assertEquals(all.getDateOptionsOnPage(), expectedDateOptions, "Actual Date Options are not matched with Expected Date Options");
		Assert.assertEquals(all.getDateOptionsOnPage(), "Last 30 Days", "Last 30 Days is not selected");
		Assert.assertTrue(all.isDefaultSelectedDateIsLast30Days(), "30 days option not selected");
		
	}
	
	@Test
	public void testFirstAWB() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		
		System.out.println(all.getFirstAWBFromDatatable());
		all.clickOnFilterButton();
		
	}
	
	@Test
	public void testAllTabFiltersList() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.clickOnFilterButton();
		List<String> expectedFilters = Arrays.asList("Order Id", "Customer Details", "Risk", "Buyer Intent", "Order Date", 
				"Amount", "Payment", "Items", "Pickup Address", "Shipping AWB", "EDD Date", "Pickup Date", "Delivered Date",  
				"Return Delivered Date","Last Updated", "No. of Attempts", "Status", "Address Prediction", "SKU", 
				"Whatsapp Status", "Zone", "Tracking Otp Cancelled");
//		for(String text : all.getAllTabFilters()) {
//			System.out.println(text);
//		}
		
//		List<String> expectedFilterHeaders = Arrays.asList("Order Id", "Customer Name", "Customer Mobile", "Customer Email",
//				"Risk", "Buyer Intent", "Order Date", "Amount", "Payment", "Items", "Pickup Address", "Shipping AWB", 
//				"EDD Date", "Pickup Date", "Delivered Date", "Return Delivered Date", "Last Updated", "No. of Attempts", 
//				"Status", "Address Prediction", "SKU", "Whatsapp Status", "Zone", "Tracking Otp Cancelled");
		
		Assert.assertEquals(all.getAllTabFiltersList(), expectedFilters, "All tab existing filters not matching with expected!");
	}
	
	@Test
	public void testOrderIDList() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
//		all.clickOnFilterButton();
		
//		for(String text : all.getCurrentListOfOrderIDs()) {
//			System.out.println(text);
//		}
		System.out.println("Current orders captured are : "+all.getCurrentListOfOrderIDs().size());
		System.out.println("first Order ID : "+ all.getCurrentListOfOrderIDs().getFirst());
	}
	
	
	@Test(description = "Verify that the user is able to see the shipping AWB filter and "
			+ "when click on the filter and check the single and multiple checkbox and click on the Apply button")
	public void testShippingAWBfilter() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.clickOnFilterButton();
		
		all.shippingAWBfilter("ElasticRun");
	}
	

}
