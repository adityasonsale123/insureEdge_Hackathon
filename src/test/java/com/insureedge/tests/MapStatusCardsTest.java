package com.insureedge.tests;

import com.insureedge.pages.ApplicationStatusPage;
import com.insureedge.pages.ApplicationStatusPage.StatusCard;
import com.insureedge.tests.BaseUiTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MapStatusCardsTest extends BaseUiTest {

    private ApplicationStatusPage page;

    @BeforeClass
    @Override
    public void baseSetup() {
        super.baseSetup();
        loginIfNeeded();                  // Reuse from BaseUiTest
        page = new ApplicationStatusPage(driver, wait);
        page.waitForLoaded();             // Wait for 4 cards to appear
    }

    @Test(priority = 1)
    public void countsShouldBeNumeric() {
        for (StatusCard c : StatusCard.values()) {
            int count = page.getCount(c);
            Assert.assertTrue(count >= 0, c.name() + " count should be >= 0");
        }
    }

    @Test(priority = 2)
    public void eachCardHasNavigableHref() {
        for (StatusCard c : StatusCard.values()) {
            String href = page.getHref(c);
            Assert.assertTrue(page.isClickable(c), c.name() + " must have a navigable href but got: " + href);
            Assert.assertTrue(href.contains(c.expectedRoute),
                    c.name() + " href should contain route " + c.expectedRoute + " but got: " + href);
        }
    }

    @Test(priority = 3)
    public void clickingEachCardNavigatesToCorrectListPage() {
        for (StatusCard c : StatusCard.values()) {
            String before = driver.getCurrentUrl();
            page.click(c);

            // wait simple: poll URL change for up to ~5s
            long start = System.currentTimeMillis();
            while (driver.getCurrentUrl().equals(before) && System.currentTimeMillis() - start < 5000) {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }

            String after = driver.getCurrentUrl();
            Assert.assertTrue(after.contains(c.expectedRoute),
                    c.name() + " should navigate to " + c.expectedRoute + " but got: " + after);

            driver.navigate().back();
            page.waitForLoaded();
        }
    }
}