package ramesh.developer.habittrackerr.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ramesh.developer.habittrackerr.domain.model.HabitIcon
import ramesh.developer.habittrackerr.ui.designsystem.toDrawableRes
import ramesh.developer.habittrackerr.ui.theme.ColorPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorSurfaceElevated
import ramesh.developer.habittrackerr.ui.theme.ColorTextPrimary

@Composable
fun IconPreviewBox(
    icon: HabitIcon,
    isEditMode: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = MaterialTheme.shapes.extraLarge
    val primaryColor = ColorPrimary

    val boxModifier = if (isEditMode) {
        modifier
            .size(72.dp)
            .clip(shape)
            .background(ColorSurfaceElevated, shape)
            .border(1.5.dp, primaryColor, shape)
            .clickable(onClick = onClick)
    } else {
        modifier
            .size(72.dp)
            .clip(shape)
            .background(ColorSurfaceElevated, shape)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val cornerRadiusPx = 20.dp.toPx()
                drawRoundRect(
                    color = primaryColor,
                    cornerRadius = CornerRadius(cornerRadiusPx),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f)),
                        cap = StrokeCap.Round
                    )
                )
            }
            .clickable(onClick = onClick)
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon.toDrawableRes()),
            contentDescription = icon.id,
            colorFilter = ColorFilter.tint(ColorTextPrimary),
            modifier = Modifier.size(36.dp)
        )
    }
}
