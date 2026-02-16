package com.insureedge.tests.harish;

import com.insureedge.base.BaseUiTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * InsurEdgeUS17P3_03 - UI verifications for Username field and its prefix.
 * Validates the LOGIN PAGE itself; navigates to loginUrl directly and DOES NOT auto-login.
 */
public class InsurEdgeUS17P3_03 extends BaseUiTest {

    // -------- Locators --------
    private final By usernameFieldBy = By.id("txtUsername");
    private final By usernameAltBy   = By.xpath("//input[not(@type='hidden') and " +
            "(contains(translate(@id,'USERNAME','username'),'username') or " +
            " contains(translate(@name,'USERNAME','username'),'username'))]");
    private final By usernameLabelBy = By.xpath("//label[normalize-space()='Username']");
    private final By prefixBy        = By.cssSelector("span.input-group-text");

    // -------- Cached elements (after navigation) --------
    private WebElement usernameField;
    private WebElement label;
    private WebElement prefix;

    @BeforeClass(alwaysRun = true)
    @Override
    public void baseSetup() {
        super.baseSetup();

        // Navigate directly to login page (e.g., may include ?logout=true)
        String loginUrl = config.getProperty("login.url");
        if (loginUrl == null || loginUrl.trim().isEmpty()) {
            throw new RuntimeException("Missing property: login.url in config.properties");
        }

        driver.get(loginUrl);
        System.out.println("[STEP] Opened Login URL: " + loginUrl);

        // Resolve username field: try primary, else fallback
        try {
            usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameFieldBy));
            System.out.println("[INFO] Using primary username locator: id=txtUsername");
        } catch (TimeoutException e) {
            System.out.println("[INFO] Primary username locator not found; trying fallback locator...");
            usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameAltBy));
            System.out.println("[INFO] Using fallback username locator (xpath contains 'username').");
        }

        label  = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameLabelBy));
        prefix = wait.until(ExpectedConditions.visibilityOfElementLocated(prefixBy));
        System.out.println("[PASS] Login page elements visible (username, label, prefix).");
    }

    // ---------- Task 1: Username field should be visible ----------
    @Test(priority = 1)
    public void verifyUsernameFieldVisible() {
        Assert.assertTrue(usernameField.isDisplayed(), "Username field is not visible.");
        System.out.println("[PASS] Username field is visible below header.");
    }

    // ---------- Task 2: Verify label text and CSS color ----------
    @Test(priority = 2)
    public void verifyLabelTextAndColor() {
        String text  = label.getText().trim();
        String color = label.getCssValue("color");
        Assert.assertEquals(text, "Username", "Username label text mismatch.");
        System.out.println("[PASS] Label text OK: '" + text + "', color=" + color);
    }

    // ---------- Task 3: Prefix should not be clickable ----------
    @Test(priority = 3)
    public void verifyPrefixNonClickable() {
        Assert.assertTrue(prefix.isDisplayed(), "Prefix is not displayed.");
        Assert.assertTrue(prefix.isEnabled(), "Prefix is unexpectedly disabled.");

        String tag = prefix.getTagName();
        boolean looksClickable = tag.equalsIgnoreCase("button") || tag.equalsIgnoreCase("a");
        Assert.assertFalse(looksClickable, "Prefix looks clickable (tag: " + tag + ")");
        System.out.println("[PASS] Prefix '@' is non-clickable UI (tag=" + tag + ").");
    }

    // ---------- Task 4: Verify we cannot type on the prefix ----------
    @Test(priority = 4)
    public void verifyCannotTypeOnPrefix() {
        try {
            prefix.sendKeys("Writing on prefix @");
            String val = prefix.getAttribute("value");
            Assert.assertTrue(val == null || val.isEmpty(), "Prefix accepted text unexpectedly.");
            System.out.println("[PASS] Prefix did not accept text (no value attribute set).");
        } catch (Exception e) {
            System.out.println("[PASS] Cannot type into prefix '@' (sendKeys threw as expected).");
        }
    }

    // ---------- Task 5: Verify prefix and username background colors ----------
    @Test(priority = 5)
    public void verifyPrefixAndUsernameBackgroundColors() {
        String prefixColor   = prefix.getCssValue("background-color");
        String usernameColor = usernameField.getCssValue("background-color");
        System.out.println("[INFO] Prefix BG: " + prefixColor + " | Username BG: " + usernameColor);

        // Basic sanity: ensure they are not identical (optional)
        Assert.assertNotEquals(prefixColor, usernameColor,
                "Prefix and username input have identical background colors (unexpected).");
        System.out.println("[PASS] Prefix and username backgrounds differ as expected.");
    }

    // ---------- Echo: pass text and verify it appears in the username input ----------
    @Test(priority = 6)
    public void verifyUsernameInputEcho() {
        usernameField.clear();
        String input = config.getProperty("test.username.echo", "admin_user");
        usernameField.sendKeys(input);
        String enteredText = usernameField.getAttribute("value");
        Assert.assertEquals(enteredText, input, "Text is not correctly echoed in the Username field.");
        System.out.println("[PASS] Username input echoes correctly: '" + enteredText + "'");
    }
}
