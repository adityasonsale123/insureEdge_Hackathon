package com.insureedge.tests.sarbik;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.testng.annotations.*;

public class InsureEdgeUS17P3_18Test extends BaseUiTest {  // <-- extend BaseUiTest

    private WebElement terms;
    private WebElement checkbox;
    private WebElement termsLink;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        // baseSetup() from BaseUiTest runs first to init driver & wait.
        baseSetup();

        // Use config if present; otherwise default to the known login URL
        String loginUrl = config.getProperty("login.url", "").trim();


        // Open login page using your POM (no added waits)
        new LoginPage(driver, wait).open(loginUrl);

        driver.manage().window().maximize();
        driver.findElement(By.xpath("//a[text()='Create an account']")).click();

        // Initialize global elements
        terms = driver.findElement(By.xpath("//label[@class='form-check-label']"));
        checkbox = driver.findElement(By.xpath("//input[@class='form-check-input']"));
        termsLink = driver.findElement(By.xpath("//a[text()='terms and conditions']"));
    }

    @Test(priority = 1)
    public void visibilityOfCheckboxAndText() {
        //Task 1
        if (terms.isDisplayed() && checkbox.isDisplayed())
            System.out.println("Checkbox and the text is visible");
    }

    @Test(priority = 2)
    public void termsLinkClickableAndColor() {
        //Task 2
        String termsColor = termsLink.getCssValue("color");
        String hexColor = Color.fromString(termsColor).asHex();
        try {
            termsLink.click();
            //driver.navigate().back();  Defect-> The link is not working
            System.out.println("'Terms and Conditions' is clickable and has a colour: " + hexColor);
        } catch (Exception e) {
            System.out.println("'Terms and Conditions' is not clickable");
        }
    }

    @Test(priority = 3)
    public void layoutAndCheckboxSelect() {
        //Task 3
        int checkboxX = checkbox.getLocation().getX();
        int termsX = terms.getLocation().getX();
        if (checkboxX < termsX) {
            System.out.println("Checkbox is placed to the left of the terms text.");
        }
        checkbox.click();
        if (checkbox.isSelected()) {
            System.out.println("Checkbox is selected, tick is visible.");
        }
    }

    // No @AfterClass — BaseUiTest.baseTeardown() handles driver.quit()
}
