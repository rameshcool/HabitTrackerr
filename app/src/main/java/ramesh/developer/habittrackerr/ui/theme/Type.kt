package ramesh.developer.habittrackerr.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ramesh.developer.habittrackerr.R

val InterFamily = FontFamily(
    Font(R.font.inter_variable, weight = FontWeight.Normal),
    Font(R.font.inter_variable, weight = FontWeight.Medium),
    Font(R.font.inter_variable, weight = FontWeight.SemiBold),
    Font(R.font.inter_variable, weight = FontWeight.Bold),
    Font(R.font.inter_variable, weight = FontWeight.ExtraBold),
)

val ManropeFamily = FontFamily(
    Font(R.font.manrope_variable, weight = FontWeight.Bold),
    Font(R.font.manrope_variable, weight = FontWeight.ExtraBold),
)

// M3 role → design system:
//   displayLarge  → Manrope 800 / 28sp  (stat numbers, hero title)
//   headlineLarge → Manrope 700 / 22sp  (section headings)
//   titleLarge    → Inter   600 / 16sp  (habit card names)
//   bodyLarge     → Inter   400 / 14sp  (regular body)
//   bodyMedium    → Inter   500 / 14sp  (streak info)
//   labelLarge    → Inter   700 / 16sp  (CTA buttons)
//   labelSmall    → Inter   500 / 11sp  (uppercase labels)
val Typography = Typography(
    displayLarge  = TextStyle(fontFamily = ManropeFamily, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp),
    headlineLarge = TextStyle(fontFamily = ManropeFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleLarge    = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    bodyLarge     = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    bodyMedium    = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelLarge    = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
    labelSmall    = TextStyle(fontFamily = InterFamily, fontWeight = FontWeight.Medium, fontSize = 11.sp, letterSpacing = 1.sp),
)
