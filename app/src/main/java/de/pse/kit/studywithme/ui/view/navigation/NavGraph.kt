package de.pse.kit.studywithme.ui.view.navigation

import android.util.Log
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

/**
 * Manages navigation through the application by buttons like the buttons on the buttom bar
 *
 * @property route
 * @property arguments
 * @constructor Create empty Nav graph
 */
sealed class NavGraph(val route: String, val arguments: List<NamedNavArgument>? = null) {
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
        route = "$editSessionName/{groupID}/{$id}",
        arguments = listOf(
            navArgument("groupID") {
                type = NavType.IntType
                nullable = false
            },
            navArgument(id) {
                type = NavType.IntType
                nullable = false
            })
    ) {
        const val groupID = "groupID"
        const val sessionID = id
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

        fun navigateToEditSession(navController: NavController, groupID: Int, sessionID: Int) {
            navController.navigate("$editSessionName/$groupID/$sessionID")
        }

        fun navigateToJoinedGroups(navController: NavController) {
            navigateToTab(navController, JoinedGroupsTab.route)
        }

        fun navigateToSearchGroups(navController: NavController) {
            navigateToTab(navController, SearchGroupsTab.route)
        }

        fun navigateToProfile(navController: NavController) {
            navigateToTab(navController, ProfileTab.route)
        }

        fun navigateToSignIn(navController: NavController) {
            navigateToTab(navController, SignInForm.route)
        }

        fun navigateToSignUp(navController: NavController) {
            navController.navigate(SignUp.route)
        }

        fun navigateToEditProfile(navController: NavController) {
            navController.navigate(EditProfile.route)
        }

        fun navigateToNewGroup(navController: NavController) {
            navController.navigate(NewGroup.route)
        }

        private fun navigateToTab(navController: NavController, route: String) {
            navController.navigate(route) {
                val from = navController.currentDestination?.route
                if ((from == SignIn.route && route != SignUp.route) || (from == SignUp.route && route != SignIn.route)) {
                    navController.backQueue.clear()
                } else if (from != SignUp.route && route == SignInForm.route){
                    navController.backQueue.clear()
                }

                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }

                launchSingleTop = true

                restoreState = true
            }
        }
    }
}
