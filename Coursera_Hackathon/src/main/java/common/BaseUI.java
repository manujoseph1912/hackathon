package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import utils.ExtentReportManager;

public class BaseUI extends ExtentReportManager {
	public WebDriver driver;
	public Properties prop;
	public String userDir = System.getProperty("user.dir");
	String propFilePath = "\\src\\test\\java\\resources\\config.properties";
	Set<String> windowIds;
	static int ssNo = 0;
	String[] windowIdsArray;
	String parentWindow;

	/*
	 * // This method is to load properties file
	 */	public void loadPropertiesFile() { 
		if (prop == null) {
			prop = new Properties();
			try {
				FileInputStream file = new FileInputStream(userDir + propFilePath);
				prop.load(file);
				System.out.println("dfdf" + test);
			} catch (Exception e) {

				Assert.fail("Exception loading properties file", e);

			}
		}
	}

	public void printExceptionError(Object e) {
		System.out.println(((Throwable) e).getMessage());
	}

/*	// This method is to launch browser
*/	public void openBrowser(String browserName) {
		try {
			if (browserName.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver", userDir + prop.getProperty("ChromeDriverPath"));
				driver = new ChromeDriver();
			} else if (browserName.equalsIgnoreCase("edge")) {
				System.setProperty("webdriver.edge.driver", userDir + prop.getProperty("EdgeDriverPath"));
				driver = new EdgeDriver();
			} else {

				Assert.fail("No browser opened, provided browser name invalid, enter chrome or edge only");
			}
			reportPass(browserName + " Browser is Opened Successfully");
			driver.manage().window().maximize();
		} catch (Exception e) {
			Assert.fail("No browser opened", e);

		}
	}

/*	// This Method is used to navigate to given URL
*/	public void navigateToURL() {
		try {
			driver.get(prop.getProperty("BaseURL"));
			parentWindow = driver.getWindowHandle();
			reportPass(prop.getProperty("BaseURL") + " is accessible");
		} catch (Exception e) {
			reportFail(prop.getProperty("baseURL") + "is not accessible exception");
			// occured");
			Assert.fail("Not navigated to given URL", e);

		}

	}

/*	// This method is used to close tab/window
*/	public void closeBrowser() {
		try {
			driver.close();
			test.log(LogStatus.PASS, "Tab close successfull");
		} catch (Exception e) {
			reportFail("Unable to close current tab/window exception occured");
			Assert.fail("Exception thrown for closing tab/window", e);
		}
	}

	public void tearDown() { // This method is used to Quit driver
		try {
			driver.quit();
			test.log(LogStatus.PASS, "Driver quit successfull");
			// reportPass("Driver quit successfull");
		} catch (Exception e) {
			reportFail("Driver quit not successfull exception occured");
			Assert.fail("Exception thrown for driver quit", e);

		}
	}

/*	 This method is used to get key property from Properties file 
*/	public String getValue(String locatorKey) {
		return prop.getProperty(locatorKey);
	}

	/*
	 * This method is used to find element based on given locator
	 */
	public WebElement findElement(String locatorKey, String elementName) {
		WebElement element = null;
		try {

			if (locatorKey.endsWith("_Id")) {
				element = driver.findElement(By.id(getValue(locatorKey)));
			} else if (locatorKey.endsWith("_Xpath")) {
				element = driver.findElement(By.xpath(getValue(locatorKey)));
			} else if (locatorKey.endsWith("_TagName")) {
				element = driver.findElement(By.tagName(getValue(locatorKey)));
			} else {
				Assert.fail("Locator key does match with any provided conditions");
			}
		} catch (Exception e) {
			reportFail("Unable to locate element " + elementName);
			Assert.fail("Unable to locate element", e);
		}
		return element;
	}

/*	 This method is to find elements based on given locator key 
*/	public List<WebElement> findElements(String locatorKey, String elementsName) {
		List<WebElement> elements = null;
		try {

			if (locatorKey.endsWith("_Id")) {
				elements = driver.findElements(By.id(getValue(locatorKey)));
			} else if (locatorKey.endsWith("_Xpath")) {
				elements = driver.findElements(By.xpath(getValue(locatorKey)));
			} else if (locatorKey.endsWith("_TagName")) {
				elements = driver.findElements(By.tagName(getValue(locatorKey)));
			} else {
				Assert.fail("Locator key does match with any provided conditions");
			}

		} catch (Exception e) {
			reportFail("Unavle to locate elements " + elementsName);
			Assert.fail("Unable to locate elements" + e);

		}
		return elements;
	}

/*	 This method is to verify page Title 
*/	public void verifyTitle(WebDriver driver, String titleLocatorKey) {
		Assert.assertEquals(driver.getTitle(), getValue("titleLocatorKey"));
	}

/*	 This method is used to check element visibility 
*/	public void checkElementIsDisplayed(String locatorKey, String elementName) {
		WebDriverWait wait = new WebDriverWait(driver, 40);

		try {

			try {
				if (locatorKey.endsWith("_Id")) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(getValue(locatorKey))));
					Assert.assertTrue(driver.findElement(By.id(getValue(locatorKey))).isDisplayed());
				} else if (locatorKey.endsWith("_Xpath")) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getValue(locatorKey))));
					Assert.assertTrue(driver.findElement(By.xpath(getValue(locatorKey))).isDisplayed());
				} else if (locatorKey.endsWith("_TagName")) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(getValue(locatorKey))));
					Assert.assertTrue(driver.findElement(By.tagName(getValue(locatorKey))).isDisplayed());
				} else {
					Assert.fail("Locator key does match with any provided conditions");
				}
				reportPass(elementName + " is present");
			} catch (AssertionError ae) {
				reportFail(elementName + " not present");
			}
		} catch (Exception e) {
			reportFail("Unable to locate element " + elementName);
			Assert.fail("Unable to locate element", e);
		}

	}

	/*
	 * This method is used to enter text in WebElement
	 */ public void enterText(WebElement element, String inputText, String elementName) {
		try {
			element.sendKeys(inputText);
			reportPass(inputText + " was entered in " + elementName);
		} catch (Exception e) {
			reportFail(inputText + " was not entered in " + elementName);
			Assert.fail("Search text not entered", e);
		}
	}

	/*
	 * This method is used to click a WebElement
	 */ public void clickElement(WebElement element, String elementName) {
		try {
			element.click();
			reportPass(elementName + " was clicked");
		} catch (Exception e) {
			reportFail(elementName + " not clicked");
			Assert.fail("Element not clicked", e);
		}
	}

	/*
	 * This method is used to get Text from WebElement
	 */ public String getText(String locatorKey, String elementName) {
		String text = null;
		WebElement element = null;
		try {
			element = findElement(locatorKey, elementName);
			text = element.getText();

		} catch (Exception e) {
			reportFail("Text not retrieved from " + elementName);

			Assert.fail("Text not retrieved from element", e);

		}
		return text;
	}

	/*
	 * This method is used to retrieve attribute value of WebElement
	 */ public String getAttributeValue(WebElement element, String elementName, String attribute) {
		String text = null;
		try {
			text = element.getAttribute(attribute);
		} catch (Exception e) {
			reportFail("Attribute " + attribute + " not retrieved from " + elementName);
			Assert.fail("Attribute value not retrieved from element,e");
		}
		return text;
	}

	/*
	 * This method is used to locate Select drop down based on locato key
	 */ public Select selectDropDown(String locatorKey, String dropDownName) {
		Select dropDown = null;
		try {
			dropDown = new Select(driver.findElement(By.id(getValue(locatorKey))));

		} catch (Exception e) {
			reportFail("Unable to locate " + dropDownName + " select drop down");
			Assert.fail("Select drop down not located", e);
		}
		return dropDown;

	}

	/*
	 * This method is used to select a option from Select drop down based on visible
	 * text
	 */
	public void selectByText(Select dropDown, String value, String dropDownName) {
		WebElement optionSelected;
		try {
			dropDown.selectByVisibleText(value);
			optionSelected = dropDown.getFirstSelectedOption();
			try {
				Assert.assertEquals(optionSelected.getText(), value);
				reportPass(value + " Selected from drop down:" + dropDownName);

			} catch (AssertionError e) {
				reportFail("Selected option does not match provided option " + e.getMessage());
				Assert.fail("Selected option does not match provided option ", e);

			}

		} catch (Exception e) {
			reportFail("Unable to select " + value + " from dropDown: " + dropDownName);
			Assert.fail("Unable to select specified option from select drop down", e);
		}
	}

	/*
	 * This method is used to select option from Filter based on option key
	 */ public void selectFromFilter(List<WebElement> options, String optionKey, String filterName) {
		// System.out.println("Languages:"+languages.size());
		try {
			int flag = 0;
			for (WebElement option : options) {
				String value = option.getAttribute("value");
				/*
				 * System.out.println(value); System.out.println(i);
				 */
				if (value.equals(getValue(optionKey))) {
					flag = 1;
					clickElement(option, getValue(optionKey));
					reportPass(getValue(optionKey) + " is selected");

					break;
				}

			}
			if (flag == 0) {
				Assert.fail("Given option not in filter drop down");
			}
		} catch (Exception e) {
			reportFail("Unable to select " + getValue(optionKey) + " from filter:");
			/// "+filterName);
			Assert.fail("Specfied option not selected from filter", e);
		}
	}

	/*
	 * This method is used to wait for a element to be clickable based given seconds
	 */ public WebElement waitForElementClickable(String locatorKey, String elementName, int seconds) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, seconds);

			if (locatorKey.endsWith("_Id")) {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.id(getValue(locatorKey))));
			} else if (locatorKey.endsWith("_Xpath")) {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(getValue(locatorKey))));
			} else if (locatorKey.endsWith("_TagName")) {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.tagName(getValue(locatorKey))));
			} else {
				Assert.fail("Locator key does match with any provided conditions");
			}

		} catch (Exception e) {
			reportFail("Unable to locate element " + elementName);
			Assert.fail("Exception thrown for waiting for element", e);
		}
		return element;

	}

	/*
	 * This method is used to wait for visibility of WebElements
	 */	public List<WebElement> waitForVisibilityOfElements(String locatorKey, String elementsName, int seconds) {
		List<WebElement> elements = new ArrayList<WebElement>();
		try {
			WebDriverWait wait = new WebDriverWait(driver, seconds);

			if (locatorKey.endsWith("_Id")) {
				elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(getValue(locatorKey))));
			} else if (locatorKey.endsWith("_Xpath")) {
				elements = wait
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(getValue(locatorKey))));
			} else if (locatorKey.endsWith("_TagName")) {
				elements = wait
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName(getValue(locatorKey))));
			} else {
				Assert.fail("Locator key does match with any provided conditions");
			}

		} catch (Exception e) {
			reportFail("Unable to locate elements " + elementsName);
			Assert.fail("Exception thrown for waiting for elements", e);
			e.printStackTrace();
		}
		return elements;

	}

	/*
	 * This method is used to print course details
	 */	public void getCourseDetails(String course) {
		try {
			synchronized (System.out) {
				System.out.println(course + " Details");
				String CourseTitle = getText("CourseName_TagName", course + " Title");
				System.out.println(CourseTitle);
				String CourseRating = getText("CourseRating_Xpath", course + " Rating");
				System.out.println(CourseRating.replaceAll("\n", " "));
				String CourseHours = getText("CourseHours_Xpath", course + " Hours");
				System.out.println(CourseHours);
			}
			reportPass("Details of " + course + "retrieved successfully");
		} catch (Exception e) {
			reportFail("Details of " + course + "not retrieved successfully");
			printExceptionError(e);
		}
	}

	/*
	 * This method is used to hover over element using actions class
	 */	public void hoverOverElement(WebElement element, String elementName) {
		try {
			Actions mouseOver = new Actions(driver);
			mouseOver.moveToElement(element).build().perform();
			reportPass("Mouse hovered over " + elementName);
		} catch (Exception e) {
			reportFail("Mouse not hovered over " + elementName);
			Assert.fail("Mouse hover exception", e);
		}
	}

	/*
	 * This method is used to add screenshot in Extent Report
	 */	public String addScreenShot() {
		String imgPath = captureScreen();
		return ExtentReportManager.test.addScreenCapture(imgPath);

	}
/*This method is used to capture screen
*/	public String captureScreen() {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Date date = new Date();

		String d = date.toString();
		d = d.replaceAll(" ", "");
		d = d.replaceAll(":", "");
		String imgPath = userDir + "\\ExtentReports\\ExScreenshots\\capture" + ssNo + d + ".png";
		try {
			FileHandler.copy(screenshot, new File(imgPath));
			// System.out.println("Screen Captured");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ssNo++;
		return imgPath;

	}

	/*
	 * This method is used to switch to new window/tab
	 */	public void switchToNewWindow() {
		try {
			windowIds = driver.getWindowHandles();
			windowIdsArray = windowIds.toArray(new String[windowIds.size()]);
			driver.switchTo().window(windowIdsArray[1]);
			reportPass("Driver switched to child window/tab successfully");
		} catch (Exception e) {
			reportFail("Driver not switched to child window/tab");
			Assert.fail("Exception in switching to new window/tab", e);
		}
	}

	/*
	 * This method is used to switch to parent window
	 */	public void switchToParentWindow() {
		try {
			driver.switchTo().window(parentWindow);
			reportPass("Driver switched to parent window successfully");
		} catch (Exception e) {
			reportFail("Driver not switched to parent window");
			Assert.fail("Exception in switching to parent window", e);
		}
	}

	/*
	 * This method is used to navigate back
	 */	public void navigateBack() {
		try {
			driver.navigate().back();
		} catch (Exception e) {
			Assert.fail("Exception thrown for navigating back", e);
		}
	}

	/*
	 * This method is to log Pass status in Extent Report
	 */	public void reportPass(String message) {
		String ss = addScreenShot();
		test.log(LogStatus.PASS, message + ss);
	}

	/*
	 * This method is log Fail status in Extent Report
	 */	public void reportFail(String message) {
		String ss = addScreenShot();
		test.log(LogStatus.FAIL, message + ss);
	}
}
