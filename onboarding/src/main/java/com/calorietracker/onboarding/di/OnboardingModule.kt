package com.calorietracker.onboarding.di

import com.calorietracker.onboarding.domain.use_cases.ValidateNutrients
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object OnboardingModule {

    @Provides
    @ViewModelScoped
    fun provideValidateNutrients(): ValidateNutrients {
        return ValidateNutrients()
    }
}