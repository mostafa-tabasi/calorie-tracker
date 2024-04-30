package com.calorietracker.core.di

import com.calorietracker.core.domain.use_cases.FilterOutNumber
import com.calorietracker.core.domain.use_cases.ValidateNumber
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
    fun provideValidateNumberUseCase(): ValidateNumber {
        return ValidateNumber()
    }
}