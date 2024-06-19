package com.calorietracker.onboarding.presentation.age

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun AgeScreen(
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onNext: () -> Unit,
    viewModel: AgeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.NavigateToNextScreen -> onNext()
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = it.message.asString(context),
                    )
                }

                else -> Unit
            }
        }
    }

    AgeScreenLayout(
        innerPadding = innerPadding,
        age = viewModel.age,
        onAgeChange = viewModel::onAgeChange,
        onNextClick = viewModel::onNextClick
    )
}

@Composable
private fun AgeScreenLayout(
    innerPadding: PaddingValues,
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
        NextButton(innerPadding, onNextClick)
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
    innerPadding: PaddingValues,
    onNextClick: () -> Unit,
) {
    ActionButton(
        text = stringResource(id = R.string.next),
        modifier = Modifier
            .layoutId("nextButton")
            .padding(bottom = innerPadding.calculateBottomPadding()),
        onClick = onNextClick,
        isEnabled = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun AgeScreenPreview() {
    CalorieTrackerTheme {
        AgeScreenLayout(
            PaddingValues(0.dp),
            "17",
            {},
            {},
        )
    }
}