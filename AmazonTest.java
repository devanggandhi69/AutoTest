import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.*;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.Select;

public class AmazonTest {

	private WebDriver driver;
	public static Logger logger = Logger.getLogger(ScreenShotDataProvider.class);
	static InputStream pathStream = AmazonTest.class.getClassLoader().getResourceAsStream("SetPath.properties");
	InputStream emailStream = AmazonTest.class.getClassLoader().getResourceAsStream("Email.properties");

	public static String[][] getExcelData() throws Exception {
		String[][] arrayExcelData = null;
		try {
			Properties PathProperties = new Properties();
			PathProperties.load(pathStream);

			String FilePath = PathProperties.getProperty("XMLFilePath");
			FileInputStream ExcelFile = new FileInputStream(FilePath);
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

	static void captureScreenshot(WebDriver adriver) {
		File src = ((TakesScreenshot) adriver).getScreenshotAs(OutputType.FILE);
		try {
			Properties PathProperties = new Properties();
			String ssfile = PathProperties.getProperty("SSFile");
			FileUtils.copyFile(src, new File(ssfile + System.currentTimeMillis() + ".png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
		}

	}

//	@BeforeClass
//
//	public void urldriver() {
//		try {
//			driver = new FirefoxDriver();
//			String appUrl = "https://www.amazon.com/";
//			driver.get(appUrl);
//		} catch (Exception e) {
//			logger.info("\n\n" + e.getMessage());
//
//		}
//
//	}

	// Data Provider
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
	public void keyWordSearching(String keyword) throws IOException {

		try {
			Properties PathProperties = new Properties();
			PathProperties.load(pathStream);
			String DriverPath = PathProperties.getProperty("ChromeDrivePath");
			System.setProperty("webdriver.chrome.driver", DriverPath);
			WebDriver driver = new ChromeDriver();
			String baseURl = "https://www.amazon.com/";
			// System.out.print("baseURl");
			driver.get(baseURl);
			driver.manage().window().maximize();
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
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
		}

	}

	@Test(dataProvider = "data")
	public void valueSearch(String keyword) throws IOException {
		try {
			Properties PathProperties = new Properties();
			PathProperties.load(pathStream);
			String DriverPath = PathProperties.getProperty("ChromeDrivePath");
			System.setProperty("webdriver.chrome.driver", DriverPath);
			WebDriver driver = new ChromeDriver();

			String baseURl = "https://www.amazon.com/";
			driver.get(baseURl);
			driver.manage().window().maximize();
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
		}
	}

	@Test(dataProvider = "excelData")
	public void logInTest(String login, String password) throws IOException {
		try {
			Properties PathProperties = new Properties();
			PathProperties.load(pathStream);
			String DriverPath = PathProperties.getProperty("ChromeDrivePath");
			System.setProperty("webdriver.chrome.driver", DriverPath);
			WebDriver driver = new ChromeDriver();
			String baseURl = "https://www.amazon.com/";
			driver.get(baseURl);
			driver.manage().window().maximize();
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
		}
	}

	@Test(priority = 1)

	public void urlTest() throws InterruptedException {

		driver.get("https://www.amazon.com/");
		AmazonTest.captureScreenshot(driver);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Dell Laptops");
		AmazonTest.captureScreenshot(driver);
		driver.findElement(By.xpath(".//*[@id='nav-search']/form/div[2]/div/input")).click();
		AmazonTest.captureScreenshot(driver);
		List<WebElement> search = driver
				.findElements(By.xpath(".//*[@id='result_0']/div/div/div/div[2]/div[3]/div[1]/a/h2"));
		AmazonTest.captureScreenshot(driver);
		@SuppressWarnings("unused")
		String ActualValue = search.get(0).getText();
		search.get(0).click();
		System.out.print("The size is" + search.size());

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		AmazonTest.captureScreenshot(driver);
		driver.findElement(By.id("add-to-cart-button")).click();
		AmazonTest.captureScreenshot(driver);
		driver.findElement(By.id("siNoCoverage-announce")).click();
		AmazonTest.captureScreenshot(driver);
		String url = "https://www.amazon.com/gp/huc/view.html?ie=UTF8&increasedItems=C340OWSCRQA8JG&newItems=C340OWSCRQA8JG%2C1&siHasShown=1";
		String actualUrl = driver.getCurrentUrl();
		if (actualUrl.equals(url)) {
			logger.info("\n\nVerification Successful - The correct Url is opened.");
		} else {
			logger.info("\n\nVerification Failed - An incorrect Url is opened.");
			logger.info("\n\nActual URL is : " + actualUrl);
			logger.info("\n\nExpected URL is : " + url);
			AmazonTest.captureScreenshot(driver);
		}

		driver.quit();
	}

	@SuppressWarnings("unused")
	@Test
	public void dropDownMenuTest() throws InterruptedException {
		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile ffprofile = profile.getProfile("default");
		ffprofile.setEnableNativeEvents(true);
		WebDriver driver = new FirefoxDriver(ffprofile);

		driver.get("https://www.amazon.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Dell Laptops");
		driver.findElement(By.xpath(".//*[@id='nav-search']/form/div[2]/div/input")).click();

		List<WebElement> search = driver
				.findElements(By.xpath(".//*[@id='result_0']/div/div/div/div[2]/div[3]/div[1]/a/h2"));

		String ActualValue = search.get(0).getText();
		search.get(0).click();
		System.out.print("The size is" + search.size());

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		driver.findElement(By.id("add-to-cart-button")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("siNoCoverage-announce")).click();
		Thread.sleep(4000);
		driver.findElement(By.id("hlb-view-cart-announce")).click();
		driver.findElement(By.className("a-dropdown-prompt")).click();
		driver.findElement(By.id("dropdown1_3")).click();
		Thread.sleep(2000);
		driver.quit();
	}

	@Test

	public void amazonMainPageTest() {
		try {
			driver = new FirefoxDriver();
			String appUrl = "https://www.amazon.com/";
			driver.get(appUrl);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			String expectedTitle = "Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more";
			String actualTitle = driver.getTitle();
			Assert.assertEquals(expectedTitle, actualTitle);
			AmazonTest.captureScreenshot(driver);
			driver.quit();

		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());

		}

	}

	@Test

	public void specificKeySearch() {
		try {
			driver = new FirefoxDriver();
			String appUrl = "https://www.amazon.com/";
			driver.get(appUrl);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			String expectedTitle = "Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more";
			String actualTitle = driver.getTitle();
			Assert.assertEquals(expectedTitle, actualTitle);
			AmazonTest.captureScreenshot(driver);
			WebElement search = driver.findElement(By.id("twotabsearchtextbox"));
			search.clear();
			search.sendKeys("Dell Laptop");
			WebElement searchKey = driver.findElement(By.className("nav-input"));
			searchKey.click();
			AmazonTest.captureScreenshot(driver);
			driver.quit();
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
		}

	}

	@Test
	public void specificPageSearch() {
		try {
			driver = new FirefoxDriver();
			String appUrl = "https://www.amazon.com/";
			driver.get(appUrl);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			String expectedTitle = "Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more";
			String actualTitle = driver.getTitle();
			Assert.assertEquals(expectedTitle, actualTitle);
			AmazonTest.captureScreenshot(driver);
			WebElement search = driver.findElement(By.id("twotabsearchtextbox"));
			search.clear();
			search.sendKeys("Dell Laptop");
			WebElement searchKey = driver.findElement(By.className("nav-input"));
			searchKey.click();
			AmazonTest.captureScreenshot(driver);
			new Select(driver.findElement(By.id("sort"))).selectByVisibleText("Price: High to Low");
			driver.findElement(By.cssSelector("option[value=\"price-desc-rank\"]")).click();
			AmazonTest.captureScreenshot(driver);
			driver.quit();
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
		}

	}

	@Test

	public void searchKeyWordsExtend() {
		try {
			driver = new FirefoxDriver();
			String appUrl = "https://www.amazon.com/";
			driver.get(appUrl);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			String expectedTitle = "Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more";
			String actualTitle = driver.getTitle();
			Assert.assertEquals(expectedTitle, actualTitle);
			AmazonTest.captureScreenshot(driver);
			WebElement search = driver.findElement(By.id("twotabsearchtextbox"));
			search.clear();
			search.sendKeys("Dell Laptop");
			WebElement searchKey = driver.findElement(By.className("nav-input"));
			searchKey.click();
			AmazonTest.captureScreenshot(driver);
			new Select(driver.findElement(By.id("sort"))).selectByVisibleText("Price: High to Low");
			driver.findElement(By.cssSelector("option[value=\"price-desc-rank\"]")).click();
			AmazonTest.captureScreenshot(driver);
			driver.findElement(By.name("askQuestionText")).sendKeys("laptop heat");
			driver.findElement(By.className("a-icon a-icon-search")).click();
			AmazonTest.captureScreenshot(driver);
			driver.quit();
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
		}

	}

	@AfterClass

	public void closebrowser() throws Exception {
		Properties PathProperties = new Properties();
		PathProperties.load(pathStream);
		try {
			String TestOP = PathProperties.getProperty("ReportPath");
			URI uri = new URI(TestOP);
			Desktop.getDesktop().browse(uri);
		} catch (Exception e) {
			logger.info("\n\n" + e.getMessage());
		}
		Properties EmailProperties = new Properties();
		EmailProperties.load(emailStream);
		String host = "smtp.gmail.com";
		String port = "587";
		String mailFrom = EmailProperties.getProperty("mailFrom");
		String password = EmailProperties.getProperty("password");

		// message info
		String mailTo = EmailProperties.getProperty("mailTo");
		String subject = EmailProperties.getProperty("Subject");
		String message = EmailProperties.getProperty("Msg");

		// attachments
		String[] attachFiles = new String[2];
		attachFiles[0] = PathProperties.getProperty("ReportPath");
		attachFiles[1] = PathProperties.getProperty("EmailableReport");
		AmazonTest.captureScreenshot(driver);

		try {
			EmailAttachmentSender.sendEmailWithAttachments(host, port, mailFrom, password, mailTo, subject, message,
					attachFiles);
			logger.info("\n\n Email Sent");
		} catch (Exception ex) {
			logger.info("\n\n Email send Fail");
			logger.info("\n\n" + ex.getMessage());
			ex.printStackTrace();
		}
		driver.close();
	}

}
