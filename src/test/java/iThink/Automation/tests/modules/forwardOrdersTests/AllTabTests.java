package iThink.Automation.tests.modules.forwardOrdersTests;

import java.time.Duration;
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
import iThink.Automation.utils.DevToolsManager;
import iThink.Automation.utils.DriverFactory;

public class AllTabTests extends BaseTest{
	
	private static Logger logger = Logger.getLogger(AllTabTests.class);
	private static LoginPage loginpage;
	private static Dashboard dashboard;
	private static AllTab all;
	private static DevToolsManager devTools;
	
	
//	@BeforeTest
//    public void setUpAndLogin() {
//        loginpage = new LoginPage(driver);
//        dashboard = loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234")
//                             .goToDashboard();
//        all = dashboard.goToOrderAll();
//    }
		
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
	
	
	@Test(description = "Verify that the user is able to see the shipping AWB filter and "
			+ "when click on the filter and check the single and multiple checkbox and click on the Apply button")
	public void testShippingAWBfilter() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.clickOnFilterButton();
		
		all.shippingAWBfilter("ElasticRun");
		
		if(!all.getSelectedLogistic().isEmpty()) {
			for(String text : all.getSelectedLogistic()) {
				System.out.println(text);
			}
		} else {
			System.out.println("No logistic is selected.");
		}
//		for(String text : all.getSelectedLogistic()) {
//			System.out.println(text);
//		}
	}
	
	public void testOrderIDList() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
//		all.clickOnFilterButton();
		
//		for(String text : all.getCurrentListOfOrderIDs()) {
//			System.out.println(text);
//		}
//		System.out.println("Current orders captured are : "+all.getCurrentListOfOrderIDs().size());
//		System.out.println("first Order ID : "+ all.getCurrentListOfOrderIDs().getFirst());
		
		Assert.assertTrue(all.searchValidOrderID(), "Order ID not found!");
		Assert.assertTrue(all.searchInvalidOrderID("ABc-123"), "No Record found not displayed");
	}
	
	//Test Cases
	@Test(description = "Test Case no.1 | Validate Custom Range in Date filter.")
	public void testCustomRange() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		
		List<String> expectedDateOptions = Arrays.asList("Today", "Yesterday", "Last 7 Days", "Last 30 Days", "This Month", 
				"Last Month", "Custom Range");
		
//		for(String text : all.getDateOptionsOnPage()) {
//			System.out.println(text);
////			if(all.getDateOptionsOnPage().size()==0) {
////				break;
////			}
//		}
		
		List<String> actualDateOptions = all.getDateOptionsOnPage();
		
		System.out.println("Expected : "+ expectedDateOptions);
		System.out.println("Actual : "+ actualDateOptions);
		
//		all.selectDateOptionOnPage("Custom Range");
//		all.selectCustomRange("15","2", "Sep, 2025", "Oct, 2025", "This Year");
//		all.selectCustomRange("15-Sep-2025:2-Oct-2025");
		all.selectCustomRange("15-Jan-2023", "2-Feb-2023");
		System.out.println("The Selected Range is - "+all.getSelectedCustomRange());
		
		Assert.assertEquals(actualDateOptions, expectedDateOptions, "Actual Date Options are not matched with Expected Date Options");
//		Assert.assertEquals(all.getDateOptionsOnPage(), "Last 30 Days", "Last 30 Days is not selected");
//		Assert.assertTrue(all.isDefaultSelectedDateIsLast30Days(), "30 days option not selected");
	}	//need to check this test
	
	@Test(description = "Test Case no. 2  | Order id filter apply and No Recorde displayed.")
	public void testSearchOrderIDFilter() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();

		Assert.assertTrue(all.searchValidOrderID(), "Order ID not found!");
		Assert.assertTrue(all.searchInvalidOrderID("ABc-123"), "No Record found not displayed");
	}
	
	@Test(description = "Test Case No. 3 | Risk Filter Apply")
	public void testRiskFilter() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		
//		network.startCapture();
		
		all.riskFilter("High Risk");
		try {
			Thread.sleep(Duration.ofSeconds(2));
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
//		network.stopCapture();
		System.out.println(all.getAllTabCurrentPagination());
		
		boolean called = network.wasAPICalled("/api/v1/order/forward/get/count");
		System.out.println("Was API called : "+ called);
		
		network.printAPIResponseStatus("/api/v1/order/forward/get/count");
		
		network.getResponseBody("/api/v1/order/forward/get/count").ifPresent(body -> {
			System.out.println("Api Response Body : " + body);
		});
	}
	
	@Test(description = "Test Case no. 4 | Amount filter - Select Range condition. ")
	public void testAmountFilterSelectRange() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.clickOnFilterButton();

//		all.amountFilter("greater than", "500");
//		all.clickApplyFilterButtonInAllTab();
		all.amountFilter("Select Range", "500", "1000");
		all.clickApplyFilterButtonInAllTab();
		System.out.println(all.getAllTabCurrentPagination());
		
	} 
	
	@Test(description = "Test case No. 5 | Validate Min input field in Select Range condition") 
	public void testMinInputField() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.clickOnFilterButton();
				
		all.amountMinFieldValue("Abc");
		all.amountMinFieldValue("@$%^");
		all.amountMinFieldValue("-566");
		all.amountMinFieldValue("0000");
		all.amountMinFieldValue("123.456");
		all.amountMinFieldValue("123.45");
	}
	
	@Test(description = "Test case No. 6 | Toast Error message Displayed if Min Value more than Max Value.")
	public void testMinValueMoreThanMaxValue() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.clickOnFilterButton();
		
		all.amountMinFieldValue("1000");
		all.amountMaxFieldValue("100");
		all.clickApplyFilterButtonInAllTab();
		System.out.println(all.getToastMessageInAllTab());
		Assert.assertEquals(all.getToastMessageInAllTab(), "Min Amount should not be greater than Max Amount!");
	}
	
	@Test(description = "Test Case no. 7 | Amount Filter - Greater Than Condition.")
	public void testAmountFilterGreaterThan() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.clickOnFilterButton();

		all.amountFilter("greater than", "5000");
		all.clickApplyFilterButtonInAllTab();
		System.out.println(all.getAllTabCurrentPagination());
		
	}
	
	@Test(description = "Test Case no. 8 | No Record found displayed if wrong awb searched.")
	public void testUniversalAwbSearch() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		
		all.universalAWBSearch(all.getFirstAWBFromDatatable());
		System.out.println(all.getAllTabCurrentPagination());
		
		Assert.assertEquals(all.getAllTabCurrentPagination(), " 1 entries");
		Assert.assertEquals(all.universalAWBSearch("123456789"), "No Records Found");
	}
	
	@Test(description = "Test Case no. 9 | Apply saved filters and get page count.")
	public void testSelectSavedFilter() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		System.out.println("The total entries founds are : " +all.getAllTabCurrentPagination());
//		all.selectSavedFilterByName("LRisk");
		all.selectSavedFilterByName(all.selectFirstSavedFilter());
		
		System.out.println("The total entries founds are : " +all.getAllTabCurrentPagination());
		System.out.println(all.getListOfLiOnDatatable());
	}
	
	@Test
	public void testiIsAnySavedFilterApplied() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		
		all.selectSavedFilterByName("LRisk");
		
		String appliedFilterName = all.getAppliedFilterName();
		System.out.println(appliedFilterName);
		
		if(appliedFilterName.equalsIgnoreCase("None")) {
			System.out.println("There is No applied filter");
		}
		else {
			System.out.println("Filter : '" +appliedFilterName + "' is applied.");
		}
			
	}
	

}
