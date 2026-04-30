package ramesh.developer.habittrackerr.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary              = ColorPrimary,
    onPrimary            = ColorTextPrimary,
    primaryContainer     = ColorSurfaceElevated,
    onPrimaryContainer   = ColorTextPrimary,
    secondary            = ColorSecondary,
    onSecondary          = ColorTextPrimary,
    secondaryContainer   = ColorSurfaceElevated,
    onSecondaryContainer = ColorTextPrimary,
    tertiary             = ColorAccent,
    onTertiary           = ColorTextPrimary,
    background           = ColorBackground,
    onBackground         = ColorTextPrimary,
    surface              = ColorSurface,
    onSurface            = ColorTextPrimary,
    surfaceVariant       = ColorSurfaceElevated,
    onSurfaceVariant     = ColorTextSecondary,
    surfaceBright        = ColorSurfaceBright,
    error                = ColorDestructive,
    onError              = ColorTextPrimary,
    outline              = ColorBorder,
    outlineVariant       = ColorBorder,
)

@Composable
fun HabitTrackerrTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = Typography,
        shapes      = Shapes,
        content     = content,
    )
}
