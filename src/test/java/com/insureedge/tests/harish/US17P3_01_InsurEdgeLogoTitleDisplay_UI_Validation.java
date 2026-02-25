package com.insureedge.tests.harish;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

public class US17P3_01_InsurEdgeLogoTitleDisplay_UI_Validation extends BaseUiTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        baseSetup();
        String loginUrl=config.getProperty("login.url", "").trim();

        new LoginPage(driver,wait).open(loginUrl);
        driver.manage().window().maximize();
    }

    @Test
    public void title() {
        String expectedTitle = "InsurEdge";

        WebElement titleElement = driver.findElement(By.xpath("//span[normalize-space()='InsurEdge']"));

        String actualTitle = titleElement.getText();

        if (actualTitle.equals(expectedTitle)) {
            System.out.println("Title Validation Passed! Found: " + actualTitle);
        } else {
            System.out.println("Title Validation Failed. Expected " + expectedTitle + " but found " + actualTitle);
        }
    }


    @Test
    void testLogoVisibility() {
        WebElement logo = driver.findElement(By.xpath("//span[text()='InsurEdge']"));

        if (logo.isDisplayed()) {
            System.out.println("Logo is visible on the page.");
        } else {
            System.out.println("Logo is NOT visible on the page.");
        }



    }
    /* @Test(priority = 3)
    void screenshot()
    {
        TakesScreenshot ts=(TakesScreenshot) driver;
        File source=ts.getScreenshotAs(OutputType.FILE);
        File dest=new File("C:\\Project\\insureEdge_Hackathon\\screenshots\\fullpage.png");
        source.renameTo(dest);


    }*/


}
