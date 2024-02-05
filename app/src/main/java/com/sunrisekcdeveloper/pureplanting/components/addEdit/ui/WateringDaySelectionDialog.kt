package com.sunrisekcdeveloper.pureplanting.components.addEdit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sunrisekcdeveloper.pureplanting.library.design.noRippleClickable
import com.sunrisekcdeveloper.pureplanting.library.design.theme.accent500
import com.sunrisekcdeveloper.pureplanting.library.design.theme.neutralus300
import com.sunrisekcdeveloper.pureplanting.library.design.theme.neutralus900
import com.sunrisekcdeveloper.pureplanting.library.design.ui.PrimaryButton
import com.sunrisekcdeveloper.pureplanting.library.design.ui.SecondaryButton
import java.time.DayOfWeek
import java.util.Locale

@Composable
fun WateringDaySelectionDialog(
    onDismiss: () -> Unit,
    initialSelection: List<DayOfWeek>,
    onSelection: (List<DayOfWeek>) -> Unit,
) {

    var currentSelection by remember { mutableStateOf(initialSelection.toSet()) }
    val allDays = DayOfWeek.values().toList()

    if (currentSelection.isEmpty()) {
        currentSelection.toMutableSet().run {
            add(DayOfWeek.MONDAY)
            currentSelection = this
        }
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Dates",
                    style = MaterialTheme.typography.bodyLarge
                )

                val everyDayChecked by remember {
                    derivedStateOf { currentSelection.size == allDays.size }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = everyDayChecked,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                currentSelection.toMutableSet().run {
                                    addAll(allDays)
                                    currentSelection = this
                                }
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = accent500,
                            uncheckedColor = neutralus300
                        )
                    )

                    Text(
                        text = "Everyday",
                        color = if (!everyDayChecked) neutralus900.copy(alpha = 0.8f) else neutralus900,
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .noRippleClickable {
                                currentSelection
                                    .toMutableSet()
                                    .run {
                                        addAll(allDays)
                                        currentSelection = this
                                    }
                            },
                    )
                }

                allDays.forEach { day ->
                    val checked by remember { derivedStateOf { currentSelection.contains(day) } }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                currentSelection.toMutableSet().run {
                                    if (isChecked) {
                                        add(day)
                                    } else {
                                        remove(day)
                                    }
                                    currentSelection = this
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = accent500,
                                uncheckedColor = neutralus300
                            )
                        )

                        Text(
                            text = day.name.lowercase()
                                .replaceFirstChar { it.titlecase(Locale.getDefault()) },
                            color = if (!everyDayChecked) neutralus900.copy(alpha = 0.8f) else neutralus900,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .noRippleClickable {
                                    currentSelection
                                        .toMutableSet()
                                        .run {
                                            if (contains(day)) {
                                                remove(day)
                                            } else {
                                                add(day)
                                            }
                                            currentSelection = this
                                        }
                                },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Spacer(modifier = Modifier.weight(1f))

                    SecondaryButton(
                        onClick = { onDismiss() },
                        label = "Cancel"
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryButton(
                        onClick = {
                            onSelection(currentSelection.toList())
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
private fun WateringDaySelectionDialog_Preview() {
    WateringDaySelectionDialog(
        onDismiss = {},
        initialSelection = listOf(DayOfWeek.MONDAY),
        onSelection = {}
    )
}