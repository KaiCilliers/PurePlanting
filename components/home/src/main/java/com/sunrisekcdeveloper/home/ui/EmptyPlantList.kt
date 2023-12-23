package com.sunrisekcdeveloper.home.ui


import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.sunrisekcdeveloper.components.home.R
import com.sunrisekcdeveloper.design.theme.PurePlantingTheme
import com.sunrisekcdeveloper.design.ui.BoxWithBottomFade
import com.sunrisekcdeveloper.home.models.PlantTabFilter
import com.sunrisekcdeveloper.home.models.PlantTabFilter.FORGOT_TO_WATER
import com.sunrisekcdeveloper.home.models.PlantTabFilter.HISTORY
import com.sunrisekcdeveloper.home.models.PlantTabFilter.UPCOMING
import com.sunrisekcdeveloper.notification.domain.Notification
import com.sunrisekcdeveloper.notification.domain.PlantTag
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
internal fun EmptyPlantList(
    selectedFilter: PlantTabFilter,
) {

    val text = when (selectedFilter) {
        UPCOMING -> EmptyPlantListText.AddPlants()
        FORGOT_TO_WATER -> EmptyPlantListText.ForgotNothing()
        HISTORY -> EmptyPlantListText.NoHistory()
    }

    BoxWithBottomFade {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.three_plants),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.weight(0.1f))

            Text(
                text = stringResource(id = text.title),
                style = MaterialTheme.typography.displayLarge,
            )
            Spacer(modifier = Modifier.weight(0.05f))

            Text(
                text = stringResource(id = text.description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 30.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private sealed class EmptyPlantListText(
    @StringRes open val title: Int,
    @StringRes open val description: Int
) {

    data class AddPlants(
        override val title: Int = R.string.upcoming_plant_list_empty_title,
        override val description: Int = R.string.upcoming_plant_list_empty_body
    ) : EmptyPlantListText(title, description)

    data class ForgotNothing(
        override val title: Int = R.string.forgot_to_water_plant_list_empty_title,
        override val description: Int = R.string.forgot_to_water_plant_list_empty_body
    ) : EmptyPlantListText(title, description)

    data class NoHistory(
        override val title: Int = R.string.history_plant_list_empty_title,
        override val description: Int = R.string.history_plant_list_empty_body
    ) : EmptyPlantListText(title, description)
}

@Preview
@Composable
private fun EmptyPlantList_Preview(
    @PreviewParameter(PlantTabFilterPreviewParameterProvider::class) filter: PlantTabFilter,
) {
    ThemeSurfaceWrapper {
        EmptyPlantList(
            selectedFilter = filter,
        )
    }
}

private class PlantTabFilterPreviewParameterProvider : PreviewParameterProvider<PlantTabFilter> {
    override val values = PlantTabFilter.values().asSequence()
}