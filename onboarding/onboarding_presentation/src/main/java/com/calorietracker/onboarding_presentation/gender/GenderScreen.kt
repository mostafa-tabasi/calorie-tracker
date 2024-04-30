package com.calorietracker.onboarding_presentation.gender

import androidx.compose.foundation.layout.Arrangement
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
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorietracker.core.R
import com.calorietracker.core.domain.models.Gender
import com.calorietracker.core.utils.UiEvent
import com.calorietracker.core_ui.theme.CalorieTrackerTheme
import com.calorietracker.core_ui.theme.LocalSpacing
import com.calorietracker.onboarding_presentation.components.ActionButton
import com.calorietracker.onboarding_presentation.components.DescriptionText
import com.calorietracker.onboarding_presentation.components.SelectableButton

@Composable
fun GenderScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: GenderViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> onNavigate(it)
                else -> Unit
            }
        }
    }

    GenderScreenLayout(
        selectedGender = viewModel.selectedGender,
        onGenderClick = viewModel::onGenderClick,
        onNextClick = viewModel::onNextClick,
    )
}

@Composable
fun GenderScreenLayout(
    selectedGender: Gender,
    onGenderClick: (Gender) -> Unit,
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
            description = stringResource(R.string.whats_your_gender),
        )
        Row(
            modifier = Modifier
                .layoutId("selectableButtons")
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GenderButton(
                genderTitle = stringResource(id = R.string.male),
                isSelected = selectedGender is Gender.Male,
                onGenderClick = { onGenderClick(Gender.Male) },
            )
            Spacer(modifier = Modifier.width(spacing.medium))
            GenderButton(
                genderTitle = stringResource(id = R.string.female),
                isSelected = selectedGender is Gender.Female,
                onGenderClick = { onGenderClick(Gender.Female) },
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
private fun GenderButton(
    genderTitle: String,
    isSelected: Boolean,
    onGenderClick: () -> Unit,
) {
    SelectableButton(
        text = genderTitle,
        isSelected = isSelected,
        textStyle = MaterialTheme.typography.button.copy(
            fontWeight = FontWeight.Normal
        ),
        color = MaterialTheme.colors.primaryVariant,
        selectedTextColor = MaterialTheme.colors.onPrimary,
        onClick = onGenderClick,
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
private fun GenderScreenPreview() {
    CalorieTrackerTheme {
        GenderScreenLayout(selectedGender = Gender.Male, {}, {})
    }
}