package de.pse.kit.studywithme.model.database.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import de.pse.kit.studywithme.model.repository.FakeGroupRepository
import de.pse.kit.studywithme.model.repository.FakeSessionRepository
import de.pse.kit.studywithme.model.repository.FakeUserRepository
import de.pse.kit.studywithme.ui.view.group.JoinedGroupsView
import de.pse.kit.studywithme.ui.view.navigation.MainView
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModel
import de.pse.kit.studywithme.viewModel.group.JoinedGroupsViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
class JoinedGroupsViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun navigateToSearchGroupViewAndBackTest() {
        composeTestRule.setContent {
            MainView(
                userRepo = FakeUserRepository(true),
                groupRepo = FakeGroupRepository(),
                sessionRepo = FakeSessionRepository()
            )
        }
        // For debugging
        composeTestRule.onRoot().printToLog("NAVIGATION_VIEW")

        val searchGroupsTab = composeTestRule.onNodeWithContentDescription("SearchGroupsTab")
        searchGroupsTab.assertExists()
        searchGroupsTab.performClick()

        composeTestRule.onNodeWithContentDescription("SearchGroupsView").assertExists()

        val myGroupsTab = composeTestRule.onNodeWithContentDescription("MyGroupsTab")
        myGroupsTab.assertExists()
        myGroupsTab.performClick()

        composeTestRule.onNodeWithContentDescription("JoinedGroupsView").assertExists()
    }

    @Test
    fun filterJoinedGroupsTest() {
        composeTestRule.setContent {
            val viewModel: JoinedGroupsViewModel = viewModel(
                factory = JoinedGroupsViewModelFactory(
                    rememberNavController(),
                    FakeGroupRepository()
                )
            )

            JoinedGroupsView(viewModel)
        }
        // For debugging
        composeTestRule.onRoot().printToLog("JOINED_GROUPS_VIEW")

        composeTestRule.onAllNodes(hasContentDescription("SearchGroupResult") and hasText("Programmieren"))[0].assertExists()

        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Lineare Algebra II"))
            .assertExists()

        composeTestRule.onNode(hasContentDescription("Chip") and hasText("Lineare Algebra II"))
            .performClick()

        composeTestRule.onNode(hasContentDescription("SearchGroupResult") and hasText("Programmieren"))
            .assertDoesNotExist()
    }
}