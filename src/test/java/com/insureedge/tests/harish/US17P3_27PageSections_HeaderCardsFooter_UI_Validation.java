package com.insureedge.tests.harish;

import com.insureedge.base.BaseUiTest;
import java.time.Duration;
import org.openqa.selenium.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class US17P3_27PageSections_HeaderCardsFooter_UI_Validation extends BaseUiTest {

    @BeforeClass(alwaysRun = true)
    public void setupAndLogin() throws Exception {
        // NOTE:
        // - BaseUiTest.baseSetup() (superclass @BeforeClass) already launched the browser,
        //   maximized it, set waits, and loaded config.properties.
        // - Here we only ensure login + dashboard using the centralized helper.
        loginIfNeeded();

        // Preserve your original implicit wait from the test setup
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    // No @AfterClass — BaseUiTest.baseTeardown() will quit the browser.

    @Test
    public void validatePageSections() throws Exception {

        // =============================
        // 1) HEADER VALIDATION
        // =============================
        WebElement header = driver.findElement(By.className("pagetitle"));

        if (header.isDisplayed()) {
            System.out.println("PASS → Header dash Board is visible.");
        } else {
            System.out.println("FAIL → Header dash Board is NOT visible.");
        }

        // =============================
        // 2) CARDS SECTION VALIDATION
        // =============================
        WebElement cardsSection = driver.findElement(By.cssSelector("section.section.dashboard"));

        if (cardsSection.isDisplayed()) {
            System.out.println("PASS → Cards section is visible.");
        } else {
            System.out.println("FAIL → Cards section NOT visible.");
        }

        // =============================
        // 3) FOOTER VALIDATION
        // =============================
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 400);");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement footer = driver.findElement(By.cssSelector("footer#footer"));

        if (footer.isDisplayed()) {
            System.out.println("PASS → Footer is visible.");
        } else {
            System.out.println("FAIL → Footer is NOT visible.");
        }

        String footerText = footer.getText();
        WebElement footerlink = driver.findElement(By.className("credits"));
        String footername = footerlink.getText();
        if (footername.contains("Designed by") || footername.contains("QEA Skill Enable Function"));
        {
            System.out.print(" Second footer QEA skill enable text is there ");
        }
        if (footerText.contains("InsurEdge") && footerText.contains("All Rights Reserved")) {
            System.out.println("PASS → First Footer contains correct text.");
        } else {
            System.out.println("FAIL → Footer text incorrect.");
        }
    }
}