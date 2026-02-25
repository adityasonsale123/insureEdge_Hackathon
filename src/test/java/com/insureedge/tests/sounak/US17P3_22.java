package com.insureedge.tests.sounak;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class US17P3_22 {

    private static WebDriver driver;

    @BeforeClass
    public void setUp() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://qeaskillhub.cognizant.com/pages_Register");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void createAccountTest()  {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        //driver.findElement(By.xpath("//label[text()='Your Name']//following-sibling::input")).sendKeys("Rahul");
        driver.findElement(By.xpath("//input[contains(@id , 'yourName')]")).sendKeys("Rahul");
        driver.findElement(By.id("yourEmail")).sendKeys("rahul123@gmail.com");
        driver.findElement(By.id("yourUsername")).sendKeys("rahul123");
        driver.findElement(By.id("yourPassword")).sendKeys("rahultest");
        driver.findElement(By.xpath("//input[@id='acceptTerms']")).click();
        driver.findElement(By.xpath("//button[text()='Create Account']")).click();

        driver.findElement(By.xpath("/html/body/main/div/section/div/div/div/div[2]/div/form/div[7]/p/a")).click();

        String originalWindowHandle = driver.getWindowHandle();

        String expected_url = "https://qeaskillhub.cognizant.com/AdminDashboard";
        Set<String> windowHandles = driver.getWindowHandles();
        for (String handle : windowHandles) {
            if (!handle.equals(originalWindowHandle)) {
                driver.switchTo().window(handle);
                break; // Switch to the first new tab found
            }
        }

        String actual_url = driver.getCurrentUrl();
        SoftAssert sa = new SoftAssert();
        sa.assertEquals(actual_url, expected_url);
        sa.assertAll();

    }

    @Test
    public void newAccountTest() {
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://qeaskillhub.cognizant.com/LoginPage?logout=true");
        driver.findElement(By.id("txtUsername")).sendKeys("rahul123");
        driver.findElement(By.id("txtPassword")).sendKeys("rahultest");
        driver.findElement(By.id("BtnLogin")).click();

        String originalWindowHandle = driver.getWindowHandle();

        String expected_url = "https://qeaskillhub.cognizant.com/AdminDashboard";
        Set<String> windowHandles = driver.getWindowHandles();
        for (String handle : windowHandles) {
            if (!handle.equals(originalWindowHandle)) {
                driver.switchTo().window(handle);
                break; // Switch to the first new tab found
            }
        }

        String actual_url = driver.getCurrentUrl();
        SoftAssert sa1 = new SoftAssert();
        sa1.assertEquals(actual_url, expected_url);
        sa1.assertAll();


    }

}
