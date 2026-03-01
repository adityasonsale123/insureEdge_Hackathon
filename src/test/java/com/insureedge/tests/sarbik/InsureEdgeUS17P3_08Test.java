package com.insureedge.tests.sarbik;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

public class InsureEdgeUS17P3_08Test extends BaseUiTest {  // <-- extend BaseUiTest

    private WebElement footer;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        setUp();

        // Use config if present; otherwise default to the known login URL
        String loginUrl = config.getProperty("login.url", "").trim();


        // Open login page using your POM (no added waits)
        new LoginPage(driver, wait).open(loginUrl);

        driver.manage().window().maximize();

        // Initialize the footer element
        footer = driver.findElement(By.xpath("//div[@class='credits']"));
    }

    // Task 1
    @Test
    public void verifyFooterVisible() {
        if (footer.isDisplayed()) {
            System.out.println("Footer text is visible.");
        }
    }

    // Task 2
    @Test
    public void verifyFooterNonClickable() {
        System.out.println("Footer displayed: " + footer.isDisplayed() + ", enabled: " + footer.isEnabled());
        if (!footer.getTagName().equalsIgnoreCase("button")) {
            System.out.println("The footer is not clickable UI element");
        }
    }

    // Task 3
    @Test
    public void verifyFooterTextColor() {
        String color = footer.getCssValue("color"); // returns rgba format
        String hexColor = Color.fromString(color).asHex();
        System.out.println("Footer text color: " + hexColor);
    }

    // No @AfterClass here — BaseUiTest.baseTeardown() handles browser quit
}
