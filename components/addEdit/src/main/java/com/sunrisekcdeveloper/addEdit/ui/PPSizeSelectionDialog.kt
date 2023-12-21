package com.sunrisekcdeveloper.addEdit.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sunrisekcdeveloper.addEdit.models.PlantSize

@Composable
fun PPSizeSelectionDialog(
    dismiss: () -> Unit,
    initialSelection: PlantSize,
    updateSelection: (PlantSize) -> Unit,
) {

    val plantSizeOptions = listOf(PlantSize.Small, PlantSize.Medium, PlantSize.Large, PlantSize.XLarge)
    var currentSelection by remember { mutableStateOf(initialSelection) }

    Dialog(onDismissRequest = { dismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = "Plant size")
                plantSizeOptions.forEach { plantSize ->
                    Row(
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RadioButton(
                            selected = currentSelection == plantSize,
                            onClick = { currentSelection = plantSize }
                        )
                        Text(
                            text = stringResource(plantSize.textResId),
                            modifier = Modifier.clickable { currentSelection = plantSize }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row {
                    Button(onClick = { dismiss() }) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        updateSelection(currentSelection)
                        dismiss()
                    }) {
                        Text(text = "Got it")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PPSingleSelectionDialog_Preview() {

    var selection: PlantSize by remember { mutableStateOf(PlantSize.Medium) }

    PPSizeSelectionDialog(
        dismiss = { },
        initialSelection = selection,
        updateSelection = { selection = it }
    )
}