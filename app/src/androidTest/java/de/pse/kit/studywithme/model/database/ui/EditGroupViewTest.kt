package de.pse.kit.studywithme.model.database.ui

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.ui.view.navigation.MainView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class EditGroupViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {

    }
    /**
     * /FA130/
     */
    @Test
    @Ignore
    fun changeGroupInformation() {
        val groupNameField = composeTestRule.onNode(hasContentDescription("GroupNameField"))
        val saveButton = composeTestRule.onNode(hasTestTag("SaveButton"))

        groupNameField.assertExists()
        groupNameField.performClick().performTextClearance()
        groupNameField.performClick().performTextInput("NewGroupName")
        saveButton.assertExists()
        saveButton.performClick()
        composeTestRule.onNode(hasContentDescription("JoinedGroupDetailsView")).assertExists()
        composeTestRule.onNode(hasContentDescription("TopBarTitle") and hasText("NewGroupName"))
    }
    /**
     * /FA 150/
     */
    @Test
    @Ignore
    fun hideGroup() {
        val hideButton = composeTestRule.onNode(hasTestTag("HideButton"))
        composeTestRule.onNode(hasTestTag("HideButton") and hasText("Für andere ausblenden")).assertExists()
        composeTestRule.onNode(hasTestTag("HideButton") and hasText("Für andere einblenden")).assertDoesNotExist()
        hideButton.assertExists()
        composeTestRule.onNode(hasTestTag("HideButton") and hasClickAction()).assertExists()
        hideButton.performClick()
        composeTestRule.onNode(hasTestTag("HideButton") and hasText("Für andere ausblenden")).assertDoesNotExist()
        composeTestRule.onNode(hasTestTag("HideButton") and hasText("Für andere einblenden")).assertExists()
    }

    /**
     * /FA 170/
     */
    @Test
    @Ignore
    fun deleteGroup() {
        composeTestRule.setContent {
            MainView(
                userRepo = FakeUserRepository(true),
                groupRepo = FakeGroupRepository(),
                sessionRepo = FakeSessionRepository()
            )
        }
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Die lahmen Coder")).assertExists()
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Die lahmen Coder")).performClick()
        composeTestRule.onNode(hasContentDescription("Knopf um die Gruppe zu editieren")).assertExists()
        composeTestRule.onNode(hasContentDescription("Knopf um die Gruppe zu editieren")).performClick()
        composeTestRule.onNode(hasContentDescription("EditGroupView")).assertExists()
        val deleteButton = composeTestRule.onNode(hasTestTag("DeleteButton"))
        deleteButton.assertExists()
        deleteButton.performClick()
        composeTestRule.onNode(hasContentDescription("JoinedGroupsView")).assertExists()
        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Die lahmen Coder")).assertDoesNotExist()
        //val searchGroupsTab = composeTestRule.onNode(hasContentDescription("SearchGroupsTab"))
        //searchGroupsTab.assertExists()
        //searchGroupsTab.performClick()
        //composeTestRule.onNode(hasContentDescription("SearchGroupsView")).assertExists()

    }
}