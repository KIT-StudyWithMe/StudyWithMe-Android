package de.pse.kit.studywithme.model.database.ui

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.ui.view.group.JoinedGroupDetailsView
import de.pse.kit.studywithme.ui.view.group.JoinedGroupsView
import de.pse.kit.studywithme.ui.view.navigation.MainView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.BeforeAll

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class JoinedGroupDetailsViewTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    @Before
    fun setUp() {
        composeTestRule.setContent {
            MainView(
                userRepo = FakeUserRepository(true),
                groupRepo = FakeGroupRepository(),
                sessionRepo = FakeSessionRepository()
            )
        }
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Die lahmen Coder")).assertExists()
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Die lahmen Coder")).performClick()
    }
    /**
     * /FA120/
     */
    @Test
    fun showDetailedGroupInformation() {
        composeTestRule.onNode(hasContentDescription("TopBarTitle") and hasText("Die lahmen Coder")).assertExists()
        composeTestRule.onNode(hasContentDescription("TopBarSubTitle") and hasText("Programmieren")).assertExists()
        composeTestRule.onNode(hasText("max.mustermann")).assertExists()
        composeTestRule.onNode(hasText("Bibliothek")).assertExists()
        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Monthly")).assertExists()
        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Online")).assertExists()
        composeTestRule.onNode(hasText("Vorlesung: Kapitel Nr. 1")).assertExists()
        composeTestRule.onNode(hasText("Übungsblatt Nr. 1")).assertExists()
    }

    /**
     * /FA200/
     */
    @Test
    fun confirmSessionParticipation(){
        composeTestRule.onNode(hasText ("Es nehmen 0 teil")).assertExists()
        composeTestRule.onNode(hasContentDescription("Participate-Button") and hasText("Teilnehmen")).assertExists()
        composeTestRule.onNode(hasContentDescription("Participate-Button") and hasText("Teilnehmen")).performClick()
    }


    @Test
    fun leaveGroup(){
        composeTestRule.onNode(hasContentDescription("LeaveGroupButton")).assertExists()
        composeTestRule.onNode(hasContentDescription("LeaveGroupButton")).performClick()
    }



}