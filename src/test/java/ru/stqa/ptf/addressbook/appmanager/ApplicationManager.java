package ru.stqa.ptf.addressbook.appmanager;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

  private final Properties properties;
  protected WebDriver driver;

  private SessionHelper sessionHelper;

  public ContactHelper contactHelper;

  private  NavigationHelper navigationHelper;

  private GroupHelper groupHelper;

  public StringBuffer verificationErrors = new StringBuffer();

  private String baseUrl;

  private boolean acceptNextAlert = true;

  private String browser;

  public ApplicationManager(String browser) {
    this.browser = browser;
    properties = new Properties();
  }


  public void init() throws IOException {
    String target = System.getProperty("target", "local");
    properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties",target))));

    if (browser.equals(BrowserType.FIREFOX)) {driver = new FirefoxDriver();}
    else if ((browser.equals(BrowserType.CHROME))) {driver = new ChromeDriver();}
    else if ((browser.equals(BrowserType.SAFARI))) {driver = new SafariDriver();}
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    driver.get(properties.getProperty("web.baseUrl"));
    groupHelper = new GroupHelper(driver);
    navigationHelper = new NavigationHelper(driver);
    sessionHelper = new SessionHelper(driver);
    contactHelper = new ContactHelper(driver);
    sessionHelper.login(properties.getProperty("web.adminLogin"), properties.getProperty("web.adminPassword"));
  }

  public void stop() {
    driver.quit();
  }

  public boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
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

  public GroupHelper group() {
    return groupHelper;
  }

  public NavigationHelper goTo() { return navigationHelper; }

  public ContactHelper contact() { return contactHelper; }
}
