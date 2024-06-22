package com.calorietracker.tracker.presentation.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing
import com.calorietracker.tracker.domain.model.TrackableFood
import com.calorietracker.tracker.presentation.component.NutrientInfo
import com.calorietracker.tracker.presentation.search.TrackableFoodUiState


@Composable
fun ExpandableTrackableFood(
    modifier: Modifier = Modifier,
    foodState: TrackableFoodUiState,
    onToggleChange: () -> Unit,
    onAmountChange: (String) -> Unit,
    onTrackClick: () -> Unit,
    amountTestTag: String = "",
    trackButtonTestTag: String = "",
) {
    val food = foodState.food
    val spacing = LocalSpacing.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        modifier = modifier
            .padding(spacing.extraSmall)
            .background(MaterialTheme.colors.surface)
            .clickable { onToggleChange() },
        shape = RoundedCornerShape(spacing.small),
        elevation = spacing.extraSmall,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(food.imageUrl)
                        .crossfade(true)
                        .fallback(R.drawable.ic_burger)
                        .error(R.drawable.ic_burger)
                        .build(),
                    contentDescription = food.name,
                    placeholder = painterResource(R.drawable.ic_burger),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .aspectRatio(1f),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = spacing.small),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = food.name,
                        color = MaterialTheme.colors.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(spacing.small))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.kcal_per_100g,
                                food.caloriesPer100g
                            ),
                            style = MaterialTheme.typography.body2,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        NutrientInfo(
                            name = stringResource(id = R.string.carbs),
                            nameTextSize = 12.sp,
                            amount = food.carbsPer100g,
                            amountTextSize = 16.sp,
                            unit = stringResource(id = R.string.grams),
                            unitTextSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(spacing.small))
                        NutrientInfo(
                            name = stringResource(id = R.string.protein),
                            nameTextSize = 12.sp,
                            amount = food.proteinsPer100g,
                            amountTextSize = 16.sp,
                            unit = stringResource(id = R.string.grams),
                            unitTextSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(spacing.small))
                        NutrientInfo(
                            name = stringResource(id = R.string.fat),
                            nameTextSize = 12.sp,
                            amount = food.fatsPer100g,
                            amountTextSize = 16.sp,
                            unit = stringResource(id = R.string.grams),
                            unitTextSize = 12.sp
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = foodState.isExpanded,
                enter = slideInVertically()
                        + expandVertically(expandFrom = Alignment.Top)
                        + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically()
                        + shrinkVertically()
                        + fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.medium, vertical = spacing.small),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        modifier = Modifier
                            .widthIn(max = 150.dp)
                            .testTag(amountTestTag),
                        value = foodState.amount,
                        onValueChange = onAmountChange,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.amount),
                                color = Color.Gray,
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = if (foodState.amount.isNotBlank()) ImeAction.Done
                            else ImeAction.Default,
                            keyboardType = KeyboardType.Number,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onTrackClick()
                                keyboardController?.hide()
                                defaultKeyboardAction(ImeAction.Done)
                            }
                        ),
                    )
                    IconButton(
                        modifier = Modifier.testTag(trackButtonTestTag),
                        onClick = {
                            onTrackClick()
                            keyboardController?.hide()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(id = R.string.track),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandableTrackableFoodPreview() {
    CalorieTrackerTheme {
        ExpandableTrackableFood(
            foodState = TrackableFoodUiState(
                food = TrackableFood(
                    name = "Burger",
                    imageUrl = null,
                    caloriesPer100g = 177,
                    carbsPer100g = 42,
                    proteinsPer100g = 84,
                    fatsPer100g = 51,
                ),
                isExpanded = true,
                amount = "123",
            ),
            onAmountChange = {},
            onTrackClick = {},
            onToggleChange = {},
        )
    }
}
