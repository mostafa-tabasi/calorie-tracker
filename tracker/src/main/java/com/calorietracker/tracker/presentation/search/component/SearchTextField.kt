package com.calorietracker.tracker.presentation.search.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.calorietracker.core.R
import com.calorietracker.core.ui.theme.CalorieTrackerTheme
import com.calorietracker.core.ui.theme.LocalSpacing


@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    hint: String,
    hintSize: TextUnit = 16.sp,
    text: String,
    textSize: TextUnit = 16.sp,
    onTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.xxsSmall),
        value = text,
        textStyle = MaterialTheme.typography.h3.copy(
            color = MaterialTheme.colors.onBackground,
            fontSize = textSize,
        ),
        onValueChange = onTextChange,
        placeholder = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = hint,
                color = Color.Gray,
                fontSize = hintSize,
            )
        },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClick()
                keyboardController?.hide()
                defaultKeyboardAction(ImeAction.Search)
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    onSearchClick()
                    keyboardController?.hide()
                },
                modifier = Modifier.padding(horizontal = spacing.extraSmall),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search),
                )
            }
        },
        shape = RoundedCornerShape(spacing.large),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldPreview() {
    CalorieTrackerTheme {
        SearchTextField(
            hint = "Add Breakfast",
            text = "",
            onTextChange = {},
            onSearchClick = {},
        )
    }
}