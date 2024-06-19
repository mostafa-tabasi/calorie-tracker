package com.calorietracker.onboarding.presentation.activity_level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorietracker.core.R
import com.calorietracker.core.domain.model.ActivityLevel
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.onboarding.presentation.components.ActionButton
import com.calorietracker.onboarding.presentation.components.DescriptionText
import com.calorietracker.onboarding.presentation.components.SelectableButton

@Composable
fun ActivityLevelScreen(
    innerPadding: PaddingValues,
    onNext: () -> Unit,
    viewModel: ActivityLevelViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.NavigateToNextScreen -> onNext()
                else -> Unit
            }
        }
    }

    ActivityLevelScreenLayout(
        innerPadding = innerPadding,
        selectedLevel = viewModel.selectedLevel,
        onLevelClick = viewModel::onActivityLevelClick,
        onNextClick = viewModel::onNextClick,
    )
}

@Composable
private fun ActivityLevelScreenLayout(
    innerPadding: PaddingValues,
    selectedLevel: ActivityLevel,
    onLevelClick: (ActivityLevel) -> Unit,
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
            description = stringResource(R.string.whats_your_activity_level),
        )
        Row(
            modifier = Modifier
                .layoutId("selectableButtons")
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ActivityLevelButton(
                levelName = stringResource(id = R.string.low),
                isSelected = selectedLevel is ActivityLevel.Low,
                onLevelClick = { onLevelClick(ActivityLevel.Low) },
            )
            Spacer(modifier = Modifier.width(spacing.medium))
            ActivityLevelButton(
                levelName = stringResource(id = R.string.medium),
                isSelected = selectedLevel is ActivityLevel.Medium,
                onLevelClick = { onLevelClick(ActivityLevel.Medium) },
            )
            Spacer(modifier = Modifier.width(spacing.medium))
            ActivityLevelButton(
                levelName = stringResource(id = R.string.high),
                isSelected = selectedLevel is ActivityLevel.High,
                onLevelClick = { onLevelClick(ActivityLevel.High) },
            )
        }
        NextButton(innerPadding, onNextClick)
    }
}

@Composable
private fun layoutsConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val questionText = createRefFor("questionText")
        val selectableButtons = createRefFor("selectableButtons")
        val nextButton = createRefFor("nextButton")

        constrain(nextButton) {
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        }

        constrain(questionText) {
            top.linkTo(parent.top)
        }

        constrain(selectableButtons) {
            bottom.linkTo(parent.bottom)
        }

        createVerticalChain(questionText, selectableButtons, chainStyle = ChainStyle.Packed)
    }
}

@Composable
private fun ActivityLevelButton(
    levelName: String,
    isSelected: Boolean,
    onLevelClick: () -> Unit,
) {
    SelectableButton(
        text = levelName,
        isSelected = isSelected,
        textStyle = MaterialTheme.typography.button.copy(
            fontWeight = FontWeight.Normal
        ),
        color = MaterialTheme.colors.primaryVariant,
        selectedTextColor = MaterialTheme.colors.onPrimary,
        onClick = onLevelClick,
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
private fun ActivityLevelScreenPreview() {
    CalorieTrackerTheme {
        ActivityLevelScreenLayout(
            PaddingValues(0.dp),
            selectedLevel = ActivityLevel.Medium,
            {},
            {},
        )
    }
}