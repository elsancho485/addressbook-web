package ru.stqa.ptf.addressbook.tests;

import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;
import ru.stqa.ptf.addressbook.appmanager.ApplicationManager;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.testng.Assert.fail;

public class TestBase {

  Logger logger = LoggerFactory.getLogger(TestBase.class);

  protected static final ApplicationManager app
          = new ApplicationManager(System.getProperty("browser", BrowserType.FIREFOX));

  @BeforeSuite(alwaysRun = true)
  public void setUp() throws Exception {
    app.init();
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() throws Exception {
    app.stop();
    String verificationErrorString = app.verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  @BeforeMethod(alwaysRun = true)
  public void logTestStart(Method m, Object[] p) {
    logger.info("Start test " + m.getName() + " with parameteres " + Arrays.asList(p));
  }

  @AfterMethod(alwaysRun = true)
  public  void  logTestStop(Method m) {
    logger.info("Stop test " + m.getName());
  }

}
