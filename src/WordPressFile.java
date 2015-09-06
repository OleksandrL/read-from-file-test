import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WordPressFile {
  private WebDriver driver;
  private String baseUrl;
  private StringBuffer verificationErrors = new StringBuffer();
  private String testFile;

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://demo.opensourcecms.com/wordpress/wp-login.php";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    testFile = System.getProperty("user.dir") + "\\test_data.txt";
    
  }

  @Test
  public void testWordPress() throws Exception {
	driver.get(baseUrl);
	
	FileReader FR = new FileReader(testFile);
	BufferedReader BR = new BufferedReader(FR);
	String content = "";
	int i = 0;
	String [] validData = new String[2];
	while((content = BR.readLine())!= null){
		if((content.contains("Valid login")||(content.contains("Valid password")))){
			String [] words = content.split(": ");
			validData[i] = words[words.length-1];
			i++;
		}
	}
	BR.close();
	FR.close();
	
    driver.findElement(By.id("user_login")).clear();
    driver.findElement(By.id("user_login")).sendKeys(validData[0]);
    driver.findElement(By.id("user_pass")).clear();
    driver.findElement(By.id("user_pass")).sendKeys(validData[1]);
    driver.findElement(By.id("wp-submit")).click();
    
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Howdy, admin[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
    driver.findElement(By.xpath("//li[@id='menu-posts']/a")).click();
    driver.findElement(By.cssSelector("a.add-new-h2")).click();
    driver.findElement(By.id("title")).clear();
    driver.findElement(By.id("title")).sendKeys("Selenium Demo Post");
    driver.findElement(By.id("publish")).click();
    
    // Warning: verifyTextPresent may require manual changes
    try {
      assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Post published\\.[\\s\\S]*$"));
    } catch (Error e) {
      verificationErrors.append(e.toString());
    }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
}
