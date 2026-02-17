package com.insureedge.tests.sounak;

import com.insureedge.base.BaseUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * US17P3_02
 * Task: Validate that the header text is displayed with the correct wording and case:
 *       "Login to Your Account".
 *
 * Integration details:
 * - Extends BaseUiTest to reuse driver/waits and config.
 * - Uses explicit wait for the header to avoid flakiness.
 */
public class US17P3_02 extends BaseUiTest {

    private static final By LOGIN_HEADER = By.xpath("//div[contains(@class,'pt-4') and contains(@class,'pb-2')]/h5");

    @Test(description = "Verify Login page header text equals 'Login to Your Account'")
    public void header_ShouldMatchExpectedText() {
        // Prefer config-driven URL; fallback to the known URL if property is absent.
        String urlFromConfig = config.getProperty("login.url", "").trim();
        String targetUrl = urlFromConfig.isEmpty()
                ? "https://qeaskillhub.cognizant.com/LoginPage?logout=true"
                : urlFromConfig;

        driver.get(targetUrl);
        System.out.println("[STEP] Opened Login URL: " + targetUrl);

        // Wait for header to be visible
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_HEADER));

        String actualHeader = header.getText().trim();
        String expectedHeader = "Login to Your Account";

        System.out.println("[STEP] Found header text: '" + actualHeader + "'");
        Assert.assertEquals(
                actualHeader,
                expectedHeader,
                "Login page header text mismatch."
        );

        System.out.println("[PASS] Header text matched exactly: '" + expectedHeader + "'");
    }
}