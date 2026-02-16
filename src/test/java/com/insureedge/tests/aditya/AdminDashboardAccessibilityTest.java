package com.insureedge.tests;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.AdminDashboardPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;

public class AdminDashboardAccessibilityTest extends BaseUiTest {

    private static final By LINK_USERS      = By.xpath("//*[@id='ContentPlaceHolder_Admin_lblRegisteredUsers']/ancestor::a[1]");
    private static final By LINK_POLICIES   = By.xpath("//*[@id='ContentPlaceHolder_Admin_lblListedPolicies']/ancestor::a[1]");
    private static final By LINK_CATEGORIES = By.xpath("//*[@id='ContentPlaceHolder_Admin_lblListedCategories']/ancestor::a[1]");
    private static final By MAYBE_LINK_QUESTIONS = By.xpath("//*[@id='ContentPlaceHolder_Admin_lblTotalQuestions']/ancestor::a[1]");

    @BeforeClass
    @Override
    public void baseSetup() {
        super.baseSetup();
        loginIfNeeded();
        new AdminDashboardPage(driver, wait).waitForLoaded();
    }

    @Test(priority = 1)
    public void cardsHaveDiscernibleNames() {
        assertLinkHasName(LINK_USERS, "Users link");
        assertLinkHasName(LINK_POLICIES, "Policies link");
        assertLinkHasName(LINK_CATEGORIES, "Categories link");
        ensureQuestionsNonInteractive();
    }

    @Test(priority = 2)
    public void simpleFocusOrderOnCards() {
        WebElement first  = wait.until(ExpectedConditions.visibilityOfElementLocated(LINK_USERS));
        WebElement second = wait.until(ExpectedConditions.visibilityOfElementLocated(LINK_POLICIES));
        WebElement third  = wait.until(ExpectedConditions.visibilityOfElementLocated(LINK_CATEGORIES));

        ((JavascriptExecutor)driver).executeScript("arguments[0].focus()", first);
        pressTab();
        WebElement a2 = active();
        pressTab();
        WebElement a3 = active();

        Assert.assertEquals(a2, second, "[FAIL] Focus order mismatch at 2");
        Assert.assertEquals(a3, third,  "[FAIL] Focus order mismatch at 3");
        System.out.println("[PASS] Focus order is logical for dashboard cards.");
    }

    private void assertLinkHasName(By by, String label) {
        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        String text = safe(link.getText());
        String aria = safe(link.getAttribute("aria-label"));
        Assert.assertTrue(!text.isEmpty() || !aria.isEmpty(),
                label + " should have accessible name");
        System.out.println("[PASS] " + label + " has accessible name: " + (text.isEmpty()? aria : text));
    }

    private void ensureQuestionsNonInteractive() {
        java.util.List<WebElement> maybe = driver.findElements(MAYBE_LINK_QUESTIONS);
        if (maybe.isEmpty()) {
            System.out.println("[INFO] Total Questions not a link. Good.");
            return;
        }
        WebElement a = maybe.get(0);
        String href = safe(a.getAttribute("href")).toLowerCase();
        Assert.assertTrue(href.isEmpty() || href.endsWith("#") || href.contains("javascript:void"),
                "Total Questions must NOT navigate");
        System.out.println("[PASS] Total Questions is non-interactive (as expected).");
    }

    private void pressTab() {
        new org.openqa.selenium.interactions.Actions(driver).sendKeys(Keys.TAB).perform();
    }
    private WebElement active() {
        return (WebElement)((JavascriptExecutor)driver).executeScript("return document.activeElement");
    }
    private String safe(String s) { return s == null ? "" : s.trim(); }
}
