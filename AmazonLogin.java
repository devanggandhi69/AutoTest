package automation;

import java.util.regex.Pattern;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.internal.Nullable;

import static org.testng.Assert.*;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class AmazonLogin {
	private WebDriver driver;

	@BeforeClass
	public void urldriver() {
		driver = new FirefoxDriver();

		String appUrl = "https://www.amazon.com/";
		driver.get("https://www.amazon.com/");

	}

	@Test(priority = 1)
	public void amazonLogin() {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		String expectedTitle = "Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more";
		String actualTitle = driver.getTitle();
		Assert.assertEquals(expectedTitle, actualTitle);
	
	}

	@Test(priority = 2)
	public void amazonuser() {
		WebElement signin = driver.findElement(By.className("nav-action-button"));
		signin.click();
		String expectedTitle = "Amazon Sign In";
		String actualTitle = driver.getTitle();
		Assert.assertEquals(expectedTitle, actualTitle);
		AmazonLogin.captureScreenshot(driver);
		WebElement username = driver.findElement(By.id("ap_email"));
		username.clear();
		username.sendKeys("Your Email ID");
		WebElement pass = driver.findElement(By.id("ap_password"));
		pass.clear();
		pass.sendKeys("Your Password");
		driver.findElement(By.id("signInSubmit")).click();
	}

	@Test(priority = 3)
	public void keySearch() {
		WebElement search = driver.findElement(By.id("twotabsearchtextbox"));
		search.clear();
		search.sendKeys("Dell Laptop");
		WebElement searchKey = driver.findElement(By.className("nav-input"));
		searchKey.click();
	}

	@Test(priority = 4)
	public void searchPage() {

		new Select(driver.findElement(By.id("sort"))).selectByVisibleText("Price: High to Low");
		driver.findElement(By.cssSelector("option[value=\"price-desc-rank\"]")).click();
	}

	@Test(priority = 5)
	public void searchKeyWords() {
		driver.findElement(By.name("askQuestionText")).sendKeys("laptop heat");
		driver.findElement(By.className("a-icon a-icon-search")).click();

	}
	
	  

	@AfterClass
	public void closebrowser() {

		try {

			// Enter your file path
			URI uri = new URI("C:/Heartyy/jan/Automation_Selenium/automation/index.html");
			Desktop.getDesktop().browse(uri);

		} catch (Exception e) {
			System.out.println("\n\n" + e.getMessage());
		}
		driver.close();
	}
	 private static void captureScreenshot(WebDriver adriver) {
		  File src=((TakesScreenshot)adriver).getScreenshotAs(OutputType.FILE);
		  try{
			  FileUtils.copyFile(src, new File("C:/Heartyy/jan/Automation_Selenium/automation/"+ System.currentTimeMillis()+".png"));
		  }
			  catch (IOException e)
			  {
			   System.out.println(e.getMessage());
			  }
		  }
		  

}
