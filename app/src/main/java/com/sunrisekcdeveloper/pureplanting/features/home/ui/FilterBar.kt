package com.sunrisekcdeveloper.pureplanting.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.pureplanting.core.design.noRippleClickable
import com.sunrisekcdeveloper.pureplanting.core.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.pureplanting.core.design.theme.ppColors
import com.sunrisekcdeveloper.pureplanting.features.home.models.PlantTabFilter

@Composable
internal fun FilterBar(
    selectedFilter: PlantTabFilter,
    onSelection: (PlantTabFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    @Composable
    fun FilterBarItem(
        text: String,
        selected: Boolean,
        onClick: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .noRippleClickable { onClick() }
                .width(IntrinsicSize.Min)
                .padding(5.dp)
        ) {
            Text(
                text = text,
                color = if (selected) MaterialTheme.ppColors.focused else MaterialTheme.ppColors.unfocused,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.width(IntrinsicSize.Max),
            )
            if (selected) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth(0.5f)
                        .clip(CircleShape)
                        .background(MaterialTheme.ppColors.focused)
                )
            }
        }
    }

    Row(
        modifier = modifier
            .wrapContentWidth()
            .padding(start = 20.dp),
    ) {
        FilterBarItem(
            text = "Upcoming",
            selected = selectedFilter == PlantTabFilter.UPCOMING,
            onClick = { onSelection(PlantTabFilter.UPCOMING) }
        )
        Spacer(modifier = Modifier.weight(0.2f))

        FilterBarItem(
            text = "Forgot to Water",
            selected = selectedFilter == PlantTabFilter.FORGOT_TO_WATER,
            onClick = { onSelection(PlantTabFilter.FORGOT_TO_WATER) }
        )
        Spacer(modifier = Modifier.weight(0.2f))

        FilterBarItem(
            text = "History",
            selected = selectedFilter == PlantTabFilter.HISTORY,
            onClick = { onSelection(PlantTabFilter.HISTORY) }
        )
        Spacer(modifier = Modifier.weight(0.6f))
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterBar_Preview() {
    PurePlantingTheme {
        FilterBar(selectedFilter = PlantTabFilter.UPCOMING, onSelection = {})
    }
}