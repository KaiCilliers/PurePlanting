package com.sunrisekcdeveloper.pureplanting.features.addEdit.ui

import com.sunrisekcdeveloper.pureplanting.core.design.theme.accent500
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.sunrisekcdeveloper.pureplanting.features.addEdit.models.PlantSize
import com.sunrisekcdeveloper.pureplanting.core.design.ui.PrimaryButton
import com.sunrisekcdeveloper.pureplanting.core.design.ui.SecondaryButton

@Composable
fun PlantSizeSelectionDialog(
    onDismiss: () -> Unit,
    initialSelection: PlantSize,
    onSelection: (PlantSize) -> Unit,
) {

    val plantSizeOptions = listOf(PlantSize.Small, PlantSize.Medium, PlantSize.Large, PlantSize.XLarge)
    var currentSelection by remember { mutableStateOf(initialSelection) }

    Dialog(onDismissRequest = { onDismiss() }) {
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
                            onClick = { currentSelection = plantSize },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = accent500
                            )
                        )
                        Text(
                            text = stringResource(plantSize.textResId),
                            modifier = Modifier.clickable { currentSelection = plantSize }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))

                    SecondaryButton(
                        onClick = { onDismiss() },
                        label = "Cancel"
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryButton(
                        onClick = {
                            onSelection(currentSelection)
                            onDismiss()
                        },
                        label = "Got it"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun SingleSelectionDialog_Preview() {

    var selection: PlantSize by remember { mutableStateOf(PlantSize.Medium) }

    PlantSizeSelectionDialog(
        onDismiss = { },
        initialSelection = selection,
        onSelection = { selection = it }
    )
}