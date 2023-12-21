package com.sunrisekcdeveloper.addEdit.ui

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.design.theme.neutralus100
import com.sunrisekcdeveloper.design.theme.neutralus500
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun InputText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedContainerColor = neutralus100,
            unfocusedContainerColor = neutralus100,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.wrapContentHeight(),
        singleLine = true,
        maxLines = maxLines
    )
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
fun InputText_Preview() {
    ThemeSurfaceWrapper {

        var input by remember {
            mutableStateOf("sample input")
        }

        InputText(
            value = input,
            onValueChange = { input = it },
        )
    }
}