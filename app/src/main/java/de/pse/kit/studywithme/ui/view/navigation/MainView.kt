package de.pse.kit.studywithme.ui.view.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import de.pse.kit.studywithme.model.data.User
import de.pse.kit.studywithme.model.repository.*
import de.pse.kit.studywithme.ui.component.NavigationBar
import de.pse.kit.studywithme.ui.view.auth.SignInView
import de.pse.kit.studywithme.ui.view.auth.SignUpView
import de.pse.kit.studywithme.ui.view.group.*
import de.pse.kit.studywithme.ui.view.profile.EditProfileView
import de.pse.kit.studywithme.ui.view.profile.ProfileView
import de.pse.kit.studywithme.ui.view.session.EditSessionView
import de.pse.kit.studywithme.ui.view.session.NewSessionView
import de.pse.kit.studywithme.viewModel.auth.SignInViewModel
import de.pse.kit.studywithme.viewModel.auth.SignUpViewModel
import de.pse.kit.studywithme.viewModel.group.*
import de.pse.kit.studywithme.viewModel.profile.EditProfileViewModel
import de.pse.kit.studywithme.viewModel.profile.ProfileViewModel
import de.pse.kit.studywithme.viewModel.session.EditSessionViewModel
import de.pse.kit.studywithme.viewModel.session.NewSessionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Composable
fun MainView(userRepo: UserRepositoryInterface = UserRepository.getInstance(LocalContext.current),
             groupRepo: GroupRepositoryInterface = GroupRepository.getInstance(LocalContext.current),
             sessionRepo: SessionRepositoryInterface = SessionRepository.getInstance(LocalContext.current)) {
    val navController = rememberNavController()
    val startRoute =
        if (userRepo.isSignedIn()) NavGraph.JoinedGroupsTab.route
        else NavGraph.SignInForm.route

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        joinedGroupsGraph(navController, groupRepo, sessionRepo)

        searchGroupsGraph(navController, groupRepo)

        profileGraph(navController, userRepo)

        signInGraph(navController, userRepo)
    }
}

@ExperimentalMaterial3Api
fun NavGraphBuilder.signInGraph(navController: NavController, userRepo: UserRepositoryInterface) {
    navigation(startDestination = NavGraph.SignIn.route, route = NavGraph.SignInForm.route) {
        composable(NavGraph.SignIn.route) {
            SignInView(
                SignInViewModel(
                    navController,
                    userRepo
                )
            )
        }
        composable(NavGraph.SignUp.route) {
            SignUpView(
                SignUpViewModel(
                    navController,
                    userRepo
                )
            )
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.joinedGroupsGraph(navController: NavController, groupRepo: GroupRepositoryInterface, sessionRepo: SessionRepositoryInterface) {
    navigation(
        startDestination = NavGraph.JoinedGroups.route,
        route = NavGraph.JoinedGroupsTab.route
    ) {
        composable(route = NavGraph.JoinedGroups.route) {
            JoinedGroupsView(
                JoinedGroupsViewModel(
                    navController,
                    groupRepo
                )
            )
        }
        composable(
            route = NavGraph.JoinedGroupDetails.route,
            arguments = NavGraph.JoinedGroupDetails.arguments!!
        ) {
            JoinedGroupDetailsView(
                JoinedGroupDetailsViewModel(
                    navController,
                    it.arguments!!.getInt(NavGraph.JoinedGroupDetails.argName),
                    groupRepo,
                    sessionRepo
                )
            )
        }
        composable(
            route = NavGraph.EditGroup.route,
            arguments = NavGraph.EditGroup.arguments!!
        ) {
            EditGroupView(
                EditGroupViewModel(
                    navController,
                    it.arguments!!.getInt(NavGraph.EditGroup.argName)
                )
            )
        }
        composable(
            route = NavGraph.NewSession.route,
            arguments = NavGraph.NewSession.arguments!!
        ) {
            NewSessionView(
                NewSessionViewModel(
                    navController,
                    it.arguments!!.getInt(NavGraph.NewSession.argName),
                    SessionRepository.getInstance(LocalContext.current)
                )
            )
        }
        composable(
            route = NavGraph.EditSession.route,
            arguments = NavGraph.EditSession.arguments!!
        ) {
            EditSessionView(
                EditSessionViewModel(
                    navController,
                    it.arguments!!.getInt(NavGraph.EditSession.argName),
                    sessionRepo
                )
            )
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.searchGroupsGraph(navController: NavController, groupRepo: GroupRepositoryInterface) {
    navigation(
        startDestination = NavGraph.SearchGroups.route,
        route = NavGraph.SearchGroupsTab.route
    ) {
        composable(route = NavGraph.SearchGroups.route) {
            SearchGroupsView(
                SearchGroupsViewModel(
                    navController,
                    groupRepo
                )
            )
        }
        composable(
            route = NavGraph.NonJoinedGroupDetails.route,
            arguments = NavGraph.NonJoinedGroupDetails.arguments!!
        ) {
            NonJoinedGroupDetailsView(
                NonJoinedGroupDetailsViewModel(
                    navController,
                    it.arguments!!.getInt(NavGraph.NonJoinedGroupDetails.argName)
                )
            )
        }
        composable(NavGraph.NewGroup.route) {
            NewGroupView(NewGroupViewModel(navController))
        }
        composable(
            route = NavGraph.EditGroup.route,
            arguments = NavGraph.EditGroup.arguments!!
        ) {
            EditGroupView(
                EditGroupViewModel(
                    navController,
                    it.arguments!!.getInt(NavGraph.EditGroup.argName)
                )
            )
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.profileGraph(navController: NavController, userRepo: UserRepositoryInterface) {
    navigation(
        startDestination = NavGraph.Profile.route,
        route = NavGraph.ProfileTab.route
    ) {
        composable(route = NavGraph.Profile.route) {
            ProfileView(
                ProfileViewModel(
                    navController,
                    userRepo
                )
            )
        }
        composable(route = NavGraph.EditProfile.route) {
            EditProfileView(
                EditProfileViewModel(
                    navController,
                    userRepo
                )
            )
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun MainViewPreview() {
    MainView(FakeUserRepository(signedIn = true), FakeGroupRepository(), FakeSessionRepository())
}