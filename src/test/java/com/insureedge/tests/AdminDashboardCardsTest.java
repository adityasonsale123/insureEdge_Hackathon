package com.insureedge.tests;

import com.insureedge.pages.AdminDashboardPage;
import com.insureedge.pages.AdminDashboardPage.Card;
import com.insureedge.tests.BaseUiTest;
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
    public void countsShouldBeNumericAndNonNegative() {
        for (Card c : Card.values()) {
            int count = dashboard.getCount(c);
            Assert.assertTrue(count >= 0, c + " should be >= 0");
        }
    }

    @Test(priority = 2)
    public void clickableCardsNavigate_questionsDoesNot() {
        // Clickable
        for (Card c : new Card[]{Card.REGISTERED_USERS, Card.LISTED_POLICIES, Card.LISTED_CATEGORIES}) {
            Assert.assertTrue(dashboard.isClickable(c), c + " must be clickable.");
            String before = driver.getCurrentUrl();
            dashboard.clickCard(c);
            // wait for URL to change
            long start = System.currentTimeMillis();
            while (driver.getCurrentUrl().equals(before) && System.currentTimeMillis() - start < 5000) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
            String after = driver.getCurrentUrl();
            Assert.assertTrue(after.contains(c.expectedRouteIfClickable),
                    c + " should navigate to route " + c.expectedRouteIfClickable + " but got: " + after);
            driver.navigate().back();
            dashboard.waitForLoaded();
        }

        // Non-clickable
        Assert.assertFalse(dashboard.isClickable(Card.TOTAL_QUESTIONS), "Total Questions must be non-clickable.");
        String urlBefore = driver.getCurrentUrl();
        dashboard.clickCard(Card.TOTAL_QUESTIONS); // safe: either no-op or just <h6> click
        Assert.assertEquals(driver.getCurrentUrl(), urlBefore,
                "Clicking Total Questions should not change URL.");
    }
}