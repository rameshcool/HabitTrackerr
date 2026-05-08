package ramesh.developer.habittrackerr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ramesh.developer.habittrackerr.ui.navigation.AppNavigation
import ramesh.developer.habittrackerr.ui.theme.HabitTrackerrTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerrTheme {
                AppNavigation()
            }
        }
    }
}