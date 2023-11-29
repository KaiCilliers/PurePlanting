package com.sunrisekcdeveloper.addEditPlant.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.navigation.theme.PurePlantingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PPTextField() {

    var testText by remember {
        mutableStateOf("")
    }

    TextField(
        value = testText,
        onValueChange = { testText = it.removeSuffix(" ml") + " ml" },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFF9F9F9),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        shape = CircleShape.copy(
            all = CornerSize(12.dp)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
    )
}

@Preview
@Composable
private fun PPTextField_Preview() {
    PurePlantingTheme {
        PPTextField(

        )
    }
}