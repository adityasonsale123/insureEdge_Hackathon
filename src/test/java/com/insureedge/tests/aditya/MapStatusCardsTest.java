package com.insureedge.tests.aditya;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.AdminDashboardPage;
import com.insureedge.pages.AdminDashboardPage.PolicyHolderCard;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Locale;

public class MapStatusCardsTest extends BaseUiTest {

    private AdminDashboardPage page;

    @BeforeClass(alwaysRun = true)
    @Override
    public void baseSetup() {
        super.baseSetup();
        loginIfNeeded(); // navigates to dashboard.url (AdminDashboard)
        page = new AdminDashboardPage(driver, wait);
        page.waitForLoaded();
    }

    @Test(priority = 1)
    public void countsShouldBeNumeric() {
        for (PolicyHolderCard c : PolicyHolderCard.values()) {
            int count = page.getCount(c);
            Assert.assertTrue(count >= 0, c.name() + " count should be >= 0");
            System.out.println("[PASS] " + c.name() + " count: " + count);
        }
    }

    @Test(priority = 2)
    public void eachCardHasNavigableHref() {
        for (PolicyHolderCard c : PolicyHolderCard.values()) {
            String href = page.getHref(c);
            Assert.assertTrue(page.isClickable(c), c.name() + " must be clickable but got: " + href);

            // Accept both .aspx and extension-less in href
            String exp = c.expectedRoute.toLowerCase(Locale.ROOT);          // may or may not include .aspx
            String hrefLc = href.toLowerCase(Locale.ROOT);
            boolean hrefOk = hrefLc.contains(exp) || hrefLc.contains(exp + ".aspx");

            Assert.assertTrue(hrefOk,
                    c.name() + " href should contain route '" + c.expectedRoute + "' (or with .aspx) but got: " + href);

            System.out.println("[PASS] " + c.name() + " href OK: " + href);
        }
    }

    @Test(priority = 3)
    public void clickingEachCardNavigatesToCorrectListPage() throws InterruptedException {
        for (PolicyHolderCard c : PolicyHolderCard.values()) {
            String before = driver.getCurrentUrl();
            page.clickPolicyHolder(c);

            waitForUrlChange(before, 5000);
            String after = driver.getCurrentUrl();

            // Accept both forms after navigation: with .aspx or extension-less
            String expectedWithExt = c.expectedRoute.toLowerCase(Locale.ROOT);
            String expectedNoExt   = expectedWithExt.replace(".aspx", "");
            String afterLc = after.toLowerCase(Locale.ROOT);

            boolean matches = afterLc.contains(expectedWithExt) || afterLc.contains(expectedNoExt);

            Assert.assertTrue(matches,
                    c.name() + " should navigate to '" + c.expectedRoute +
                            "' (or rewritten '" + expectedNoExt + "') but got: " + after);

            System.out.println("[PASS] " + c.name() + " navigated to: " + after);

            driver.navigate().back();
            page.waitForLoaded();
        }
    }

    // ---- tiny helper ----
    private void waitForUrlChange(String oldUrl, long timeoutMs) throws InterruptedException {
        long t0 = System.currentTimeMillis();
        while (driver.getCurrentUrl().equals(oldUrl) && System.currentTimeMillis() - t0 < timeoutMs) {
            Thread.sleep(100);
        }
    }
}
