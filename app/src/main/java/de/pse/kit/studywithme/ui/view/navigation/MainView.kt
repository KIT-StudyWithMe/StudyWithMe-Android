package de.pse.kit.studywithme.ui.view.navigation

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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
import de.pse.kit.studywithme.viewModel.SignedInViewModel
import de.pse.kit.studywithme.viewModel.auth.SignInViewModel
import de.pse.kit.studywithme.viewModel.auth.SignInViewModelFactory
import de.pse.kit.studywithme.viewModel.auth.SignUpViewModel
import de.pse.kit.studywithme.viewModel.auth.SignUpViewModelFactory
import de.pse.kit.studywithme.viewModel.group.*
import de.pse.kit.studywithme.viewModel.profile.EditProfileViewModel
import de.pse.kit.studywithme.viewModel.profile.EditProfileViewModelFactory
import de.pse.kit.studywithme.viewModel.profile.ProfileViewModel
import de.pse.kit.studywithme.viewModel.profile.ProfileViewModelFactory
import de.pse.kit.studywithme.viewModel.session.EditSessionViewModel
import de.pse.kit.studywithme.viewModel.session.EditSessionViewModelFactory
import de.pse.kit.studywithme.viewModel.session.NewSessionViewModel
import de.pse.kit.studywithme.viewModel.session.NewSessionViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

/**
 * Navigation changes the view the main view shows
 *
 * @param userRepo
 * @param groupRepo
 * @param sessionRepo
 */
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Composable
fun MainView(
    userRepo: UserRepositoryInterface = UserRepository.getInstance(UserRepoConstructor(LocalContext.current)),
    groupRepo: GroupRepositoryInterface = GroupRepository.getInstance(GroupRepoConstructor(LocalContext.current)),
    sessionRepo: SessionRepositoryInterface = SessionRepository.getInstance(SessionRepoConstructor(LocalContext.current))
) {
    val navController = rememberNavController()
    var startRoute = NavGraph.SignInForm.route

    runBlocking {
        if (userRepo.isSignedIn()) startRoute = NavGraph.JoinedGroupsTab.route
    }

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
            val viewModel: SignInViewModel = viewModel(
                factory = SignInViewModelFactory(
                    navController,
                    userRepo
                )
            )

            LaunchedEffect("navigation") {
                viewModel.refreshView()
            }

            SignInView(viewModel)
        }

        composable(NavGraph.SignUp.route) {
            val viewModel: SignUpViewModel = viewModel(
                factory = SignUpViewModelFactory(
                    navController,
                    userRepo
                )
            )

            LaunchedEffect("navigation") {
                viewModel.refreshView()
            }

            SignUpView(viewModel)
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.joinedGroupsGraph(
    navController: NavController,
    groupRepo: GroupRepositoryInterface,
    sessionRepo: SessionRepositoryInterface
) {
    navigation(
        startDestination = NavGraph.JoinedGroups.route,
        route = NavGraph.JoinedGroupsTab.route
    ) {
        composable(route = NavGraph.JoinedGroups.route) {
            val viewModel: JoinedGroupsViewModel = viewModel(
                factory = JoinedGroupsViewModelFactory(
                    navController,
                    groupRepo
                )
            )

            LaunchedEffect("navigation") {
                viewModel.refreshJoinedGroups()
            }

            JoinedGroupsView(
                viewModel
            )
        }

        composable(
            route = NavGraph.JoinedGroupDetails.route,
            arguments = NavGraph.JoinedGroupDetails.arguments!!
        ) {
            val viewModel: JoinedGroupDetailsViewModel = viewModel(
                factory = JoinedGroupDetailsViewModelFactory(
                    navController,
                    it.arguments!!.getInt(NavGraph.JoinedGroupDetails.argName),
                    groupRepo,
                    sessionRepo
                )
            )

            LaunchedEffect("navigation") {
                viewModel.refreshJoinedGroupDetails()
            }

            JoinedGroupDetailsView(viewModel)
        }

        composable(
            route = NavGraph.EditGroup.route,
            arguments = NavGraph.EditGroup.arguments!!
        ) {
            val viewModel: EditGroupViewModel = viewModel(
                factory = EditGroupViewModelFactory(
                    navController,
                    it.arguments!!.getInt(NavGraph.EditGroup.argName),
                    groupRepo
                )
            )

            EditGroupView(viewModel)
        }

        composable(
            route = NavGraph.NewSession.route,
            arguments = NavGraph.NewSession.arguments!!
        ) {
            val viewModel: NewSessionViewModel = viewModel(
                factory = NewSessionViewModelFactory(
                    navController,
                    sessionRepo,
                    groupRepo,
                    it.arguments!!.getInt(NavGraph.NewSession.argName)
                )
            )

            NewSessionView(viewModel)
        }

        composable(
            route = NavGraph.EditSession.route,
            arguments = NavGraph.EditSession.arguments!!
        ) {
            val viewModel: EditSessionViewModel = viewModel(
                factory = EditSessionViewModelFactory(
                    navController,
                    it.arguments!!.getInt(NavGraph.EditSession.sessionID),
                    sessionRepo,
                    groupRepo,
                    it.arguments!!.getInt(NavGraph.EditSession.groupID)
                )
            )

            EditSessionView(viewModel)
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.searchGroupsGraph(
    navController: NavController,
    groupRepo: GroupRepositoryInterface
) {
    navigation(
        startDestination = NavGraph.SearchGroups.route,
        route = NavGraph.SearchGroupsTab.route
    ) {
        composable(route = NavGraph.SearchGroups.route) {
            val viewModel: SearchGroupsViewModel = viewModel(
                factory = SearchGroupsViewModelFactory(
                    navController,
                    groupRepo
                )
            )

            LaunchedEffect("navigation") {
                viewModel.refreshGroups()
            }

            SearchGroupsView(viewModel)
        }

        composable(
            route = NavGraph.NonJoinedGroupDetails.route,
            arguments = NavGraph.NonJoinedGroupDetails.arguments!!
        ) {
            val viewModel: NonJoinedGroupDetailsViewModel = viewModel(
                factory = NonJoinedGroupDetailsViewModelFactory(
                    navController,
                    it.arguments!!.getInt(NavGraph.JoinedGroupDetails.argName),
                    groupRepo
                )
            )

            LaunchedEffect("navigation") {
                viewModel.refreshNonJoinedGroupDetails()
            }

            NonJoinedGroupDetailsView(viewModel)
        }

        composable(NavGraph.NewGroup.route) {
            val viewModel: NewGroupViewModel = viewModel(
                factory = NewGroupViewModelFactory(
                    navController,
                    groupRepo
                )
            )

            NewGroupView(viewModel)
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
            val viewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModelFactory(
                    navController,
                    userRepo
                )
            )

            LaunchedEffect("navigation") {
                viewModel.refreshUser()
            }

            ProfileView(viewModel)
        }

        composable(route = NavGraph.EditProfile.route) {
            val viewModel: EditProfileViewModel = viewModel(
                factory = EditProfileViewModelFactory(
                    navController,
                    userRepo
                )
            )

            EditProfileView(viewModel)
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