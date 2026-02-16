package com.insureedge.tests.harish;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;
import org.testng.annotations.*;

public class InsureEdgeUS17P3_18Test {

    private WebDriver driver;
    private WebElement terms;
    private WebElement checkbox;
    private WebElement termsLink;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://qeaskillhub.cognizant.com/LoginPage?logout=true");
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//a[text()='Create an account']")).click();

        // Initialize global elements
        terms = driver.findElement(By.xpath("//label[@class='form-check-label']"));
        checkbox = driver.findElement(By.xpath("//input[@class='form-check-input']"));
        termsLink = driver.findElement(By.xpath("//a[text()='terms and conditions']"));
    }

    @Test(priority = 1)
    public void task1_visibilityOfCheckboxAndText() {
        //Task 1
        if (terms.isDisplayed() && checkbox.isDisplayed())
            System.out.println("Checkbox and the text is visible");
    }

    @Test(priority = 2)
    public void task2_termsLinkClickableAndColor() {
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
    public void task3_layoutAndCheckboxSelect() {
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

    @AfterClass(alwaysRun = true)
    public void closeBrowser() {
        driver.quit();
    }
}

