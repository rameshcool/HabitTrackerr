package ramesh.developer.habittrackerr.ui.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ramesh.developer.habittrackerr.ui.theme.ColorTextTertiary

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = ColorTextTertiary,
        modifier = modifier
    )
}
