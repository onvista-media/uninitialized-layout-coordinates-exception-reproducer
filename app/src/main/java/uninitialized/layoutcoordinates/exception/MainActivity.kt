package uninitialized.layoutcoordinates.exception

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import uninitialized.layoutcoordinates.exception.ui.theme.UninitializedLayoutCoordinatesExceptionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UninitializedLayoutCoordinatesExceptionTheme {
                ScreenContent()
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun ScreenContent() {
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
                        text = "Content: $shown",
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = "SHARED_ELEMENT"),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    )
                }
            }
            if (shown) {
                AnimatedTitleContent(
                    animatedVisibilityScope = this@AnimatedContent
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AnimatedTitleContent(
    animatedVisibilityScope: AnimatedContentScope
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = "Content",
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = "SHARED_ELEMENT"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                },
            )
        }
    ) { p ->
        Text(
            modifier = Modifier.padding(p),
            text = "Some Text"
        )
    }
}