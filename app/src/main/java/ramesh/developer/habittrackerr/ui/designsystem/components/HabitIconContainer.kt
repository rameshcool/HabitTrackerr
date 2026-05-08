package ramesh.developer.habittrackerr.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ramesh.developer.habittrackerr.domain.model.HabitIcon
import ramesh.developer.habittrackerr.ui.designsystem.toDrawableRes
import ramesh.developer.habittrackerr.ui.theme.ColorSurfaceElevated
import ramesh.developer.habittrackerr.ui.theme.ColorTextSecondary

@Composable
fun HabitIconContainer(
    icon: HabitIcon,
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(ColorSurfaceElevated, MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon.toDrawableRes()),
            contentDescription = icon.id,
            colorFilter = ColorFilter.tint(ColorTextSecondary),
            modifier = Modifier.size(size * 0.55f)
        )
    }
}
