package com.insureedge.tests.sounak;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.AdminDashboardPage;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * US17P3_30
 * Validates navigation of Admin Dashboard tiles:
 * 1) Registered Users → AdminViewCustomer
 * 2) Listed Policies → AdminViewPolicy
 * 3) Listed Categories → AdminCreateMainCategory
 */
public class US17P3_30 extends BaseUiTest {

    @Test(priority = 1, description = "Validate Registered Users tile navigation")
    public void registeredUserTest() {
        loginIfNeeded();   // Reuse framework login
        AdminDashboardPage dashboard = new AdminDashboardPage(driver, wait);
        dashboard.waitForLoaded();

        dashboard.click(AdminDashboardPage.Tile.REGISTERED_USERS);

        String expected = "AdminViewCustomer";
        String actual = driver.getCurrentUrl();

        SoftAssert sa = new SoftAssert();
        sa.assertTrue(actual.contains(expected),
                "Expected URL to contain: " + expected + " but got: " + actual);
        sa.assertAll();
    }

    @Test(priority = 2, description = "Validate Listed Policies tile navigation")
    public void listedPoliciesTest() {
        loginIfNeeded();
        AdminDashboardPage dashboard = new AdminDashboardPage(driver, wait);
        dashboard.waitForLoaded();

        dashboard.click(AdminDashboardPage.Tile.LISTED_POLICIES);

        String expected = "AdminViewPolicy";
        String actual = driver.getCurrentUrl();

        SoftAssert sa = new SoftAssert();
        sa.assertTrue(actual.contains(expected),
                "Expected URL to contain: " + expected + " but got: " + actual);
        sa.assertAll();
    }

    @Test(priority = 3, description = "Validate Listed Categories tile navigation")
    public void listedCategoriesTest() {
        loginIfNeeded();
        AdminDashboardPage dashboard = new AdminDashboardPage(driver, wait);
        dashboard.waitForLoaded();

        dashboard.click(AdminDashboardPage.Tile.LISTED_CATEGORIES);

        String expected = "AdminCreateMainCategory";
        String actual = driver.getCurrentUrl();

        SoftAssert sa = new SoftAssert();
        sa.assertTrue(actual.contains(expected),
                "Expected URL to contain: " + expected + " but got: " + actual);
        sa.assertAll();
    }
}