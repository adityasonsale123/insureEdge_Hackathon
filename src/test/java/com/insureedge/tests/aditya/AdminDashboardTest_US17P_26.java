package com.insureedge.tests.aditya;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.AdminDashboardPage;
import com.insureedge.pages.AdminDashboardPage.Tile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

public class AdminDashboardTest_US17P_26 extends BaseUiTest {

    private AdminDashboardPage dashboard;

    @BeforeClass
    public void openDashboard() {
        loginIfNeeded();
        dashboard = new AdminDashboardPage(driver, wait);
        dashboard.waitForLoaded();
    }

    @Test(priority = 1)
    public void checkCountsAreNumeric() {
        for (Tile t : Tile.values()) {
            int count = dashboard.getCount(t);
            Assert.assertTrue(count >= 0);
        }
    }

    @Test(priority = 2)
    public void checkClickableTilesNavigate() {
        Tile[] clickable = {
                Tile.REGISTERED_USERS,
                Tile.LISTED_POLICIES,
                Tile.LISTED_CATEGORIES
        };

        for (Tile t : clickable) {
            Assert.assertTrue(dashboard.isClickable(t));
            dashboard.click(t);

            wait.until(ExpectedConditions.urlContains(
                    t.expectedRoute.toLowerCase()
            ));

            driver.navigate().back();
            dashboard.waitForLoaded();
        }
    }

    @Test(priority = 3)
    public void checkTotalQuestionsIsNotClickable() {
        Assert.assertFalse(dashboard.isClickable(Tile.TOTAL_QUESTIONS));
    }
}