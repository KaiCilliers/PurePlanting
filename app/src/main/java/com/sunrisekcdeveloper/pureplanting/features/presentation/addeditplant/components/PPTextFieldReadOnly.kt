package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.app.ui.theme.PurePlantingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PPTextFieldReadOnly(
    text: String,
    onClick: () -> Unit,
) {
    TextField(
        value = text,
        onValueChange = { },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFF9F9F9),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Black
        ),
        trailingIcon = { Icon(imageVector = Icons.Outlined.KeyboardArrowDown, contentDescription = null) },
        shape = CircleShape.copy(
            all = CornerSize(12.dp)
        ),
        readOnly = true,
        enabled = false,
        modifier = Modifier.noRippleClickable { onClick() }
    )
}

@Preview
@Composable
private fun PPTextFieldTapAndDisplay_Preview() {
    PurePlantingTheme {
        PPTextFieldReadOnly(
            text = "awda",
            onClick = {}
        )
    }
}