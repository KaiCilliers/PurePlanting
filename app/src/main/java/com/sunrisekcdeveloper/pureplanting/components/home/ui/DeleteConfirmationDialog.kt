package com.sunrisekcdeveloper.pureplanting.components.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.library.design.theme.neutralus100
import com.sunrisekcdeveloper.pureplanting.library.design.ui.PrimaryButton
import com.sunrisekcdeveloper.pureplanting.library.design.ui.SecondaryButton
import com.sunrisekcdeveloper.pureplanting.R
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun DeleteConfirmationDialog(
    plantName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.delete_icon),
                    modifier = Modifier
                        .padding(end = 10.dp),
                    contentDescription = "",
                )
                Text(text = "Are you sure?", Modifier.weight(1f))
            }
        },
        text = {
            Text(text = "Do you really want to delete your plant \"$plantName\"?")
        },
        containerColor = neutralus100,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            PrimaryButton(
                onClick = { onConfirm() },
                label = "Got it"
            )
        },
        dismissButton = {
            SecondaryButton(
                onClick = { onDismiss() },
                label = "Cancel"
            )
        }
    )
}

@Preview
@Composable
private fun DeleteConfirmationDialog_Preview() {
    ThemeSurfaceWrapper {
        DeleteConfirmationDialog(
            plantName = "Rose",
            onConfirm = {},
            onDismiss = {}
        )
    }
}