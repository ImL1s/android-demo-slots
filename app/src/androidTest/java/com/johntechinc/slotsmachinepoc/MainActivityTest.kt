package com.johntechinc.slotsmachinepoc

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_buttons_are_displayed() {
        onView(withId(R.id.btn_start)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_stop)).check(matches(isDisplayed()))
    }

    @Test
    fun test_pickers_are_displayed() {
        onView(withId(R.id.wp_1)).check(matches(isDisplayed()))
        onView(withId(R.id.wp_2)).check(matches(isDisplayed()))
        onView(withId(R.id.wp_3)).check(matches(isDisplayed()))
    }
}
