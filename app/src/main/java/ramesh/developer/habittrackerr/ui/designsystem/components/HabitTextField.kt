package ramesh.developer.habittrackerr.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ramesh.developer.habittrackerr.ui.theme.ColorBorder
import ramesh.developer.habittrackerr.ui.theme.ColorDestructive
import ramesh.developer.habittrackerr.ui.theme.ColorSurface
import ramesh.developer.habittrackerr.ui.theme.ColorTextPrimary
import ramesh.developer.habittrackerr.ui.theme.ColorTextTertiary

@Composable
fun HabitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = ColorTextPrimary),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorSurface, MaterialTheme.shapes.medium)
                        .border(
                            width = if (errorMessage != null) 1.5.dp else 1.dp,
                            color = if (errorMessage != null) ColorDestructive else ColorBorder,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = ColorTextTertiary
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = ColorDestructive,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
