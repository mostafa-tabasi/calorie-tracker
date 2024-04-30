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
import com.calorietracker.android.utils.navigation.navigate
import com.calorietracker.core.utils.navigation.Route
import com.calorietracker.core_ui.theme.CalorieTrackerTheme
import com.calorietracker.onboarding_presentation.activity_level.ActivityLevelScreen
import com.calorietracker.onboarding_presentation.age.AgeScreen
import com.calorietracker.onboarding_presentation.gender.GenderScreen
import com.calorietracker.onboarding_presentation.goal.GoalScreen
import com.calorietracker.onboarding_presentation.height.HeightScreen
import com.calorietracker.onboarding_presentation.weight.WeightScreen
import com.calorietracker.onboarding_presentation.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                ) {
                    NavHost(
                        modifier = Modifier.padding(it),
                        navController = navController,
                        startDestination = Route.WELCOME,
                    ) {
                        composable(Route.WELCOME) {
                            WelcomeScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.GENDER) {
                            GenderScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate,
                            )
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate,
                            )
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = navController::navigate,
                            )
                        }
                        composable(Route.ACTIVITY) {
                            ActivityLevelScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.GOAL) {
                            GoalScreen(onNavigate = navController::navigate)
                        }
                        composable(Route.NUTRIENT_GOAL) {}
                        composable(Route.TRACKER_OVERVIEW) {}
                        composable(Route.SEARCH) {}
                    }
                }
            }
        }
    }
}