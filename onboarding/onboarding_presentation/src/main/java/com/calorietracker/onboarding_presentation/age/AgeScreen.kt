package com.calorietracker.onboarding_presentation.age

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
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core_ui.theme.CalorieTrackerTheme
import com.calorietracker.core_ui.theme.LocalSpacing
import com.calorietracker.onboarding_presentation.components.ActionButton
import com.calorietracker.onboarding_presentation.components.DescriptionText
import com.calorietracker.onboarding_presentation.components.UnitTextField

@Composable
fun AgeScreen(
    scaffoldState: ScaffoldState,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: AgeViewModel = hiltViewModel(),
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

    AgeScreenLayout(
        age = viewModel.age,
        onAgeChange = viewModel::onAgeChange,
        onNextClick = viewModel::onNextClick
    )
}

@Composable
private fun AgeScreenLayout(
    age: String,
    onAgeChange: (String) -> Unit,
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
            description = stringResource(R.string.whats_your_age),
        )
        AgeTextField(age, onAgeChange)
        NextButton(onNextClick)
    }
}

@Composable
private fun layoutsConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val questionText = createRefFor("questionText")
        val ageTextField = createRefFor("ageTextField")
        val nextButton = createRefFor("nextButton")

        constrain(questionText) { top.linkTo(parent.top) }
        constrain(ageTextField) { bottom.linkTo(parent.bottom) }
        constrain(nextButton) {
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
        createVerticalChain(questionText, ageTextField, chainStyle = ChainStyle.Packed)
    }
}

@Composable
private fun AgeTextField(
    age: String,
    onAgeChange: (String) -> Unit,
) {
    UnitTextField(
        value = age,
        onValueChange = { onAgeChange(it) },
        unit = stringResource(id = R.string.years),
        modifier = Modifier
            .fillMaxWidth()
            .layoutId("ageTextField")
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
        AgeScreenLayout("17", {}, {})
    }
}