package Modules;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.BaseUI;
import TestNGListener.ListenerTest;

@Listeners(ListenerTest.class)

public class PrintCourses extends BaseUI {
	int i=0;
	List<WebElement> courses;

	@BeforeTest(alwaysRun=true)
	@Parameters("testname")
	public void Exreporting(String testname) {
		startExtentReport(testname);
		test = report.startTest(testname);
	}
	
	@Test(priority=1,groups= {"UI","Smoke","Functional","Regression"},description="Open Browser and Navigate To URL")
	public void GoToURL() {
		test= report.startTest("Open Browser And Navigate To URL");
		loadPropertiesFile();
		openBrowser("chrome");
		navigateToURL();
		}
	@Test(priority=2,dependsOnMethods= {"GoToURL"},groups= {"UI","Regression"},description="Check Home Page UI")
	void checkHomePageUI() {
		test= report.startTest("Home Page UI");
		checkElementIsDisplayed("ExploreButton_Xpath", "Explore Button");
		checkElementIsDisplayed("SearchBox_Xpath", "Search Box");
		checkElementIsDisplayed("ForEnterprise_Id", "For Enterprise");
		//checkElementIsDisplayed("ForStudents_Xpath", "For Students");
		checkElementIsDisplayed("JoinForFree_Xpath", "Join For Free Button");
		String expBottomElements[] = {"Start or advance your career","Browse popular topics",
				"Popular courses and articles","Earn a degree or certificate online","Coursera","Community","More"};
		WebElement bottom = waitForElementClickable("Bottom_Xpath", "Bottom", 40);
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", bottom);
		List<WebElement> bottomElements = waitForVisibilityOfElements("BottomElements_Xpath", "Bottom Elements", 40);
		
		//System.out.println(bottomElements.size());
		try{
			for(WebElement element : bottomElements) {
			Assert.assertEquals(element.getText(), expBottomElements[i]);
			i++;
			}
			i=0;
			reportPass("Bottom elements are as expectd");
		}catch(AssertionError ae) {
			reportFail("Bottom elements not as expected "+ae.getMessage());
			Assert.fail("Bottom Elements Not as expected");
		}
		
	}
	@Test(priority=3,dependsOnMethods= {"checkHomePageUI"},groups= {"Functional","Regression"},description="Search for Course")
	void searchForCourse() {
		//test= report.startTest("Search And Print Courses");
		WebElement searchBox = waitForElementClickable("SearchBox_Xpath", "SearchBox", 30);
		WebElement searchIcon = waitForElementClickable("SearchIcon_Xpath", "SearchIcon", 30);
		clickElement(searchBox, "SearchBox");
		enterText(searchBox, getValue("CourseSearch1"), "SearchBox");
		clickElement(searchIcon, "SearchIcon");
		}
	@Test(priority=4,dependsOnMethods= {"checkHomePageUI","searchForCourse"},groups= {"Smoke","Functional","Regression"},description="Selecting Filters")
	void selectFilters() {
		test= report.startTest("Selecting Filters");
		WebElement languageFilter = waitForElementClickable("LanguageFilter_Xpath", "LanguageFilter", 30);
		clickElement(languageFilter, "LanguageFilter");
		List<WebElement> languages = findElements("ElementsInFilter_Xpath", "Languages");
		selectFromFilter(languages,"LanguageOption","Language");
		
		WebElement levelFilter = waitForElementClickable("LevelFilter_Xpath", "LevelFilter", 30);
		clickElement(levelFilter, "LevelFilter");
		List<WebElement> levels = findElements("ElementsInFilter_Xpath", "Levels");
		selectFromFilter(levels,"LevelOption","Level");
		}
	@Test(priority=5,dependsOnMethods= {"selectFilters","searchForCourse"},groups= {"Functional","Regression"},description="Wait For Courses and Start Get course Details")
	void waitForCourses() {
		test = report.startTest("Course Details");
		courses = waitForVisibilityOfElements("CoursesDisplayed_Xpath", "Cousrses", 30);
		
		}
	@Test(priority=6,dependsOnMethods= {"waitForCourses","selectFilters","searchForCourse"},groups= {"Functional","Regression"},description="Click On First Course")
	void clickFirstCourse() {
		//test= report.startTest("Click First Course");

		 
		
		clickElement(courses.get(0), "First Course");
		switchToNewWindow();
		}
	@Test(priority=7,dependsOnMethods= {"selectFilters","searchForCourse","clickFirstCourse"},groups= {"Functional"},description="Print First Course Details")
	void printFirstCourseDetails() {
		//test= report.startTest("Print First Course Details");
		getCourseDetails("First Course");
		closeBrowser();
		
		switchToParentWindow();
	}
	@Test(priority=8,dependsOnMethods= {"waitForCourses","selectFilters","searchForCourse"},groups= {"Functional"},description="Click on Second Course")
	void clickSecondCourse() {
		//test= report.startTest("Click Second Course");
		//List<WebElement> listElements = waitForVisibilityOfElements("ListElements_Xpath", "List Elements", 30);
		
		
		clickElement(courses.get(1), "Second Course");
		switchToNewWindow();
	}
	@Test(priority=9,dependsOnMethods= {"waitForCourses","selectFilters","searchForCourse"},groups= {"Functional"},description="Print Second Course details")
	void printSecondCourseDetails() {
		getCourseDetails("Second Course");
		closeBrowser();
		
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
