package com.insureedge.tests.sounak;

import com.insureedge.base.BaseUiTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Locale;
import java.util.Set;

/**
 * US17P3_13
 *
 * Scenario:
 * 1) Open Login page
 * 2) Enter username/password, select Remember Me, Login
 * 3) Handle same-tab or new-tab navigation
 * 4) Click avatar/profile -> Sign Out
 * 5) Open a new tab and go to AdminDashboard
 * 6) Verify "⛔ Access Denied" is shown
 */
public class US17P3_13 extends BaseUiTest {

    // --- Locators (prefer robust selectors with normalize-space) ---
    private static final By USERNAME = By.id("txtUsername");
    private static final By PASSWORD = By.id("txtPassword");
    private static final By REMEMBER_ME = By.id("rememberMe");
    private static final By LOGIN_BTN = By.id("BtnLogin");

    // Avatar/profile button (provided path; keep as-is, add fallback)
    private static final By AVATAR_IMG = By.xpath("//*[@id='header']/nav/ul/li[2]/a/img");
    // Sign out menu item (robust text match)
    private static final By SIGN_OUT = By.xpath("//span[normalize-space()='Sign Out']");

    // Post-logout access denied
    private static final By ACCESS_DENIED = By.xpath("//*[normalize-space(text())='⛔ Access Denied']");

    private String getLoginUrl() {
        String url = config.getProperty("login.url", "").trim();
        return url.isEmpty() ? "https://qeaskillhub.cognizant.com/LoginPage?logout=true" : url;
    }

    private String getDashboardUrl() {
        String url = config.getProperty("dashboard.url", "").trim();
        return url.isEmpty() ? "https://qeaskillhub.cognizant.com/AdminDashboard" : url;
    }

    private String getUsername() {
        return config.getProperty("login.username", "").trim();
    }

    private String getPassword() {
        return config.getProperty("login.password", "").trim();
    }

    @Test(description = "Remember Me: login, logout, and verify Access Denied when revisiting AdminDashboard")
    public void rememberMe() {
        // Step 1: Open login page
        String loginUrl = getLoginUrl();
        driver.get(loginUrl);
        System.out.println("[STEP] Opened Login URL: " + loginUrl);

        // Step 2: Enter credentials + remember me + login
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME)).clear();
        driver.findElement(USERNAME).sendKeys(getUsername());

        wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD)).clear();
        driver.findElement(PASSWORD).sendKeys(getPassword());

        // Remember Me (if present)
        try {
            WebElement remember = driver.findElement(REMEMBER_ME);
            if (!remember.isSelected()) {
                remember.click();
                System.out.println("[STEP] Remember Me selected.");
            } else {
                System.out.println("[STEP] Remember Me already selected.");
            }
        } catch (NoSuchElementException ignored) {
            System.out.println("[INFO] Remember Me checkbox not present; continuing.");
        }

        driver.findElement(LOGIN_BTN).click();
        System.out.println("[STEP] Login button clicked.");

        // Step 3: Handle possible new tab after login (or same-tab)
        String originalWindow = driver.getWindowHandle();
        Set<String> beforeHandles = driver.getWindowHandles();

        // Wait a moment for navigation or new tab
        new WebDriverWait(driver, Duration.ofSeconds(6)).until(d -> driver.getWindowHandles().size() >= beforeHandles.size());

        Set<String> afterHandles = driver.getWindowHandles();
        if (afterHandles.size() > beforeHandles.size()) {
            for (String h : afterHandles) {
                if (!beforeHandles.contains(h)) {
                    driver.switchTo().window(h);
                    System.out.println("[STEP] Switched to new tab after login: " + h);
                    break;
                }
            }
        } else {
            // Same tab: wait until we're no longer on the login page (best effort)
            try {
                new WebDriverWait(driver, Duration.ofSeconds(8)).until(d ->
                        !driver.getCurrentUrl().toLowerCase(Locale.ROOT).contains("loginpage"));
            } catch (TimeoutException te) {
                System.out.println("[INFO] URL did not change away from login quickly; proceeding to attempt logout.");
            }
        }

        // Step 4: Click avatar/profile and Sign Out
        // First, make avatar clickable (fallback to waiting existence + JS click)
        WebElement avatar = null;
        try {
            avatar = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.elementToBeClickable(AVATAR_IMG));
            avatar.click();
        } catch (Exception e) {
            System.out.println("[INFO] Direct click on avatar failed; trying JS click if element is present.");
            try {
                avatar = driver.findElement(AVATAR_IMG);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", avatar);
            } catch (Exception ex) {
                throw new RuntimeException("Unable to open profile/menu for Sign Out.", ex);
            }
        }

        // Now click Sign Out
        try {
            WebElement signOut = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.elementToBeClickable(SIGN_OUT));
            signOut.click();
            System.out.println("[STEP] Clicked Sign Out.");
        } catch (TimeoutException te) {
            throw new RuntimeException("Sign Out option not visible/clickable.", te);
        }

        // Step 5: Open a new tab and navigate to AdminDashboard
        String dashboardUrl = getDashboardUrl();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(dashboardUrl);
        System.out.println("[STEP] Navigated to Admin Dashboard after logout: " + dashboardUrl);

        // Step 6: Verify "⛔ Access Denied"
        WebElement denied = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(ACCESS_DENIED));
        String actual = denied.getText().trim();
        String expected = "⛔ Access Denied";
        Assert.assertEquals(actual, expected, "Access control after logout is not enforced as expected.");

        System.out.println("[PASS] Verified Access Denied after logout when visiting AdminDashboard.");
    }
}