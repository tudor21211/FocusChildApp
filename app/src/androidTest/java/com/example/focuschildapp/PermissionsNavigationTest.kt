package com.example.focuschildapp

import android.provider.Settings
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PermissionsNavigationTest {

    @get:Rule(order = 1)
    val composableTestRule = createAndroidComposeRule<MainActivity>()

            @Before
            fun setupIntents() {
                Intents.init()
            }

            @After
            fun tearDownIntents() {
                Intents.release()
            }


    @Test
    fun testPermissionsNavigation() {

        composableTestRule
            .onNodeWithTag("Usage access")
            .assertIsDisplayed()
            .performClick()

        composableTestRule
            .onNodeWithText("Go to settings")
            .assertIsDisplayed()
            .performClick()

        Intents.intended(IntentMatchers.hasAction(Settings.ACTION_USAGE_ACCESS_SETTINGS))

    }
}
