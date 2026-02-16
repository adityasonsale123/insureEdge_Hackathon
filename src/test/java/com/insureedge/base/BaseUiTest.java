package com.insureedge.base;

import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class BaseUiTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties config = new Properties();

    @BeforeClass(alwaysRun = true)
    public void baseSetup() {
        loadConfig();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        System.out.println("[STEP] Browser launched");
    }

    @AfterClass(alwaysRun = true)
    public void baseTeardown() {
        if (driver != null) {
            driver.quit();
            System.out.println("[PASS] Browser closed");
        }
    }

    protected void loadConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            config.load(is);
            System.out.println("[STEP] Loaded config.properties");
        } catch (Exception e) {
            throw new RuntimeException("config.properties not found", e);
        }
    }

    /** Optional helper if tests need you to be logged in. */
    // BaseUiTest.java (minimal change)
    // BaseUiTest.java  (minimal, drop-in)
    protected void loginIfNeeded() {
        String loginUrl     = config.getProperty("login.url", "").trim();
        String user         = config.getProperty("login.username", "").trim();
        String pass         = config.getProperty("login.password", "").trim();
        String dashboardUrl = config.getProperty("dashboard.url", "").trim();

        if (!loginUrl.isEmpty()) {
            com.insureedge.pages.LoginPage lp = new com.insureedge.pages.LoginPage(driver, wait);
            lp.open(loginUrl);
            if (lp.isAt()) {
                System.out.println("[STEP] On Login page. Attempting login...");
                lp.login(user, pass);
                System.out.println("[PASS] Login attempted");
            }
        } else {
            System.out.println("[INFO] login.url not set. Skipping login.");
        }

        // Navigate to dashboard + fallback if 404 or counters not present
        goToDashboard(dashboardUrl);
    }

    private void goToDashboard(String preferredUrl) {
        String current = driver.getCurrentUrl();
        String[] candidates = new String[] {
                emptyToNull(preferredUrl),
                // hard fallback (correct path)
                "https://qeaskillhub.cognizant.com/AdminDashboard",
                // auto-fix a wrongly configured /Admin/Dashboard → /AdminDashboard
                current.replace("/Admin/Dashboard", "/AdminDashboard")
        };

        for (String url : candidates) {
            if (url == null || url.trim().isEmpty()) continue;
            driver.get(url);
            System.out.println("[STEP] Navigated to: " + url);

            if (isIIS404() || !isDashboardVisible()) {
                System.out.println("[INFO] Not a valid dashboard (404 or counters missing), trying next candidate...");
                continue;
            }
            System.out.println("[PASS] Dashboard visible at: " + url);
            return;
        }

        // If you prefer skip instead of fail, throw new SkipException(...) here
        throw new RuntimeException("Dashboard not reachable. Tried candidates; last URL: " + driver.getCurrentUrl());
    }

    private boolean isDashboardVisible() {
        // Check one well-known counter
        return !driver.findElements(By.id("ContentPlaceHolder_Admin_lblRegisteredUsers")).isEmpty();
    }

    private boolean isIIS404() {
        // IIS classic 404; adjust if your server shows different text
        String title = driver.getTitle();
        String src   = driver.getPageSource();
        return (title != null && title.contains("404")) ||
                (src != null && src.contains("404 - File or directory not found"));
    }

    private static String emptyToNull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }
}