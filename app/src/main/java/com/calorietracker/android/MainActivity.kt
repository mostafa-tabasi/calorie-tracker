package com.calorietracker.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.utils.navigation.Route
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                ) { paddingValues ->
                    val shouldShowOnboarding = preferences.loadShouldShowOnboarding()
                    NavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        startDestination = if (shouldShowOnboarding) Route.Welcome
                        else Route.TrackerOverview,
                    ) {
                        composable<Route.Welcome> {
                            WelcomeScreen(onNavigate = { navController.navigate(it.route) })
                        }
                        composable<Route.Gender> {
                            GenderScreen(onNavigate = { navController.navigate(it.route) })
                        }
                        composable<Route.Age> {
                            AgeScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = { navController.navigate(it.route) },
                            )
                        }
                        composable<Route.Height> {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = { navController.navigate(it.route) },
                            )
                        }
                        composable<Route.Weight> {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = { navController.navigate(it.route) },
                            )
                        }
                        composable<Route.Activity> {
                            ActivityLevelScreen(onNavigate = { navController.navigate(it.route) })
                        }
                        composable<Route.Goal> {
                            GoalScreen(onNavigate = { navController.navigate(it.route) })
                        }
                        composable<Route.NutrientGoal> {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = { navController.navigate(it.route) },
                            )
                        }
                        composable<Route.TrackerOverview> {
                            TrackerOverviewScreen(onNavigate = { navController.navigate(it.route) })
                        }
                        composable<Route.Search> {
                            val args = it.toRoute<Route.Search>()
                            SearchScreen(
                                scaffoldState = scaffoldState,
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