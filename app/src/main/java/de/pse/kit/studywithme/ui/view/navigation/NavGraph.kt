package de.pse.kit.studywithme.ui.view.navigation

import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination

const val signInName = "signIn"
const val signUpName = "signUp"
const val joinedGroupDetailsName = "joinedGroupDetails"
const val nonJoinedGroupDetailsName = "nonJoinedGroupDetails"
const val editGroupName = "editGroup"
const val newSessionName = "newSession"
const val editSessionName = "editSession"

const val id = "id"

sealed class NavGraph(val route: String, val arguments: List<NamedNavArgument>? = null) {
    object Loading : NavGraph("loading")
    object SignUp : NavGraph(signUpName)
    object SignInForm : NavGraph("signInForm")
    object SignIn : NavGraph(signInName)
    object ProfileTab : NavGraph("profileTab")
    object Profile : NavGraph("profile")
    object EditProfile : NavGraph("editProfile")
    object JoinedGroupsTab : NavGraph("joinedGroupsTab")
    object JoinedGroups : NavGraph("joinedGroups")
    object SearchGroupsTab : NavGraph("searchGroupsTab")
    object SearchGroups : NavGraph("searchGroups")
    object JoinedGroupDetails : NavGraph(
        route = "$joinedGroupDetailsName/{$id}",
        arguments = listOf(navArgument(id) {
            type = NavType.IntType
            nullable = false
        })
    ) {
        const val argName = id
    }

    object NonJoinedGroupDetails : NavGraph(
        route = "$nonJoinedGroupDetailsName/{$id}",
        arguments = listOf(navArgument(id) {
            type = NavType.IntType
            nullable = false
        })
    ) {
        const val argName = id
    }

    object NewGroup : NavGraph("newGroup")
    object EditGroup : NavGraph(
        route = "$editGroupName/{$id}",
        arguments = listOf(navArgument(id) {
            type = NavType.IntType
            nullable = false
        })
    ) {
        const val argName = id
    }

    object NewSession : NavGraph(
        route = "$newSessionName/{$id}",
        arguments = listOf(navArgument(id) {
            type = NavType.IntType
            nullable = false
        })
    ) {
        const val argName = id
    }

    object EditSession : NavGraph(
        route = "$editSessionName/{$id}",
        arguments = listOf(navArgument(id) {
            type = NavType.IntType
            nullable = false
        })
    ) {
        const val argName = id
    }

    companion object {
        fun navigateToJoinedGroup(navController: NavController, groupID: Int) {
            navController.navigate("$joinedGroupDetailsName/$groupID")
        }

        fun navigateToNonJoinedGroup(navController: NavController, groupID: Int) {
            navController.navigate("$nonJoinedGroupDetailsName/$groupID")
        }

        fun navigateToEditGroup(navController: NavController, groupID: Int) {
            navController.navigate("$editGroupName/$groupID")
        }

        fun navigateToNewSession(navController: NavController, groupID: Int) {
            navController.navigate("$newSessionName/$groupID")
        }

        fun navigateToEditSession(navController: NavController, sessionID: Int) {
            navController.navigate("$editSessionName/$sessionID")
        }

        fun navigateToJoinedGroups(navController: NavController) {
            navController.navigate("joinedGroups") {

            }
        }

        fun navigateToTab(navController: NavController, route: String) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }

                launchSingleTop = true

                restoreState = true
            }
        }
    }
}
