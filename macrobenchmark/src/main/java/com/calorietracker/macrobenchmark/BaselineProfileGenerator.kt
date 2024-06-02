package com.calorietracker.macrobenchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baselineProfileRule.collect(
        packageName = "com.calorietracker.android",
    ) {
        pressHome()
        startActivityAndWait()
        device.wait(Until.hasObject(By.textContains("find out more about you :)")), 3_000)

        var nextButton = device.findObject(By.res("welcome:nextButton"))
        nextButton.click()
        device.wait(Until.hasObject(By.text("What is your gender?")), 3_000)

        nextButton = device.findObject(By.res("gender:nextButton"))
        nextButton.click()
        device.wait(Until.hasObject(By.text("What\'s your age?")), 3_000)
    }
}