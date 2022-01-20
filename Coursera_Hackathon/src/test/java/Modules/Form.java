package Modules;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.BaseUI;
import utils.ExcelData;

public class Form extends BaseUI {
	@BeforeTest(alwaysRun=true)
	@Parameters("testname")
	public void Exreporting(String testname) {
		startExtentReport(testname);
		test = report.startTest(testname);
	}
	@Test(description="Regression Test Ready To Form",groups= {"Regression"})
	void startTest() {
		loadPropertiesFile();
		openBrowser("chrome");
		navigateToURL();
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

	}
	@AfterTest(alwaysRun=true)
	void quit() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		report.endTest(test);
		tearDown();
		endExtentReport();

	}
}
