package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.DayOfWeek

@Composable
fun PPDateSelectionDialog(
    dismiss: () -> Unit,
    initialSelections: List<DayOfWeek>, // todo use custom data type
    updateSelection: (List<DayOfWeek>) -> Unit,
) {

    var currentSelections by remember { mutableStateOf(initialSelections.toSet()) }
    val allDates = DayOfWeek.values().toList()

    if (currentSelections.isEmpty()) {
        currentSelections.toMutableSet().run {
            add(DayOfWeek.MONDAY)
            currentSelections = this
        }
    }

    Dialog(onDismissRequest = { dismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp), // todo remove hardcoded value
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = "Dates")

                val everyDayChecked by remember {
                    derivedStateOf { currentSelections.size == allDates.size }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = everyDayChecked,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                currentSelections.toMutableSet().run {
                                    addAll(allDates)
                                    currentSelections = this
                                }
                            }
                        }
                    )

                    Text(
                        text = "Everyday",
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .noRippleClickable {
                                currentSelections.toMutableSet().run {
                                    addAll(allDates)
                                    currentSelections = this
                                }
                            },
                    )
                }

                allDates.forEach { date ->
                    val checked by remember { derivedStateOf { currentSelections.contains(date) } }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                currentSelections.toMutableSet().run {
                                    if (isChecked) {
                                        add(date)
                                    } else {
                                        remove(date)
                                    }
                                    currentSelections = this
                                }
                            }
                        )

                        Text(
                            text = date.name,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .noRippleClickable {
                                    currentSelections
                                        .toMutableSet()
                                        .run {
                                            if (contains(date)) {
                                                remove(date)
                                            } else {
                                                add(date)
                                            }
                                            currentSelections = this
                                        }
                                },
                        )
                    }
                }

                Row {
                    Button(onClick = { dismiss() }) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        updateSelection(currentSelections.toList())
                        dismiss()
                    }) {
                        Text(text = "Got it")
                    }
                }
            }
        }
    }
}

// todo live template to create this standard preview with the file (note, private)
@Preview
@Composable
private fun PPDateSelectionDialog_Preview() {
    PPDateSelectionDialog(
        dismiss = {},
        initialSelections = listOf(DayOfWeek.MONDAY),
        updateSelection = {}
    )
}

// todo relocate
inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier =
    composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { onClick() }
        )
    }