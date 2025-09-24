package iThink.Automation.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

//import iThink.Automation.base.BasePage;
import iThink.Automation.base.BaseTest;
import iThink.Automation.pages.Dashboard;
import iThink.Automation.pages.LoginPage;

public class DashboardTests extends BaseTest{
	
	@Test
	public void testNavOrderAll() {
		LoginPage login = new LoginPage(driver); {
		login.fillLogin("xylifedemo@gmail.com", "Admin@1234");
		
		
		}
	}

}
