package ramesh.developer.habittrackerr.ui.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ramesh.developer.habittrackerr.ui.theme.ColorBackground
import ramesh.developer.habittrackerr.ui.theme.ColorBorder
import ramesh.developer.habittrackerr.ui.theme.ColorSuccess

@Composable
fun CompletionToggle(
    isCompleted: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isCompleted) ColorSuccess else Color.Transparent,
        label = "completionBg"
    )

    Box(
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (!isCompleted) Modifier.border(2.dp, ColorBorder, CircleShape)
                else Modifier
            )
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = ColorBackground,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
