package com.calorietracker.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.calorietracker.android.navigation.Route
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.onboarding.presentation.activity_level.ActivityLevelScreen
import com.calorietracker.onboarding.presentation.age.AgeScreen
import com.calorietracker.onboarding.presentation.gender.GenderScreen
import com.calorietracker.onboarding.presentation.goal.GoalScreen
import com.calorietracker.onboarding.presentation.height.HeightScreen
import com.calorietracker.onboarding.presentation.nutrient_goal.NutrientGoalScreen
import com.calorietracker.onboarding.presentation.weight.WeightScreen
import com.calorietracker.onboarding.presentation.welcome.WelcomeScreen
import com.calorietracker.tracker.presentation.search.SearchScreen
import com.calorietracker.tracker.presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics {
                            testTagsAsResourceId = true
                        },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                ) { innerPadding ->
                    val shouldShowOnboarding = preferences.loadShouldShowOnboarding()
                    NavHost(
                        navController = navController,
                        startDestination = if (shouldShowOnboarding) Route.Welcome
                        else Route.TrackerOverview,
                    ) {
                        composable<Route.Welcome> {
                            WelcomeScreen(onNext = { navController.navigate(Route.Gender) })
                        }
                        composable<Route.Gender> {
                            GenderScreen(
                                innerPadding = innerPadding,
                                onNext = { navController.navigate(Route.Age) },
                            )
                        }
                        composable<Route.Age> {
                            AgeScreen(
                                innerPadding = innerPadding,
                                snackbarHostState = snackbarHostState,
                                onNext = { navController.navigate(Route.Height) },
                            )
                        }
                        composable<Route.Height> {
                            HeightScreen(
                                innerPadding = innerPadding,
                                snackbarHostState = snackbarHostState,
                                onNext = { navController.navigate(Route.Weight) },
                            )
                        }
                        composable<Route.Weight> {
                            WeightScreen(
                                innerPadding = innerPadding,
                                snackbarHostState = snackbarHostState,
                                onNext = { navController.navigate(Route.Activity) },
                            )
                        }
                        composable<Route.Activity> {
                            ActivityLevelScreen(
                                innerPadding = innerPadding,
                                onNext = { navController.navigate(Route.Goal) })
                        }
                        composable<Route.Goal> {
                            GoalScreen(
                                innerPadding = innerPadding,
                                onNext = { navController.navigate(Route.NutrientGoal) })
                        }
                        composable<Route.NutrientGoal> {
                            NutrientGoalScreen(
                                innerPadding = innerPadding,
                                snackbarHostState = snackbarHostState,
                                onNext = { navController.navigate(Route.TrackerOverview) },
                            )
                        }
                        composable<Route.TrackerOverview> {
                            TrackerOverviewScreen(
                                innerPadding = innerPadding,
                                onNavigateToSearch = { mealType, dayOfMonth, month, year ->
                                    navController.navigate(
                                        Route.Search(
                                            mealName = mealType,
                                            dayOfMonth = dayOfMonth,
                                            month = month,
                                            year = year,
                                        )
                                    )
                                })
                        }
                        composable<Route.Search> {
                            val args = it.toRoute<Route.Search>()
                            SearchScreen(
                                innerPadding = innerPadding,
                                snackbarHostState = snackbarHostState,
                                onNavigateUp = navController::navigateUp,
                                mealName = args.mealName,
                                dayOfMonth = args.dayOfMonth,
                                month = args.month,
                                year = args.year,
                            )
                        }
                    }
                }
            }
        }
    }
}