package com.sunrisekcdeveloper.detail

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.sunrisekcdeveloper.components.detail.R
import com.sunrisekcdeveloper.detail.ui.PPDetailsBanner
import com.sunrisekcdeveloper.detail.ui.PlantDetailLabel
import com.sunrisekcdeveloper.ui.ThemeSurfaceWrapper

@Composable
fun DetailUi(
    viewModel: DetailViewModel,
    modifier: Modifier = Modifier,
) {

    val plant by viewModel.plant.collectAsState()

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
        ) {

            TopSection(
                plant.details.imageSrcUri,
                modifier = Modifier.weight(1f)
            )

            BottomSection(
                modifier = Modifier
                    .heightIn(min = 350.dp)
                    .offset(y = (-25).dp),
                title = plant.details.name,
                description = plant.details.description,
                onButtonClick = { viewModel.onWaterPlant() }
            )
        }

        BackIcon(Modifier.padding(top = 30.dp, start = 20.dp))

        EditIcon(
            Modifier
                .wrapContentSize()
                .align(alignment = Alignment.TopEnd)
                .padding(top = 30.dp, end = 20.dp)
        )
    }
}

@Preview
@Composable
private fun DetailUi_Preview() {
    ThemeSurfaceWrapper {
        DetailUi(DetailViewModel.Fake())
    }
}

@Composable
private fun TopSection(
    plantImgSrc: String,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        ImageWithBottomFadeGradient(modifier = Modifier.fillMaxSize(), imgSrc = plantImgSrc)

        PPDetailsBanner(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .padding(bottom = 40.dp)
                .align(Alignment.BottomCenter),
            labels = listOf(
                PlantDetailLabel(label = "Size", value = "Medium"),
                PlantDetailLabel(label = "Water", value = "250ml"),
                PlantDetailLabel(label = "Frequency", value = "2 times/week"),
            ),
        )
    }
}

@Composable
private fun BackIcon(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.arrow_left_with_border),
        contentDescription = "",
        alignment = Alignment.TopStart,
        modifier = modifier
    )
}

@Composable
private fun EditIcon(modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(40.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.edit2),
            contentDescription = "",
            modifier = Modifier
                .size(35.dp)
                .padding(5.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)

        )
    }
}

@Composable
private fun ImageWithBottomFadeGradient(
    modifier: Modifier = Modifier,
    imgSrc: String,
) {
    Box(
        modifier = modifier
    ) {
        if (imgSrc.isNotBlank()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(model = Uri.parse(imgSrc)),
                contentDescription = "",
                alignment = Alignment.Center,
                contentScale = ContentScale.FillBounds
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.top_bar_plant),
                contentDescription = "",
                alignment = Alignment.TopCenter
            )
            Image(
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .padding(top = 70.dp),
                painter = painterResource(id = R.drawable.single_plant_placeholder),
                contentDescription = "",
            )
        }

        Column {
            Spacer(modifier = Modifier.weight(0.8f))
            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxSize()
                    .clip(RectangleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
        }
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier,
    title: String,
    description: String,
    onButtonClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            val (waterButton, descriptionView, titleView) = createRefs()

            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .constrainAs(titleView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },

                )
            Text(
                text = description,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .wrapContentHeight()
                    .constrainAs(descriptionView) {
                        top.linkTo(titleView.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(waterButton.top, margin = 8.dp)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    },
                lineHeight = 1.5.em,

                )
            LargeButton(
                onButtonClick = { onButtonClick() },
                label = "Mark as Watered",
                modifier = Modifier.constrainAs(waterButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun LargeButton(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    label: String
) {
    Button(
        onClick = { onButtonClick() },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 60.dp),
        shape = RoundedCornerShape(7.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 5.dp)
        )
    }
}