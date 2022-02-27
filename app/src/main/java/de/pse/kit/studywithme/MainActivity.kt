package de.pse.kit.studywithme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.pse.kit.myapplication.ui.theme.*
import de.pse.kit.studywithme.ui.view.navigation.MainView
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme: Boolean = isSystemInDarkTheme()
            val colors = if (darkTheme) Green900 else Grey200

            SideEffect {
                systemUiController.setSystemBarsColor(colors, darkIcons = !darkTheme)
            }

            MyApplicationTheme3 {
                MainView()
            }
        }
    }
}
