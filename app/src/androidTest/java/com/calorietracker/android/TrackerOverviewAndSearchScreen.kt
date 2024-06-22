package com.calorietracker.android

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.calorietracker.android.data.repository.TrackerRepositoryFake
import com.calorietracker.android.navigation.Route
import com.calorietracker.core.domain.model.ActivityLevel
import com.calorietracker.core.domain.model.Gender
import com.calorietracker.core.domain.model.Goal
import com.calorietracker.core.domain.model.UserInfo
import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.tracker.domain.model.TrackableFood
import com.calorietracker.tracker.domain.use_case.CalculateMealNutrients
import com.calorietracker.tracker.domain.use_case.DeleteTrackedFood
import com.calorietracker.tracker.domain.use_case.GetFoodsForDate
import com.calorietracker.tracker.domain.use_case.SearchFood
import com.calorietracker.tracker.domain.use_case.TrackFood
import com.calorietracker.tracker.domain.use_case.TrackerUseCases
import com.calorietracker.tracker.presentation.search.SearchScreen
import com.calorietracker.tracker.presentation.search.SearchViewModel
import com.calorietracker.tracker.presentation.tracker_overview.TrackerOverviewScreen
import com.calorietracker.tracker.presentation.tracker_overview.TrackerOverviewViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewAndSearchScreen {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @OptIn(ExperimentalComposeUiApi::class)
    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 32,
            weight = 62f,
            height = 174,
            activityLevel = ActivityLevel.High,
            goal = Goal.GainWeight,
            carbRatio = 0.3f,
            proteinRatio = 0.5f,
            fatRatio = 0.2f,
        )
        repositoryFake = TrackerRepositoryFake()
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(repositoryFake),
            searchFood = SearchFood(repositoryFake),
            getFoodsForDate = GetFoodsForDate(repositoryFake),
            deleteTrackedFood = DeleteTrackedFood(repositoryFake),
            calculateMealNutrients = CalculateMealNutrients(preferences),
        )
        trackerOverviewViewModel = TrackerOverviewViewModel(preferences, trackerUseCases)
        searchViewModel = SearchViewModel(
            trackerUseCases,
            filterOutNumber = FilterOutNumber(),
        )

        composeRule.activity.setContent {
            CalorieTrackerTheme {
                navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics {
                            testTagsAsResourceId = true
                        },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.TrackerOverview,
                    ) {
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
                                },
                                viewModel = trackerOverviewViewModel,
                            )
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
                                viewModel = searchViewModel,
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast() {
        repositoryFake.searchResult = listOf(
            TrackableFood(
                name = "food",
                imageUrl = null,
                caloriesPer100g = 150,
                proteinsPer100g = 5,
                carbsPer100g = 50,
                fatsPer100g = 1,
            )
        )
        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule
            .onNodeWithTag("trackerOverview:addButton:breakfast")
            .assertDoesNotExist()
        composeRule
            .onNodeWithTag("trackerOverview:toggleButton:breakfast")
            .performClick()
        composeRule
            .onNodeWithTag("trackerOverview:addButton:breakfast")
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag("trackerOverview:addButton:breakfast")
            .performClick()

        assertThat(navController.currentDestination?.route)
            .startsWith(Route.Search::class.qualifiedName)

        composeRule
            .onNodeWithTag("search:searchTextField")
            .performTextInput("food")
        composeRule
            .onNodeWithTag("search:searchButton")
            .performClick()
        composeRule
            .onNodeWithTag("search:trackableFoodLayout:food")
            .performClick()
        composeRule
            .onNodeWithTag("search:amountTextField:food")
            .performTextInput(addedAmount.toString())
        composeRule
            .onNodeWithTag("search:trackButton:food")
            .performClick()

        assertThat(navController.currentDestination?.route)
            .startsWith(Route.TrackerOverview::class.qualifiedName)

        composeRule
            .onAllNodesWithText(expectedCalories.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedCarbs.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedProtein.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedFat.toString())
            .onFirst()
            .assertIsDisplayed()
    }
}