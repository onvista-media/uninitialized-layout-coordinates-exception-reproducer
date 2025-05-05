package uninitialized.layoutcoordinates.exception

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uninitialized.layoutcoordinates.exception.ui.theme.UninitializedLayoutCoordinatesExceptionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UninitializedLayoutCoordinatesExceptionTheme {
                Screen()
            }
        }
    }
}

/**
 * Press the button then rotate the device.
 * -> IllegalArgumentException: Error: Uninitialized LayoutCoordinates
 */
@Composable
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
internal fun Screen() {
    var contentShown by rememberSaveable { mutableStateOf(false) }
    SharedTransitionLayout {
        AnimatedContent(
            targetState = contentShown
        ) { shown ->
            if (shown.not()) {
                Button(
                    modifier = Modifier.padding(100.dp),
                    onClick = { contentShown = contentShown.not() }
                ) {
                    Text(
                        text = "Title",
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = "SHARED_ELEMENT"),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    )
                }
            } else {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Title",
                                    modifier = Modifier.sharedElement(
                                        sharedContentState = rememberSharedContentState(key = "SHARED_ELEMENT"),
                                        animatedVisibilityScope = this
                                    )
                                )
                            },
                        )
                    }
                ) { p ->
                    Text(
                        modifier = Modifier.padding(p),
                        text = "Content"
                    )
                }
            }
        }
    }
}