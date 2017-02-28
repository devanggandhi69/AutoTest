import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;


public class TC7and8 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeClass(alwaysRun = true)
  public void setUp() throws Exception {
	System.setProperty("webdriver.chrome.driver","C:\\Users\\Cathy\\Downloads\\eclipse-jee-mars-R-win32-x86_64\\chromedriver.exe");
    driver = new ChromeDriver();
    baseUrl = "https://www.amazon.com";
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
  }

  @Test
  public void testTc7and8() throws Exception {
    //TC7
	driver.get(baseUrl + "/");
    driver.findElement(By.id("twotabsearchtextbox")).clear();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Dell Laptop");
    driver.findElement(By.cssSelector("input.nav-input")).click();
    driver.findElement(By.partialLinkText("Dell Latitude E5450")).click();
    driver.findElement(By.id("mbb-offeringID-1")).click();
    driver.findElement(By.id("add-to-cart-button")).click();
    assertEquals(driver.findElement(By.cssSelector("h1.a-size-medium.a-text-bold")).getText(), "Added to Cart");
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    
    //TC8
    driver.findElement(By.cssSelector("span.nav-cart-icon.nav-sprite")).click();
    assertTrue((driver.findElements(By.partialLinkText("Dell Latitude E5450")).size() > 0) ? true:false);
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
