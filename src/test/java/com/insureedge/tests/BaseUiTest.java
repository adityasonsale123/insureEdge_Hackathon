//package com.insureedge.tests;

package com.insureedge.tests;

import com.insureedge.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.ResourceBundle;

public abstract class BaseUiTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ResourceBundle prop;

    @BeforeClass
    public void baseSetup() {
        prop = ResourceBundle.getBundle("config");
        driver = new ChromeDriver(); 
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterClass(alwaysRun = true)
    public void baseTeardown() {
        if (driver != null) driver.quit();
    }

    protected String mustGet(String key) {
        String v = get(key);
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return v.trim();
    }

    protected String get(String key) {
        try { return prop.getString(key); } catch (Exception e) { return ""; }
    }

    protected String getOrDefault(String key, String def) {
        String v = get(key);
        return (v == null || v.trim().isEmpty()) ? def : v.trim();
    }

    protected void loginIfNeeded() {
        String loginUrl = mustGet("loginUrl");
        String user = mustGet("adminUser");
        String pass = mustGet("adminPass");
        String dashboardUrl = get("dashboardUrl");

        LoginPage login = new LoginPage(driver, wait);
        login.open(loginUrl);

        if (login.isAt()) {
            login.login(user, pass);
        }
        if (dashboardUrl != null && !dashboardUrl.trim().isEmpty()) {
            driver.get(dashboardUrl.trim());
        }
    }
}
