package com.insureedge.tests.sarbik;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.testng.annotations.*;

public class InsureEdgeUS17P3_13Test extends BaseUiTest { // <-- extend BaseUiTest

    WebElement logo;
    WebElement text;
    WebElement createAccount;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        baseSetup();

        // Use config if present; otherwise default to the known login URL
        String loginUrl = config.getProperty("login.url", "").trim();


        // Open login page using your POM (no added waits)
        new LoginPage(driver, wait).open(loginUrl);

        driver.manage().window().maximize();
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Go to Registration page
       driver.findElement(By.xpath("//a[text()='Create an account']")).click();

        // Initialize page elements used in tests
        logo = driver.findElement(By.xpath("//a[@class='logo d-flex align-items-center w-auto']//child::img"));
        text = driver.findElement(By.xpath("//span[text()='NiceAdmin']"));
        createAccount = driver.findElement(By.xpath("//h5[text()='Create an Account']"));
    }

    @Test(priority = 1)
    public void task1_verifyLogoAndHeaderVisibility() {
        // Task 1
        if (logo.isDisplayed() && text.isDisplayed())
            System.out.println("Logo and Header text is visible");
    }

    @Test(priority = 2)
    public void task2_checkHeaderClickable() {
        // Task 2
        try {
            text.click();
            System.out.println("NiceAdmin text is clickable.");
            driver.navigate().back(); // go back to registration page
        } catch (Exception e) {
            System.out.println("NiceAdmin text is NOT clickable.");
        }
    }

    @Test(priority = 3)
    public void task3_validateLogoLeftOfText() {
        // Task 3
        int logoX = logo.getLocation().getX();
        int textX = text.getLocation().getX();

        if (logoX < textX) {
            System.out.println("Logo is placed to the left of NiceAdmin text.");
        } else {
            System.out.println("Logo is NOT placed correctly.");
        }
    }

    @Test(priority = 4)
    public void task4_verifyHeaderVisibility() {
        // Task 4
        if (createAccount.isDisplayed())
            System.out.println("Header Create an Account is visible");
    }

    @Test(priority = 5)
    public void task6_verifyHeaderColor() {
        // Task 6
        String[] headings = { "h1", "h2", "h3", "h4", "h5", "h6" };
        String tag = createAccount.getTagName().toLowerCase();

        String headerColor = createAccount.getCssValue("color");
        String hexColor = Color.fromString(headerColor).asHex();

        // Traverse through the heading array
        for (String h : headings) {
            if (tag.equals(h)) {
                System.out.println("'Create an Account' is displayed as a header with the tag " + tag
                        + " and has the text colour as " + hexColor);
                break;
            }
        }
    }

    // No @AfterClass — BaseUiTest.baseTeardown() handles driver.quit()
}