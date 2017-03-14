import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ScreenShotDataProvider {

	public Logger logger = Logger.getLogger(ScreenShotDataProvider.class);
	WebDriver driver;

	// Screen Capture Code
	static void captureScreenshot(WebDriver adriver) {
		File src = ((TakesScreenshot) adriver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(src, new File("E:/TestCode/FirstTestCase/image/" + System.currentTimeMillis() + ".png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	// Fatching Data From xlsx File
	public static String[][] getExcelData() throws Exception {
		String[][] arrayExcelData = null;
		try {

			String FilePath = "C:\\NPU Studies\\CS595\\DataProvider.xlsx";

			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(FilePath);

			// Access the required test data sheet
			XSSFWorkbook ExcelWBook = new XSSFWorkbook(ExcelFile);
			XSSFSheet Sheet1 = ExcelWBook.getSheetAt(0);
			int totalNoOfRows = Sheet1.getPhysicalNumberOfRows();
			int totalNoOfCols = 2;
			arrayExcelData = new String[totalNoOfRows][totalNoOfCols];
			for (int i = 0; i < totalNoOfRows; i++)
				for (int j = 0; j <= 1; j++) {
					arrayExcelData[i][j] = Sheet1.getRow(i).getCell(j).getStringCellValue();
				}
			ExcelWBook.close();
			return arrayExcelData;
		} catch (Exception e) {
			throw (e);
		}
	}

	@DataProvider(name = "excelData")
	public Object[][] loginData() throws Exception {
		Object[][] arrayObject = new Object[3][2];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				arrayObject = getExcelData();
			}
		}
		return arrayObject;
	}

	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "E:\\TestCode\\Automation_Selenium\\JarFile\\chromedriver.exe");
		driver = new ChromeDriver();
		String baseUrl = "https://www.amazon.com";
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Test(dataProvider = "data")

	public void tc_1_automated(String keyword) {
		System.setProperty("webdriver.chrome.driver", "E:\\TestCode\\Automation_Selenium\\JarFile\\chromedriver.exe");
		driver = new ChromeDriver();
		String baseURl = "https://www.amazon.com/";
		driver.get(baseURl);
		// driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(keyword);
		driver.findElement(By.xpath(".//*[@id='nav-search']/form/div[2]/div/input")).click();
		String ActualValue = driver.findElement(By.cssSelector(".a-color-state.a-text-bold")).getText();
		String expectedValue = keyword;
		ActualValue = ActualValue.replace("\"", "");
		Assert.assertTrue(expectedValue.equals(ActualValue), "Values matched");
		logger.info("Asserting first test case here");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.close();
		driver.quit();
	}

	@DataProvider(name = "data")

	public static Object[][] getData(Method name) {
		Object[][] obj = null;

		if (name.getName().equalsIgnoreCase("tc_1_automated")) {
			obj = new Object[3][1];
			obj[0][0] = "car";
			obj[1][0] = "amazon echo";
			obj[2][0] = "apple watch";
		}

		if (name.getName().equalsIgnoreCase("tc_2_automated")) {
			obj = new Object[1][1];
			obj[0][0] = "Dell laptops";
		}
		return obj;
	}

	@Test(dataProvider = "data")

	public void tc_2_automated(String keyword) {
		try {
			System.setProperty("webdriver.chrome.driver",
					"E:\\TestCode\\Automation_Selenium\\JarFile\\chromedriver.exe");
			driver = new ChromeDriver();
			String baseURl = "https://www.amazon.com/";
			driver.get(baseURl);
			// driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.findElement(By.id("twotabsearchtextbox")).sendKeys(keyword);
			driver.findElement(By.xpath(".//*[@id='nav-search']/form/div[2]/div/input")).click();
			List<WebElement> search = driver
					.findElements(By.xpath(".//*[@id='result_0']/div/div/div/div[2]/div[3]/div[1]/a/h2"));
			String ActualValue = search.get(0).getText();
			System.out.println("Actual value:" + ActualValue);
			search.get(0).click();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0,1000)", "");
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			String expectedValue = driver.findElement(By.xpath(".//*[@id='title']")).getText();
			System.out.println("Expected value:" + expectedValue);
			logger.info("Asserting second test case here");
			Assert.assertTrue(expectedValue.equals(ActualValue), "Values matched");
			driver.quit();
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
			// System.out.println("\n\n" + e.getMessage());
		}
	}

	@Test(dataProvider = "excelData")

	public void tc_3_automated(String login, String password) {
		try {
			System.setProperty("webdriver.chrome.driver",
					"E:\\TestCode\\Automation_Selenium\\JarFile\\chromedriver.exe");
			driver = new ChromeDriver();
			String baseURl = "https://www.amazon.com/";
			driver.get(baseURl);
			// driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			driver.findElement(By.xpath(".//*[@id='nav-link-accountList']/span[1]")).click();
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			driver.findElement(By.xpath(".//*[@id='ap_email']")).sendKeys(login);
			driver.findElement(By.xpath(".//*[@id='ap_password']")).sendKeys(password);
			driver.findElement(By.xpath(".//*[@id='signInSubmit']")).click();
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			boolean titleTextfield = driver.findElement(By.xpath(".//*[@id='auth-warning-message-box']/div"))
					.isDisplayed();
			Assert.assertTrue(titleTextfield, "Title text field present which is not expected");
			driver.quit();
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
			// System.out.println("\n\n" + e.getMessage());
		}
	}

	@AfterClass

	public void closebrowser() {
		try {

			// Enter your file path
			URI uri = new URI("E:/TestCode/FirstTestCase/test-output/index.html");
			Desktop.getDesktop().browse(uri);
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
			
		}
		String host = "smtp.gmail.com";
		String port = "587";
		String mailFrom = "your-email-address";
		String password = "your-email-password";

		// message info
		String mailTo = "your-friend-email";
		String subject = "New email with attachments";
		String message = "I have some attachments for you.";

		// attachments
		String[] attachFiles = new String[1];
		attachFiles[0] = "E:/TestCode/FirstTestCase/test-output/index.html";
		attachFiles[1] = "E:/TestCode/FirstTestCase/test-output/emailable-report.html";

		try {
			EmailAttachmentSender.sendEmailWithAttachments(host, port, mailFrom, password, mailTo, subject,
					message, attachFiles);
			logger.info("\n\n Email Sent");
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
			// System.out.println("\n\n" + e.getMessage());
		}
		driver.close();
	}

}
