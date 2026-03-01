package com.insureedge.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminDashboardPage extends BasePage {

    public AdminDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /** Top 4 dashboard tiles */
    public enum Tile {
        REGISTERED_USERS(By.id("ContentPlaceHolder_Admin_lblRegisteredUsers"), "AdminViewCustomer"),
        LISTED_POLICIES(By.id("ContentPlaceHolder_Admin_lblListedPolicies"), "AdminViewPolicy"),
        LISTED_CATEGORIES(By.id("ContentPlaceHolder_Admin_lblListedCategories"), "AdminCreateMainCategory"),
        TOTAL_QUESTIONS(By.id("ContentPlaceHolder_Admin_lblTotalQuestions"), null); // non-clickable

        public final By count;
        public final String expectedRoute; // null => non-clickable

        Tile(By count, String expectedRoute) {
            this.count = count;
            this.expectedRoute = expectedRoute;
        }
    }

    /** Bottom 4 policy holder cards */
    public enum PolicyHolderCard {
        APPLIED (By.id("ContentPlaceHolder_Admin_lblAppliedPolicyHolders"),   "AdminAppliedPolicyHolders"),
        APPROVED(By.id("ContentPlaceHolder_Admin_lblApprovedPolicyHolders"),  "AdminApprovedPolicyHolder.aspx"),
        PENDING (By.id("ContentPlaceHolder_Admin_lblPendingPolicyHolders"),   "AdminPendingPolicyHolder.aspx"),
        REJECTED(By.id("ContentPlaceHolder_Admin_lblRejectedPolicyHolders"),  "AdminRejectedPolicyHolder.aspx");

        public final By count;
        public final String expectedRoute;

        PolicyHolderCard(By count, String expectedRoute) {
            this.count = count;
            this.expectedRoute = expectedRoute;
        }
    }

    /** Wait until all eight counters are visible */
    public void waitForLoaded() {
        try {
            // top 4
            for (Tile t : Tile.values())
                visible(t.count);
            // bottom 4
            for (PolicyHolderCard c : PolicyHolderCard.values())
                visible(c.count);
            System.out.println("[PASS] Admin Dashboard counters visible.");
        } catch (TimeoutException e) {
            System.out.println("[FAIL] Dashboard counters not visible. URL: " + driver.getCurrentUrl());
            throw e;
        }
    }

    /* -------- Tile helpers -------- */
    public int getCount(Tile t) {
        String txt = visible(t.count).getText().trim();
        if (!txt.matches("\\d+")) throw new AssertionError(t + " count not numeric: '" + txt + "'");
        return Integer.parseInt(txt);
    }

    public boolean isClickable(Tile t) {
        if (t.expectedRoute == null) return false;
        try {
            WebElement h6 = visible(t.count);
            WebElement a = h6.findElement(By.xpath("./ancestor::a[1]"));
            String href = attr(a, "href").toLowerCase();
            return !(href.isEmpty() || href.endsWith("#") || href.contains("javascript:void"));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

      //public String getHref(Tile t) {
        //WebElement h6 = visible(t.count);
        //WebElement a = h6.findElement(By.xpath("./ancestor::a[1]"));
        //return attr(a, "href");
    //}

    public void clickTile(Tile t) {
        click(t.count);
    }

    /* -------- Policy holder card helpers -------- */
    public int getCount(PolicyHolderCard c) {
        String txt = visible(c.count).getText().trim();
        if (!txt.matches("\\d+")) throw new AssertionError(c + " count not numeric: '" + txt + "'");
        return Integer.parseInt(txt);
    }

    public boolean isClickable(PolicyHolderCard c) {
        try {
            WebElement h6 = visible(c.count);
            WebElement a = h6.findElement(By.xpath("./ancestor::a[1]"));
            String href = attr(a, "href").toLowerCase();
            return !(href.isEmpty() || href.endsWith("#") || href.contains("javascript:void"));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getHref(PolicyHolderCard c) {
        WebElement h6 = visible(c.count);
        WebElement a = h6.findElement(By.xpath("./ancestor::a[1]"));
        return attr(a, "href");
    }

    public void clickPolicyHolder(PolicyHolderCard c) {
        click(c.count);
    }
}