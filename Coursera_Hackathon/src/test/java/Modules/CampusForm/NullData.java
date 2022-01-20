package Modules.CampusForm;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import common.BaseUI;

public class NullData extends BaseUI{
	/*@BeforeTest
	@Parameters("filename")
	public void Exreporting(String filename) {
		startExtentReport(filename);
	}*/
	@BeforeClass
	@Parameters("testname")
	void beginTest(String testname) {
		test= report.startTest(testname);
	}
	@Test(priority=1,groups= {"UI","Smoke","Functional"},description="Open Browser and Navigate to URL")
	public void GoToURL() {
		//test= report.startTest("Open Browser And Navigate To URL");
		loadPropertiesFile();
		openBrowser("chrome");
		navigateToURL();
		}
	
	
	@Test(priority=2,description="Navigate to Ready To Transform Form")
	void NavigateToForm() {
		//test= report.startTest("Navigate To Form");

		
		WebElement enterprise = waitForElementClickable("ForEnterprise_Id", "ForEnterprise", 30);
		clickElement(enterprise,"EnterPrise");
		WebElement products = waitForElementClickable("Products_Xpath", "Products", 30);
		hoverOverElement(products, "Products");
		/*List<WebElement> productsList = waitForVisibilityOfElements("ProductsDropDown_Xpath", "productsList", 30);
		for(WebElement option : productsList) {
			if(option.getText().equals(getValue("ClickItem")));{
				clickElement(option, "Products option "+getValue("ClickItem"));
				System.out.println(option);
				break;
			}
		}*/
		WebElement forCampus = waitForElementClickable("ForCampus_Xpath","ForCampus", 20);
		clickElement(forCampus, "ForCampus");
		switchToNewWindow();
		WebElement contactUs = waitForElementClickable("Contactus_Xpath", "Contact Us", 30);
		clickElement(contactUs,"Contact Us");
	}
	@Test(priority=3,dependsOnMethods= {"NavigateToForm"},description="Verify Visibility Of Form Elements")
	public void verifyElements() {
		//test= report.startTest("Verify Form Elements");

		//WebElement formName = findElement("FormName_Xpath", "FormName");
		try{Assert.assertEquals(getText("FormName_Xpath", "FormName"),getValue("FormName"));
		List<WebElement> formFields = waitForVisibilityOfElements("AllFormFields_Xpath", "All form fields", 40);
		Assert.assertEquals(formFields.size(),12);
		WebElement submit = waitForElementClickable("SubmitButton_Xpath", "submit", 20);
		Assert.assertTrue(submit.isDisplayed());
		reportPass("Form Elements are present");
		}catch(AssertionError ae) {
			reportFail("Assertion error for form elements");
			Assert.fail();
		}
		

	}
	@Test(priority=4,dependsOnMethods= {"NavigateToForm"},description="Submit Form With Empty Fields")
	public void submitForm() {
		WebElement submit = findElement("SubmitButton_Xpath", "Submit");
		clickElement(submit, "Submit");
		List<WebElement> errormsg = waitForVisibilityOfElements("FirstNameErrMsg_Id", "Error Message", 20);
		Assert.assertEquals(getText("FirstNameErrMsg_Id", "Error Message"),
				"This field is required.");
		reportPass("Error Message Captured");
		test.log(LogStatus.INFO,"Error Message Retrived as :"+getText("FirstNameErrMsg_Id", "Error Message"));
	}
	@AfterClass
	void endClass() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		report.endTest(test);
		tearDown();
	}

}
