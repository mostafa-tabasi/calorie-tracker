package com.calorietracker.onboarding_domain.di

import com.calorietracker.onboarding_domain.use_cases.ValidateNutrients
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object OnboardingDomainModule {

    @Provides
    @ViewModelScoped
    fun provideValidateNutrients(): ValidateNutrients {
        return ValidateNutrients()
    }
}