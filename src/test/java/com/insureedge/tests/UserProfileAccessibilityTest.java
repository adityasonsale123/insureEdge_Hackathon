package com.insureedge.tests;

import org.testng.annotations.Test;

public class UserProfileAccessibilityTest extends BaseUiTest {

    @Test
    public void checkProfilePage() {
        driver.get("https://example.com/profile");
        // your assertions here
    }
}