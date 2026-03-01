package com.insureedge.tests.aditya;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.AdminDashboardPage;
import com.insureedge.pages.AdminDashboardPage.Tile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

public class MapStatusCardsTest_US17P3_31 extends BaseUiTest {

    private AdminDashboardPage page;

    @BeforeClass
    public void openDashboard() {
        loginIfNeeded();                     // login + go to dashboard
        page = new AdminDashboardPage(driver, wait);
        page.waitForLoaded();                // wait for all tiles to load
    }

    // ----------------------------------------------------
    // 1) All 4 tile counts must be numeric
    // ----------------------------------------------------
    @Test(priority = 1)
    public void verifyCountsAreNumeric() {
        for (Tile t : Tile.values()) {
            int count = page.getCount(t);
            Assert.assertTrue(count >= 0, t + " count must be >= 0");
            System.out.println("[PASS] " + t + " count: " + count);
        }
    }

    // ----------------------------------------------------
    // 2) Registered Users, Listed Policies, Listed Categories must navigate
    // ----------------------------------------------------
    @Test(priority = 2)
    public void verifyClickableTiles() {

        Tile[] clickable = {
                Tile.REGISTERED_USERS,
                Tile.LISTED_POLICIES,
                Tile.LISTED_CATEGORIES
        };

        for (Tile t : clickable) {

            Assert.assertTrue(page.isClickable(t), t + " must be clickable");

            page.click(t);

            // Wait for URL to contain expected route
            wait.until(ExpectedConditions.urlContains(t.expectedRoute.toLowerCase()));

            System.out.println("[PASS] " + t + " navigated to " + driver.getCurrentUrl());

            // Return to dashboard
            driver.navigate().back();
            page.waitForLoaded();
        }
    }

    // ----------------------------------------------------
    // 3) Total Questions tile MUST NOT navigate
    // ----------------------------------------------------
    @Test(priority = 3)
    public void verifyTotalQuestionsIsNotClickable() {

        Assert.assertFalse(page.isClickable(Tile.TOTAL_QUESTIONS),
                "Total Questions should NOT be clickable");

        String before = driver.getCurrentUrl();

        page.click(Tile.TOTAL_QUESTIONS);

        // Ensure URL stays same
        Assert.assertEquals(driver.getCurrentUrl(), before,
                "Total Questions must not navigate");

        System.out.println("[PASS] Total Questions did not navigate.");
    }
}