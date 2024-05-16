package com.calorietracker.onboarding.presentation.nutrient_goal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.onboarding.presentation.components.ActionButton
import com.calorietracker.onboarding.presentation.components.DescriptionText
import com.calorietracker.onboarding.presentation.components.UnitTextField

@Composable
fun NutrientGoalScreen(
    scaffoldState: ScaffoldState,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: NutrientGoalViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> onNavigate(it)
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.message.asString(context),
                    )
                }

                else -> Unit
            }
        }
    }

    NutrientGoalScreenLayout(
        carbs = viewModel.state.carbsRatio,
        proteins = viewModel.state.proteinsRatio,
        fats = viewModel.state.fatsRatio,
        onCarbsChange = { viewModel.onEvent(NutrientGoalEvent.OnCarbsRatioChange(it)) },
        onProteinsChange = { viewModel.onEvent(NutrientGoalEvent.OnProteinsRatioChange(it)) },
        onFatsChange = { viewModel.onEvent(NutrientGoalEvent.OnFatsRatioChange(it)) },
        onNextClick = { viewModel.onEvent(NutrientGoalEvent.OnNextClick) },
    )
}

@Composable
private fun NutrientGoalScreenLayout(
    carbs: String,
    proteins: String,
    fats: String,
    onCarbsChange: (String) -> Unit,
    onProteinsChange: (String) -> Unit,
    onFatsChange: (String) -> Unit,
    onNextClick: () -> Unit,
) {
    val spacing = LocalSpacing.current
    ConstraintLayout(
        constraintSet = layoutsConstraintSet(),
        Modifier
            .fillMaxSize()
            .padding(spacing.medium),
    ) {
        DescriptionText(
            id = "questionText",
            description = stringResource(R.string.what_are_your_nutrient_goals),
        )
        NutrientsTextField(
            id = "carbsTextField",
            value = carbs,
            unit = stringResource(id = R.string.percent_carbs),
            onValueChange = onCarbsChange,
        )
        NutrientsTextField(
            "proteinsTextField",
            value = proteins,
            unit = stringResource(id = R.string.percent_proteins),
            onValueChange = onProteinsChange,
        )
        NutrientsTextField(
            "fatsTextField",
            value = fats,
            unit = stringResource(id = R.string.percent_fats),
            onValueChange = onFatsChange,
        )
        NextButton(onNextClick)
    }
}

@Composable
private fun layoutsConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val questionText = createRefFor("questionText")
        val carbsTextField = createRefFor("carbsTextField")
        val proteinsTextField = createRefFor("proteinsTextField")
        val fatsTextField = createRefFor("fatsTextField")
        val nextButton = createRefFor("nextButton")

        constrain(questionText) { top.linkTo(parent.top) }
        constrain(fatsTextField) { bottom.linkTo(parent.bottom) }
        constrain(nextButton) {
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
        createVerticalChain(
            questionText,
            carbsTextField,
            proteinsTextField,
            fatsTextField,
            chainStyle = ChainStyle.Packed
        )
    }
}

@Composable
private fun NutrientsTextField(
    id: String,
    value: String,
    unit: String,
    onValueChange: (String) -> Unit,
) {
    UnitTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        unit = unit,
        modifier = Modifier
            .fillMaxWidth()
            .layoutId(id)
    )
}

@Composable
private fun NextButton(
    onNextClick: () -> Unit,
) {
    ActionButton(
        text = stringResource(id = R.string.next),
        modifier = Modifier.layoutId("nextButton"),
        onClick = onNextClick,
        isEnabled = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun AgeScreenPreview() {
    CalorieTrackerTheme {
        NutrientGoalScreenLayout("17", "25", "52", {}, {}, {}, {})
    }
}