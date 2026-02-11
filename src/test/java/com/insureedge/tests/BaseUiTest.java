////package com.insureedge.tests;
//
//package com.insureedge.tests;
//
//import com.insureedge.pages.LoginPage;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.*;
//import org.testng.annotations.*;
//
//import java.time.Duration;
//import java.util.ResourceBundle;
//
//public abstract class BaseUiTest {
//
//    protected WebDriver driver;
//    protected WebDriverWait wait;
//    protected ResourceBundle prop;
//
//    @BeforeClass
//    public void baseSetup() {
//        prop = ResourceBundle.getBundle("config");
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//        driver.manage().window().maximize();
//        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//    }
//
//    @AfterClass(alwaysRun = true)
//    public void baseTeardown() {
//        if (driver != null) driver.quit();
//    }
//
//    protected String mustGet(String key) {
//        String v = get(key);
//        if (v == null || v.trim().isEmpty()) {
//            throw new IllegalStateException("Missing required property: " + key);
//        }
//        return v.trim();
//    }
//
//    protected String get(String key) {
//        try { return prop.getString(key); } catch (Exception e) { return ""; }
//    }
//
//    protected String getOrDefault(String key, String def) {
//        String v = get(key);
//        return (v == null || v.trim().isEmpty()) ? def : v.trim();
//    }
//
//    protected void loginIfNeeded() {
//        String loginUrl = mustGet("loginUrl");
//        String user = mustGet("adminUser");
//        String pass = mustGet("adminPass");
//        String dashboardUrl = get("dashboardUrl");
//
//        LoginPage login = new LoginPage(driver, wait);
//        login.open(loginUrl);
//
//        if (login.isAt()) {
//            login.login(user, pass);
//        }
//        if (dashboardUrl != null && !dashboardUrl.trim().isEmpty()) {
//            driver.get(dashboardUrl.trim());
//        }
//    }
//}


package com.insureedge.tests;

import com.insureedge.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public abstract class BaseUiTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ResourceBundle bundle;  // primary source
    protected Properties props;       // fallback + overrides

    @BeforeClass
    public void baseSetup() {
        // 1) Load ResourceBundle "config" from classpath (src/test/resources or src/main/resources)
        try {
            // Honor system locale (Jenkins logs show en_IN). If no locale-specific bundle, it will fall back to config.properties.
            bundle = ResourceBundle.getBundle("config", Locale.getDefault());
        } catch (MissingResourceException e) {
            bundle = null;
        }

        // 2) Load fallback config.properties if bundle not present (or to allow overrides)
        props = new Properties();
        // First try: classpath /config.properties
        try (InputStream cp = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("config.properties")) {
            if (cp != null) {
                props.load(cp);
            }
        } catch (Exception ignored) {}

        // Second try: workspace-relative path (useful during local runs or if classpath packaging fails)
        if (props.isEmpty()) {
            try (InputStream fs = Files.newInputStream(Paths.get("src", "test", "resources", "config.properties"))) {
                props.load(fs);
            } catch (Exception ignored) {}
        }

        // 3) Spin up Chrome (headless default for CI)
        ChromeOptions options = new ChromeOptions();
        // Read headless from config; default true for CI
        boolean headless = Boolean.parseBoolean(getOrDefault("headless", "true"));
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=" + getOrDefault("window.width", "1920")
                + "x" + getOrDefault("window.height", "1080"));
        options.addArguments("--no-sandbox");           // helpful in some CI environments
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
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
            throw new IllegalStateException("Missing required property: " + key
                    + ". Provide it in src/test/resources/config.properties or a ResourceBundle named 'config'.");
        }
        return v.trim();
    }

    protected String get(String key) {
        // Priority: ResourceBundle -> Properties -> empty
        try {
            if (bundle != null && bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        } catch (Exception ignored) {}
        String val = props.getProperty(key);
        return (val == null) ? "" : val;
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
