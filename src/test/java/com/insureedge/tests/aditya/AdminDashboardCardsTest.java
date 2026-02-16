package com.insureedge.tests.aditya;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.AdminDashboardPage;
import com.insureedge.pages.AdminDashboardPage.Tile;
import org.testng.Assert;
import org.testng.annotations.*;

public class AdminDashboardCardsTest extends BaseUiTest {

    private AdminDashboardPage dashboard;

    @BeforeClass
    @Override
    public void baseSetup() {
        super.baseSetup();
        loginIfNeeded();
        dashboard = new AdminDashboardPage(driver, wait);
        dashboard.waitForLoaded();
    }

    @Test(priority = 1)
    public void countsAreNumericAndNonNegative() {
        for (Tile c : Tile.values()) {
            int count = dashboard.getCount(c);
            Assert.assertTrue(count >= 0, c + " should be >= 0");
            System.out.println("[PASS] " + c + " count OK: " + count);
        }
    }

    @Test(priority = 2)
    public void clickableCardsNavigate_questionsDoesNot() throws InterruptedException {
        // Clickable ones
        for (Tile c : new Tile[]{Tile.REGISTERED_USERS, Tile.LISTED_POLICIES, Tile.LISTED_CATEGORIES}) {
            Assert.assertTrue(dashboard.isClickable(c), c + " must be clickable");
            String before = driver.getCurrentUrl();
            dashboard.clickTile(c);

            long t0 = System.currentTimeMillis();
            while (driver.getCurrentUrl().equals(before) && System.currentTimeMillis() - t0 < 5000) {
                Thread.sleep(100);
            }
            String after = driver.getCurrentUrl();
            Assert.assertTrue(after.contains(c.expectedRoute),
                    c + " should navigate to " + c.expectedRoute + " but got: " + after);
            System.out.println("[PASS] " + c + " navigated to: " + after);

            driver.navigate().back();
            dashboard.waitForLoaded();
        }

        // Non-clickable
        Assert.assertFalse(dashboard.isClickable(Tile.TOTAL_QUESTIONS), "Total Questions must be non-clickable");
        String urlBefore = driver.getCurrentUrl();
        dashboard.clickTile(Tile.TOTAL_QUESTIONS);
        Assert.assertEquals(driver.getCurrentUrl(), urlBefore, "Clicking Total Questions should not change URL.");
        System.out.println("[PASS] Total Questions did not navigate.");
    }
}