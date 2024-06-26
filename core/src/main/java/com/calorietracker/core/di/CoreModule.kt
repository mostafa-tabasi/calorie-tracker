package com.calorietracker.core.di

import com.calorietracker.core.domain.use_case.FilterOutNumber
import com.calorietracker.core.domain.use_case.ValidateAge
import com.calorietracker.core.domain.use_case.ValidateHeight
import com.calorietracker.core.domain.use_case.ValidateWeight
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CoreModule {

    @Provides
    @ViewModelScoped
    fun provideFilterOutNumberUseCase(): FilterOutNumber {
        return FilterOutNumber()
    }

    @Provides
    @ViewModelScoped
    fun provideValidateAgeUseCase(): ValidateAge {
        return ValidateAge()
    }

    @Provides
    @ViewModelScoped
    fun provideValidateHeightUseCase(): ValidateHeight {
        return ValidateHeight()
    }

    @Provides
    @ViewModelScoped
    fun provideValidateWeightUseCase(): ValidateWeight {
        return ValidateWeight()
    }
}