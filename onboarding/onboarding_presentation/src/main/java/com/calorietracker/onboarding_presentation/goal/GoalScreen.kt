package com.calorietracker.onboarding_presentation.goal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorietracker.core.R
import com.calorietracker.core.domain.models.Goal
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core_ui.theme.CalorieTrackerTheme
import com.calorietracker.core_ui.theme.LocalSpacing
import com.calorietracker.onboarding_presentation.components.ActionButton
import com.calorietracker.onboarding_presentation.components.SelectableButton

@Composable
fun GoalScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: GoalViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> onNavigate(it)
                else -> Unit
            }
        }
    }

    GoalScreenScreenLayout(
        selectedGoal = viewModel.selectedGoal,
        onGoalClick = viewModel::onGoalClick,
        onNextClick = viewModel::onNextClick,
    )
}

@Composable
private fun GoalScreenScreenLayout(
    selectedGoal: Goal,
    onGoalClick: (Goal) -> Unit,
    onNextClick: () -> Unit,
) {
    val spacing = LocalSpacing.current
    ConstraintLayout(
        constraintSet = layoutsConstraintSet(),
        Modifier
            .fillMaxSize()
            .padding(spacing.medium),
    ) {
        Description()
        Row(
            modifier = Modifier
                .layoutId("selectableButtons")
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GoalButton(
                goalTitle = stringResource(id = R.string.lose),
                isSelected = selectedGoal is Goal.LoseWeight,
                onLevelClick = { onGoalClick(Goal.LoseWeight) },
            )
            Spacer(modifier = Modifier.width(spacing.medium))
            GoalButton(
                goalTitle = stringResource(id = R.string.keep),
                isSelected = selectedGoal is Goal.KeepWeight,
                onLevelClick = { onGoalClick(Goal.KeepWeight) },
            )
            Spacer(modifier = Modifier.width(spacing.medium))
            GoalButton(
                goalTitle = stringResource(id = R.string.gain),
                isSelected = selectedGoal is Goal.GainWeight,
                onLevelClick = { onGoalClick(Goal.GainWeight) },
            )
        }
        NextButton(onNextClick)
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
private fun Description() {
    Text(
        text = stringResource(R.string.lose_keep_or_gain_weight),
        style = MaterialTheme.typography.h1,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .layoutId("questionText")
            .fillMaxWidth()
            .padding(bottom = LocalSpacing.current.medium)
    )
}

@Composable
private fun GoalButton(
    goalTitle: String,
    isSelected: Boolean,
    onLevelClick: () -> Unit,
) {
    SelectableButton(
        text = goalTitle,
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
private fun NextButton(onNextClick: () -> Unit) {
    ActionButton(
        text = stringResource(id = R.string.next),
        modifier = Modifier.layoutId("nextButton"),
        onClick = onNextClick,
        isEnabled = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun ActivityLevelScreenPreview() {
    CalorieTrackerTheme {
        GoalScreenScreenLayout(selectedGoal = Goal.KeepWeight, {}, {})
    }
}