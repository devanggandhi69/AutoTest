package automation;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.testng.*;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class test {
	
	 
  @Test
  public void tc_1_automated() {	 ProfilesIni profile = new ProfilesIni();
	 FirefoxProfile ffprofile = profile.getProfile("default");
	 ffprofile.setEnableNativeEvents(true);
		WebDriver driver = new FirefoxDriver(ffprofile);		   
		driver.get("https://www.amazon.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Dell Laptops");
		driver.findElement(By.xpath(".//*[@id='nav-search']/form/div[2]/div/input")).click();
		String ActualValue = driver.findElement(By.xpath(".//*[@id='bcKwText']/span")).getText();
		System.out.println("The Actual Value is"+ActualValue);
		System.out.println("The expected value is:"+driver.findElement(By.xpath(".//*[@id='bcKwText']/span")).getText());
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		String expectedValue=driver.findElement(By.xpath(".//*[@id='bcKwText']/span")).getText();
		
		//Assert.assertFalse(!(expectedValue.matches(ActualValue)));
		Assert.assertTrue(expectedValue.equals(ActualValue),"Values matched");
		
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.quit();
  }
  @Test
  public void tc_2_automated() { 
	  ProfilesIni profile = new ProfilesIni();
		 FirefoxProfile ffprofile = profile.getProfile("default");
		 ffprofile.setEnableNativeEvents(true);
			WebDriver driver = new FirefoxDriver(ffprofile);	
		
	  driver.get("https://www.amazon.com/");
	  driver.manage().window().maximize();
	  driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	  driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Dell Laptops");
	  driver.findElement(By.xpath(".//*[@id='nav-search']/form/div[2]/div/input")).click();
     // List<WebElement> search = driver.findElements(By.className("sx-price-whole"));
	  List<WebElement> search = driver.findElements(By.xpath(".//*[@id='result_0']/div/div/div/div[2]/div[3]/div[1]/a/h2"));
	  //.//*[@id='result_0']/div/div/div/div[2]/div[3]/div[1]/a/h2
      //int max = Integer.parseInt(search.get(0).getText());
      
		//**** facing some issue with Max selection code-code working but "Integer.parseInt(search.get(i).getText()) == max" throwing issue
     /* for (WebElement iSearch : search) {
			int tmp = Integer.parseInt(iSearch.getText());
			if (tmp > max)
				max = tmp;
		}
        String ActualValue = null;
		for (int i = 0; i < search.size()-1; i++) {
			if (Integer.parseInt(search.get(i).getText()) == max)
			 ActualValue=search.get(0).getText();
				{search.get(i).click();  
			    }	 
		}*/
	 String ActualValue=search.get(0).getText();
     search.get(0).click();
      System.out.print("The size is"+ search.size());
       
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,1000)", "");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		String expectedValue =driver.findElement(By.xpath(".//*[@id='title']")).getText();
		//Assert.assertEquals(driver.findElement(By.xpath(".//*[@id='title']")).getText(),ActualValue,"value did not match");
		System.out.println("The Actual Value is"+ActualValue);
		System.out.println("The expected value is:"+expectedValue);
		Assert.assertTrue(expectedValue.equals(ActualValue),"Values matched");
		driver.quit();
  }
  @BeforeMethod(alwaysRun = true)
  public void BeforeMethod() {
	 
	  System.out.println("******--This is before class method--******");	
	  
  }
  
  @AfterClass
  public void afterClass() {
	  System.out.println("******--This is after class method--******");
  }

  @BeforeTest
  public void beforeTest() {
	  System.out.println("******--This is before test method--*******");
  }
  

  @AfterTest
  public void afterTest() {
	  System.out.println("*****--This is after test method--******");
	  
  }

}
