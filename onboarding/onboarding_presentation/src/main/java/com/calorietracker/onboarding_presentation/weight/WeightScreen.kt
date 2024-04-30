package com.calorietracker.onboarding_presentation.weight

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
fun WeightScreen(
    scaffoldState: ScaffoldState,
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: WeightViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> onNavigate(it)
                is UiEvent.ShowSnackbar ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        it.message.asString(context)
                    )

                else -> Unit
            }
        }
    }

    WeightScreenLayout(
        weight = viewModel.weight,
        onWeightChange = viewModel::onWeightChange,
        onNextClick = viewModel::onNextClick,
    )
}

@Composable
private fun WeightScreenLayout(
    weight: String,
    onWeightChange: (String) -> Unit,
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
            description = stringResource(R.string.whats_your_weight),
        )
        WeightTextField(weight, onWeightChange)
        NextButton(onNextClick)
    }
}

@Composable
private fun layoutsConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val questionText = createRefFor("questionText")
        val weightTextField = createRefFor("weightTextField")
        val nextButton = createRefFor("nextButton")

        constrain(questionText) { top.linkTo(parent.top) }
        constrain(weightTextField) { bottom.linkTo(parent.bottom) }
        constrain(nextButton) {
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
        createVerticalChain(questionText, weightTextField, chainStyle = ChainStyle.Packed)
    }
}

@Composable
private fun WeightTextField(
    weight: String,
    onWeightChange: (String) -> Unit,
) {
    UnitTextField(
        value = weight,
        onValueChange = { onWeightChange(it) },
        unit = stringResource(id = R.string.kg),
        modifier = Modifier
            .fillMaxWidth()
            .layoutId("weightTextField")
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
private fun WeightScreenPreview() {
    CalorieTrackerTheme {
        WeightScreenLayout("174", {}, {})
    }
}