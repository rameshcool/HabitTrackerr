package ramesh.developer.habittrackerr.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// M3 role → design system:
//   extraSmall → 6dp   (heatmap cells)
//   small      → 10dp  (day picker buttons)
//   medium     → 12dp  (icon containers, text fields)
//   large      → 14dp  (habit cards, CTA buttons, FAB)
//   extraLarge → 20dp  (icon picker selected display)
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small      = RoundedCornerShape(10.dp),
    medium     = RoundedCornerShape(12.dp),
    large      = RoundedCornerShape(14.dp),
    extraLarge = RoundedCornerShape(20.dp),
)
