package com.example.focuschildapp

import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.focuschildapp.Navigation.Screens
import com.example.focuschildapp.Navigation.SetupNavGraph
import com.example.focuschildapp.com.example.focuschildapp.Firebase.di.AppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {


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
    fun verify_StartDestinationScreen(){
        composableTestRule
            .onNodeWithText("Get Started")
            .assertIsDisplayed()
            .performClick()

        val emailNode = composableTestRule.onNodeWithText("Enter your email")
        emailNode.assertIsDisplayed()
        emailNode.performTextInput("test@test.com")

        val passwordNode = composableTestRule.onNodeWithText("Enter your password")
        passwordNode.assertIsDisplayed()
        passwordNode.performTextInput("test21")

        composableTestRule
            .onNodeWithText("Login")
            .assertIsDisplayed()
            .performClick()

    }
}