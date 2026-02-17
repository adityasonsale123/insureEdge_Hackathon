package com.insureedge.tests.sounak;

import com.insureedge.base.BaseUiTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.Set;

/**
 * US17P3_07
 *
 * Tasks:
 * 1) Validate the UI label text: "Don't have account? Create an account"
 * 2) Validate navigation of the "Create an account" link (opens same tab or new tab)
 *
 * Integration notes:
 * - Extends BaseUiTest for driver lifecycle + shared waits + config.
 * - Uses explicit waits for stability.
 * - URL is config-driven: login.url (fallback to known URL).
 */
public class US17P3_07 extends BaseUiTest {

    // Locators
    private static final By CTA_PARAGRAPH = By.xpath("//p[contains(@class,'small') and contains(@class,'mb-0')]");
    private static final By CTA_LINK = By.xpath("//p[contains(@class,'small') and contains(@class,'mb-0')]/a");

    private String getLoginUrl() {
        String url = config.getProperty("login.url", "").trim();
        return url.isEmpty() ? "https://qeaskillhub.cognizant.com/LoginPage?logout=true" : url;
    }

    /**
     * Priority 1: Validate the label text exactly matches the spec.
     */
    @Test(priority = 1, description = "Validate CTA text under login form")
    public void checkText() {
        // Navigate fresh to the login page for test isolation
        String targetUrl = getLoginUrl();
        driver.get(targetUrl);
        System.out.println("[STEP] Opened Login URL: " + targetUrl);

        // Wait for paragraph to be visible
        WebElement p = wait.until(ExpectedConditions.visibilityOfElementLocated(CTA_PARAGRAPH));
        String actualText = p.getText().trim();
        String expectedText = "Don't have account? Create an account";

        SoftAssert sa = new SoftAssert();
        sa.assertEquals(actualText, expectedText, "CTA text does not match the expected design copy.");
        System.out.println("[STEP] CTA actual text: '" + actualText + "'");
        sa.assertAll();

        System.out.println("[PASS] CTA text matched exactly.");
    }

    /**
     * Priority 2: Validate the navigation behavior of the CTA link.
     * - Captures the href
     * - Clicks it
     * - Handles both: opens in same tab or a new tab
     * - Asserts the resulting URL matches expected href (full match) or starts with it (if query/params are appended)
     */
    @Test(priority = 2, description = "Validate CTA link navigation to Create Account page")
    public void checkURL() {
        // Navigate fresh to the login page for test isolation
        String targetUrl = getLoginUrl();
        driver.get(targetUrl);
        System.out.println("[STEP] Opened Login URL: " + targetUrl);

        WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(CTA_LINK));
        String expectedHref = linkElement.getAttribute("href");
        String result = expectedHref.substring(0, expectedHref.indexOf(".aspx"));
        if (expectedHref == null) expectedHref = "";
        expectedHref = expectedHref.trim();

        // Record current context (for new-tab / same-tab handling)
        String originalWindow = driver.getWindowHandle();
        Set<String> beforeHandles = driver.getWindowHandles();

        // Click the link
        linkElement.click();
        System.out.println("[STEP] Clicked CTA link with href: " + result);

        // Small wait for navigation or new tab
        wait.withTimeout(Duration.ofSeconds(5));

        // Determine if a new tab opened
        Set<String> afterHandles = driver.getWindowHandles();
        if (afterHandles.size() > beforeHandles.size()) {
            // Switch to the newly opened tab
            for (String handle : afterHandles) {
                if (!beforeHandles.contains(handle)) {
                    driver.switchTo().window(handle);
                    System.out.println("[STEP] Switched to new tab: " + handle);
                    break;
                }
            }
        } else {
            // Same-tab navigation: wait for URL change (best-effort)
            try {
                new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(d -> !driver.getCurrentUrl().equalsIgnoreCase(targetUrl));
            } catch (TimeoutException ignored) {
                // not fatal—may still have navigated with same base but anchor/hash
            }
        }

        // Compute actual URL
        String actualUrl = driver.getCurrentUrl();
        System.out.println("[STEP] Landed on URL: " + actualUrl);

        // If FRD expects extension-less compare, you tried substring before ".aspx".
        // Typically, we should compare the full href; however, to keep close to your original logic:
        // - Prefer full equality; OR startsWith (if server appends querystring/params).
        SoftAssert sa = new SoftAssert();
        if (!result.isEmpty()) {
            boolean fullMatch = actualUrl.equalsIgnoreCase(result);
            boolean startsWith = actualUrl.toLowerCase().startsWith(result.toLowerCase());
            sa.assertTrue(fullMatch || startsWith,
                    "Navigation URL mismatch. Expected '" + result + "' (or starting with it), but was '" + actualUrl + "'");
        } else {
            sa.fail("CTA link href attribute is empty—cannot validate expected navigation.");
        }

        sa.assertAll();
        System.out.println("[PASS] CTA navigation validated.");

        // If a new tab was opened, you may want to close it and switch back
        if (driver.getWindowHandles().size() > 1) {
            try {
                driver.close();
                driver.switchTo().window(originalWindow);
                System.out.println("[STEP] Closed new tab and switched back to original window.");
            } catch (Exception ignored) {
                // Not critical for test validity
            }
        }
    }
}