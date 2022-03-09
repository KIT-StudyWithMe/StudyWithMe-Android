package de.pse.kit.studywithme.model.database.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.content.MimeTypeFilter.matches
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import de.pse.kit.studywithme.MainActivity
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.ui.view.auth.SignInView
import de.pse.kit.studywithme.ui.view.group.JoinedGroupsView
import de.pse.kit.studywithme.ui.view.navigation.MainView
import de.pse.kit.studywithme.viewModel.auth.SignInViewModel
import de.pse.kit.studywithme.viewModel.auth.SignInViewModelFactory
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModel
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModelFactory
import io.ktor.utils.io.*
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class SignInViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * UI-Test /FA20/
     *
     */
    @Test
    fun wrongAccessToRightAccess() {
        composeTestRule.setContent {
            MainView(
                userRepo = FakeUserRepository(false),
                groupRepo = FakeGroupRepository(),
                sessionRepo = FakeSessionRepository()
            )
        }

        //For debugging
        composeTestRule.onRoot().printToLog("SIGN_IN_VIEW")

        val buttonEmail = composeTestRule.onNode(hasTestTag("Email-Adresse"))
        val buttonPw = composeTestRule.onNode(hasTestTag("Passwort"))
        val buttonLogin = composeTestRule.onNode(hasTestTag("Anmelden"))

        //LogIn of a non-existing and existing User
        buttonEmail.performClick().performTextInput("hort2@lichter.de")
        buttonPw.performClick().performTextInput("passworthorst98")
        buttonLogin.performClick()
        buttonEmail.performClick().performTextClearance()
        buttonEmail.performClick().performTextInput("max.mustermann@mustermail.com")
        buttonPw.performClick().performTextClearance()
        buttonPw.performClick().performTextInput("Mind6Zeichen")
        buttonLogin.performClick()
        composeTestRule.onNodeWithContentDescription("JoinedGroupsView").assertExists()
    }

    /**
     * UI-Test FA30
     *
     */
    @Test
    fun forgotPw() {
        composeTestRule.setContent {
            MainView(
                userRepo = FakeUserRepository(false),
                groupRepo = FakeGroupRepository(),
                sessionRepo = FakeSessionRepository()
            )
        }

        //For debugging
        composeTestRule.onRoot().printToLog("SIGN_IN_VIEW")

        val buttonPwForget = composeTestRule.onNode(hasTestTag("Passwort vergessen"))
        val buttonEmail = composeTestRule.onNode(hasTestTag("Email-Adresse"))

        //Forgot Pw Button is being pressed
        buttonPwForget.performClick()
        buttonEmail.performClick().performTextInput("dieter.bohlen@dsds.de")
        buttonPwForget.performClick()
        composeTestRule.onNodeWithText("Eine Email zum zurücksetzen wurde gesendet").assertExists()
        composeTestRule.onNodeWithContentDescription("SignInView").assertExists()
    }

    /**
     * UI-Test FA10
     *
     */
    @Test
    fun signUp() {
        composeTestRule.setContent {
            MainView(
                userRepo = FakeUserRepository(false),
                groupRepo = FakeGroupRepository(),
                sessionRepo = FakeSessionRepository()
            )
        }

        //For debugging
        composeTestRule.onRoot().printToLog("SIGN_IN_VIEW")

        val buttonSignUp = composeTestRule.onNode(hasTestTag("RegistrierenSignIn"))
        val buttonEmail = composeTestRule.onNode(hasTestTag("Email-AdresseSignUp"))
        val buttonUser = composeTestRule.onNode(hasTestTag("NutzernameSignUp"))
        val buttonCollege = composeTestRule.onNode(hasTestTag("UniSignUp"))
        val buttonLecture = composeTestRule.onNode(hasTestTag("LectureSignUp"))
        val buttonPw = composeTestRule.onNode(hasTestTag("PwSignUp"))
        val buttonRegister = composeTestRule.onNode(hasTestTag("SignUp"))

        //Registers new User
        buttonSignUp.performClick()
        composeTestRule.onNodeWithContentDescription("SignUpView")
        buttonEmail.performClick().performTextInput("hort2@lichter.de")
        buttonUser.performClick().performTextInput("Horst")
        buttonCollege.performClick().performTextInput("KIT")
        buttonLecture.performClick().performTextInput("Info")
        buttonPw.performClick().performTextInput("Mind6Zeichen")
        buttonRegister.performClick()
        composeTestRule.onNodeWithContentDescription("SearchGroupsView").assertExists()

    }

}