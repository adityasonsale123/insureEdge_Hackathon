package com.insureedge.tests.sarbik;

import com.insureedge.base.BaseUiTest;
import com.insureedge.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

public class InsureEdgeUS17P3_23Test extends BaseUiTest {  // <-- extend BaseUiTest

    // === Global class-level WebElements (as requested) ===
    private WebElement submitButton;
    private WebElement nameError;
    private WebElement emailInput;
    private WebElement emailError;
    private WebElement usernameInput;
    private WebElement usernameError;
    private WebElement passwordInput;
    private WebElement passwordError;
    private WebElement checkbox;
    private WebElement checkboxError;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws InterruptedException {
        // baseSetup() from BaseUiTest runs first to init driver & wait.
        baseSetup();

        // Use config if present; otherwise default to the known login URL
        String loginUrl = config.getProperty("login.url", "").trim();


        // Open login page using your POM (no added waits)
        new LoginPage(driver, wait).open(loginUrl);

        driver.manage().window().maximize();
        driver.findElement(By.xpath("//a[text()='Create an account']")).click();

        // Scroll to bottom (kept exactly as in your script)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        // Optional: Wait to observe the scroll
        Thread.sleep(2000);

        // Initialize the submit button (used in multiple tasks)
        submitButton = driver.findElement(By.xpath("//button[text()='Create Account']"));

        // Adding all the element locators
        nameError = driver.findElement(By.xpath("//div[text()='Please, enter your name!']"));
        emailInput = driver.findElement(By.xpath("//input[@id='yourEmail']"));
        emailError = driver.findElement(By.xpath("//div[text()='Please enter a valid Email adddress!']"));
        usernameInput = driver.findElement(By.xpath("//input[@id='yourUsername']"));
        usernameError = driver.findElement(By.xpath("//div[text()='Please choose a username.']"));
        passwordInput = driver.findElement(By.xpath("//input[@name='password']"));
        passwordError = driver.findElement(By.xpath("//div[text()='Please enter your password!']"));
        checkbox = driver.findElement(By.xpath("//input[@class='form-check-input']"));
        checkboxError = driver.findElement(By.xpath("//div[text()='You must agree before submitting.']"));
    }

    @Test(priority = 1)
    public void nameFieldValidation() {
        // Task 1: Name field validation
        submitButton.click();
        if (nameError.getText().equals("Please, enter your name!")) {
            System.out.println("Validated empty username field. Please, enter your name! is displayed");
        }
        else {
            System.out.println("Please, enter your name! is not displayed");
        }
    }

    @Test(priority = 2)
    public void emailFieldValidation() {
        // Task 2: Email field validation

        emailInput.sendKeys("invalidEmail");
        submitButton.click();

        if (emailError.getText().equals("Please enter a valid Email adddress!")) {
            System.out.println("Validated Invalid email specified");
        }
        else {
            System.out.println("Failed to validate invalid email");
        }
    }

    @Test(priority = 3)
    public void usernameFieldValidation() {
        // Task 3: Username field validation
        usernameInput.clear();
        submitButton.click();

        if (usernameError.getText().equals("Please choose a username.")) {
            System.out.println("Validated error message is displayed");
        }
        else {
            System.out.println("Please choose a username. is not visible");
        }
    }

    @Test(priority = 4)
    public void passwordFieldValidation() {
        // Task 4: Password field validation

        passwordInput.clear();
        submitButton.click();

        if (passwordError.getText().equals("Please enter your password!")) {
            System.out.println("Verified that the empty password field is detected");
        }
        else {
            System.out.println("Failed to verify the error for password");
        }
    }

    @Test(priority = 5)
    public void checkboxValidation() {
        // Task 5: Checkbox validation
        if (checkbox.isSelected()) {
            checkbox.click(); // uncheck the box if checked
        }
        submitButton.click();
        if (checkboxError.getText().equals("You must agree before submitting.")) {
            System.out.println("Verified the checkbox is unchecked.");
        }
        else {
            System.out.println("Failed to verify the checkbox click.");
        }
    }

    @Test(priority = 6)
    public void captureColors() {
        // Task 6
        // Using already filled elements; emailError & emailInput were set in Task 2
        String messageError = emailError.getCssValue("color");
        String messageBorder = emailInput.getCssValue("border-color");

        System.out.println("Error text color: " + messageError);
        System.out.println("Textbox border color: " + messageBorder);
    }

    @Test(priority = 7)
    public void errorPositionCheck() {
        // Task 7
        int fieldY = usernameInput.getLocation().getY();
        int errorY = usernameError.getLocation().getY();
        if (errorY > fieldY) {
            System.out.println("Error message is displayed below the textbox.");
        }
    }

    // No @AfterClass — BaseUiTest.baseTeardown() handles driver.quit()
}