package Modules.CampusForm;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import common.BaseUI;
import utils.ExcelData;
import TestNGListener.ListenerTest;

@Listeners(ListenerTest.class)

public class TestValidData extends BaseUI {
	@BeforeSuite
	@Parameters("filename")
	public void Exreporting(String filename) {
		startExtentReport(filename);
	}

	@BeforeClass
	@Parameters("testname")
	void beginTest(String testname) {
		test = report.startTest(testname);
	}

	@Test(priority = 1, groups = { "UI", "Smoke", "Functional" },description="Open Browser and Navigate to URL")
	public void GoToURL() {
		//test = report.startTest("Open Browser And Navigate To URL");
		loadPropertiesFile();
		openBrowser("chrome");
		navigateToURL();
	}

	@Test(priority = 2,description="Navigate to Ready To Transform Form")

	void NavigateToForm() {
		//test = report.startTest("Navigate To Form");

		
		WebElement enterprise = waitForElementClickable("ForEnterprise_Id", "ForEnterprise", 30);
		clickElement(enterprise, "EnterPrise");
		WebElement products = waitForElementClickable("Products_Xpath", "Products", 30);
		hoverOverElement(products, "Products");
		/*
		 * List<WebElement> productsList =
		 * waitForVisibilityOfElements("ProductsDropDown_Xpath", "productsList", 30);
		 * for(WebElement option : productsList) {
		 * if(option.getText().equals(getValue("ClickItem")));{ clickElement(option,
		 * "Products option "+getValue("ClickItem")); System.out.println(option); break;
		 * } }
		 */
		WebElement forCampus = waitForElementClickable("ForCampus_Xpath", "ForCampus", 20);
		clickElement(forCampus, "ForCampus");
		switchToNewWindow();
		WebElement contactUs = waitForElementClickable("Contactus_Xpath", "Contact Us", 30);
		clickElement(contactUs, "Contact Us");
	}

	@Test(priority = 3, dependsOnMethods = { "NavigateToForm" },description="Verify Visibility Of Form Elements")
	public void verifyElements() {
		//test = report.startTest("Verify Form Elements");

		// WebElement formName = findElement("FormName_Xpath", "FormName");
		try {
			Assert.assertEquals(getText("FormName_Xpath", "FormName"), getValue("FormName"));
			List<WebElement> formFields = waitForVisibilityOfElements("AllFormFields_Xpath", "All form fields", 40);
			Assert.assertEquals(formFields.size(), 12);
			WebElement submit = waitForElementClickable("SubmitButton_Xpath", "submit", 20);
			Assert.assertTrue(submit.isDisplayed());
			reportPass("Form Elements are present");
		} catch (AssertionError ae) {
			reportFail("Assertion error for form elements");
			Assert.fail();
		}

	}

	@Test(priority = 4, dependsOnMethods = { "verifyElements" },description="Fill Form with Valid Data and Verify Results")
	void fillFormValidData() {
		//test = report.startTest("Campus Form Valid Data");
		ExcelData ex = new ExcelData();
		String data[] = ex.getExcelData("ValidData");
		// System.out.println(data[0]);
		WebElement firstName = findElement("FirstName_Id", "First Name");
		enterText(firstName, data[0], "First Name");

		WebElement lastName = findElement("LastName_Id", "Last Name");
		enterText(lastName, data[1], "Last Name");

		Select jobFunction = selectDropDown("JobFunction_Id", "Job Function");
		selectByText(jobFunction, data[2], "Job Function");

		WebElement jobTitle = findElement("JobTitle_Id", "JobTitle");
		enterText(jobTitle, data[3], "Job Title");

		WebElement email = findElement("WorkEmail_Id", "Work Email");
		enterText(email, data[4], "Work Email");

		WebElement phone = findElement("Phone_Id", "Phone");
		enterText(phone, data[5], "Phone");

		WebElement instName = findElement("InstitutionName_Id", "Instituion Name");
		enterText(instName, data[6], "Institution Name");

		Select instType = selectDropDown("InstitutionType_Id", "Institution Type");
		selectByText(instType, data[7], "Institution Type");

		Select primaryDis = selectDropDown("PrimaryDiscipline_Id", "Primary Discipline");
		selectByText(primaryDis, data[8], "Primary Discipline");

		Select country = selectDropDown("Country_Id", "Country");
		selectByText(country, data[9], "Country");
		if (data[9].equals("India")) {
			Select state = selectDropDown("State_Id", "State");
			selectByText(state, data[10], "State");
		}

		WebElement tellUs = findElement("TellUsHow_Id", "Tell Us How");
		enterText(tellUs, data[11], "Tell Us How");

		WebElement submit = findElement("SubmitButton_Xpath", "Submit");
		clickElement(submit, "Submit");

		List<WebElement> successmsg = waitForVisibilityOfElements("SuccesMsg_Xpath", "Succes Message", 20);
		Assert.assertEquals(getText("SuccesMsg_Xpath", "Succes Message"),
				"Thank you for your interest in Coursera for Campus");
		reportPass("Success Message Captured");
		test.log(LogStatus.INFO,"Succes Message Retrived as :"+getText("SuccesMsg_Xpath", "Succes Message"));

		/*
		 * WebElement firstName = findElement("FirstName_Id", "First Name");
		 * enterText(firstName, "aswin", "First Name"); WebElement firstName =
		 * findElement("FirstName_Id", "First Name"); enterText(firstName, "aswin",
		 * "First Name"); WebElement firstName = findElement("FirstName_Id",
		 * "First Name"); enterText(firstName, "aswin", "First Name"); WebElement
		 * firstName = findElement("FirstName_Id", "First Name"); enterText(firstName,
		 * "aswin", "First Name"); WebElement firstName = findElement("FirstName_Id",
		 * "First Name"); enterText(firstName, "aswin", "First Name");
		 */
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

	@AfterSuite
	void quit() {

		endExtentReport();

	}
}
