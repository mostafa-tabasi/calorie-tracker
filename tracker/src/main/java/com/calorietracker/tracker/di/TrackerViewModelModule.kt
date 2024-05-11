package com.calorietracker.tracker.di

import com.calorietracker.core.domain.prefrences.Preferences
import com.calorietracker.tracker.domain.repository.TrackerRepository
import com.calorietracker.tracker.domain.use_case.CalculateMealNutrients
import com.calorietracker.tracker.domain.use_case.DeleteTrackedFood
import com.calorietracker.tracker.domain.use_case.GetFoodsForDate
import com.calorietracker.tracker.domain.use_case.SearchFood
import com.calorietracker.tracker.domain.use_case.TrackFood
import com.calorietracker.tracker.domain.use_case.TrackerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideTrackerUserCases(
        repository: TrackerRepository,
        preferences: Preferences,
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsForDate = GetFoodsForDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences),
        )
    }
}