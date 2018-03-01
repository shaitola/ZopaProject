package infra;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;


/**
 * Created by shay.toledano on 2/28/18.
 */
public class SeleniumWebDriver {
    private WebDriver driver;

    public SeleniumWebDriver() {
        String browserName = System.getProperty("selenium.web.browser");
        openBrowser(browserName.toLowerCase());

        //System.setProperty("webdriver.chrome.driver", "/Users/shay.toledano/Documents/tmp/project/selenium/etc/webdriver/chromedriver");
        //driver = new ChromeDriver();
    }

    public void navigate(String url) {
        driver.navigate().to(url);
    }

    public void close() {
        driver.close();
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    public void click(By by) {
        driver.findElement(by).click();
    }

    public void sendkey(By by, String data) {
        driver.findElement(by).sendKeys(data);
    }

    public void scrollDown(){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,500)", "");
    }

    public void maximizeWindow(){
        driver.manage().window().maximize();
    }

    public void openBrowser(String browserType) {
        switch (browserType) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "ie":
                driver = new InternetExplorerDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                System.out.println("browser : " + browserType + " is invalid, Launching Firefox as browser of choice..");
                driver = new FirefoxDriver();
        }
    }

    public void waitinsecons(String time){
        int i = Integer.parseInt(time);
        driver.manage().timeouts().pageLoadTimeout(i, TimeUnit.SECONDS);
    }

    public boolean isServerAlive()
    // To check if server is reachable
    {
        String url = Locator.homePageUrl;

        try {
            InetAddress.getByName(url).isReachable(3000); //Replace with your name
            return true;
        } catch (Exception e) {
            System.out.println("Site is down");
            return false;
        }
    }


}
