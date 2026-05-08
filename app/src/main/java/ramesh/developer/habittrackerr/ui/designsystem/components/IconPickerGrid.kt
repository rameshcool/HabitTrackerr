package ramesh.developer.habittrackerr.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ramesh.developer.habittrackerr.domain.model.HabitIcon
import ramesh.developer.habittrackerr.ui.designsystem.toDrawableRes
import ramesh.developer.habittrackerr.ui.theme.ColorPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorPrimaryLight
import ramesh.developer.habittrackerr.ui.theme.ColorSecondary
import ramesh.developer.habittrackerr.ui.theme.ColorSurfaceElevated
import ramesh.developer.habittrackerr.ui.theme.ColorTextPrimary

private val ALL_ICONS = HabitIcon.entries
private const val COLUMNS = 5

@Composable
fun IconPickerGrid(
    selectedIcon: HabitIcon,
    onSelectIcon: (HabitIcon) -> Unit,
    modifier: Modifier = Modifier
) {
    // Use static rows to avoid LazyVerticalGrid nested scroll conflict
    val rows = ALL_ICONS.chunked(COLUMNS)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEach { rowIcons ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowIcons.forEach { icon ->
                    val selected = icon == selectedIcon
                    val shape = MaterialTheme.shapes.medium
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(shape)
                            .background(if (selected) ColorPrimary else ColorSurfaceElevated)
                            .then(
                                if (selected) Modifier.border(2.dp, ColorPrimaryLight, shape)
                                else Modifier
                            )
                            .clickable { onSelectIcon(icon) }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(icon.toDrawableRes()),
                            contentDescription = icon.id,
                            colorFilter = ColorFilter.tint(
                                if (selected) ColorTextPrimary else ColorSecondary
                            )
                        )
                    }
                }
                // Fill remaining cells in the last row if needed
                val remaining = COLUMNS - rowIcons.size
                repeat(remaining) {
                    Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                }
            }
        }
    }
}
