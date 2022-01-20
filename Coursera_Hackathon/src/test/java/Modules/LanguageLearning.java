package Modules;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.BaseUI;
import TestNGListener.ListenerTest;
@Listeners(ListenerTest.class)

public class LanguageLearning extends BaseUI{
	@BeforeTest(alwaysRun=true)
	@Parameters("testname")
	public void Exreporting(String testname) {
		startExtentReport(testname);
		test = report.startTest(testname);
	}
	@Test(priority=1,description="LanguageLearning",groups={"Smoke","Functinal"})
	
	void languageLearning() {
		loadPropertiesFile();
		openBrowser("chrome");
		navigateToURL();
		WebElement searchBox = waitForElementClickable("SearchBox_Xpath", "SearchBox", 30);
		WebElement searchIcon = waitForElementClickable("SearchIcon_Xpath", "SearchIcon", 30);
		clickElement(searchBox, "SearchBox");
		enterText(searchBox, getValue("CourseSearch2"), "SearchBox");
		clickElement(searchIcon, "SearchIcon");
		
		WebElement languageFilter = waitForElementClickable("LanguageFilter_Xpath", "LanguageFilter", 30);
		clickElement(languageFilter, "LanguageFilter");
		WebElement showAll = waitForElementClickable("LanguageShowAll_Xpath", "ShowAll", 30);
		clickElement(showAll,"ShowAll");
		}
	@Test(priority=2,description="Print and Count",groups={"Functinal"},dependsOnMethods= {"languageLearning"})
	void print() {
		List<WebElement> allLanguages = findElements("AllLanguages_Xpath", "AllLanguages");
		System.out.println("Languages available are: ");
		for(WebElement language : allLanguages) {
			System.out.println(getAttributeValue(language,"Language","value"));
		}
		System.out.println("Count of languages: "+allLanguages.size());
		WebElement closeIcon = findElement("LanguagesClose_Xpath", "CloseIcon");
		clickElement(closeIcon,"CloseIcon");
		
		WebElement levelFilter = waitForElementClickable("LevelFilter_Xpath", "LevelFilter", 30);
		clickElement(levelFilter, "LevelFilter");
		List<WebElement> levels = findElements("ElementsInFilter_Xpath", "Levels");
		System.out.println("Levels available are: ");
		for(WebElement level : levels) {
			System.out.println(getAttributeValue(level,"Level","value"));
		}
		System.out.println("Count of levels: "+levels.size());
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
