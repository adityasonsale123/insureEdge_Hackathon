package com.insureedge.base;

import com.insureedge.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public abstract class BaseUiTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties config = new Properties();

    // Runs before every test class
    @Parameters("browser")
    @BeforeClass
    public void setup(@Optional("chrome") String browser) {

        loadConfig();  // Load config file once

        // Choose browser based on XML
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } 
        else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } 
        else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } 
        else {
            throw new RuntimeException("Browser not supported: " + browser);
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        }

    // Runs after every test class
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed");
        }
    }

    // Load config.properties file
    private void loadConfig() {
        try (InputStream file = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            config.load(file);
            System.out.println("Config file loaded");
        } catch (Exception e) {
            throw new RuntimeException("config.properties not found");
        }
    }

    // Login (only if login URL is available)
    protected void loginIfNeeded() {
        String loginUrl = config.getProperty("login.url", "");
        String username = config.getProperty("login.username", "");
        String password = config.getProperty("login.password", "");
        String dashboardUrl = config.getProperty("dashboard.url", "");

        // If login url is not empty → perform login
        if (!loginUrl.isEmpty()) {
            LoginPage login = new LoginPage(driver, wait);
            login.open(loginUrl);
            login.login(username, password);
            System.out.println("Logged in successfully");
        }

        // Go to dashboard
        if (!dashboardUrl.isEmpty()) {
            driver.get(dashboardUrl);
        }
    }
}