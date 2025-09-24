package iThink.Automation.tests.modules.forwardOrdersTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import iThink.Automation.base.BaseTest;
import iThink.Automation.modules.forwardOrders.AllTab;
import iThink.Automation.pages.Dashboard;
import iThink.Automation.pages.LoginPage;

public class AllTabTests extends BaseTest{
	
	private static Logger logger = Logger.getLogger(AllTabTests.class);
	
	@Test
	public void testUniversalAwbSearch() {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		Dashboard dashboard = loginpage.goToDashboard();
		AllTab all = dashboard.goToOrderAll();
		all.universalAWBSearch("1323150197218");
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
	
	

}
