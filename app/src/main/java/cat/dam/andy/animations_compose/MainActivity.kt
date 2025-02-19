package cat.dam.andy.animations_compose
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        content = { padding ->
                            AnimationsContent(padding)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AnimationsContent(padding: PaddingValues) {

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val scale = if (density > 2.0f) 2.0f else 1.0f

    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var isMosquitoClicked by remember { mutableStateOf(false) }
    var isBall1Clicked by remember { mutableStateOf(false) }
    var isBall2Clicked by remember { mutableStateOf(false) }
    var isBall3Started by remember { mutableStateOf(false) }
    var isBall4Clicked by remember { mutableStateOf(false) }
    var isEgg1Clicked by remember { mutableStateOf(false) }
    var isEgg2Clicked by remember { mutableStateOf(false) }
    val egg2FallingDistance = screenHeight.value - 200 * density * scale
    val appearingDuration = 5000


    val ball1Alpha by animateFloatAsState(
        targetValue = if (isBall1Clicked) 0f else 1f,
        animationSpec = tween(durationMillis = 3000),
        label = "ball1 combined animation"
    )

    val ball2Rotation by animateFloatAsState(
        targetValue = if (isBall2Clicked) 360f else 0f,
        animationSpec = tween(durationMillis = 3000),
        label = "ball2 rotation animation"
    )

    val ball2TranslationX by animateFloatAsState(
        targetValue = if (isBall2Clicked) screenWidth.value - 2 * density * scale else 0f,
        animationSpec = tween(durationMillis = 3000),
        label = "ball2 translationX animation"
    )

    val ball3BounceAnimation by animateFloatAsState(
        targetValue = if (isBall3Started) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ball3 bounce animation"
    )

    val ball4Rotation by animateFloatAsState(
        targetValue = if (isBall4Clicked) 0f else 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ball4 rotation animation"
    )

    val ball4Scale by animateFloatAsState(
        targetValue = if (isBall4Clicked) 0.2f else 2f,
        animationSpec = tween(durationMillis = 4000),
        label = "ball4 scale animation"
    )

    val egg1Rotation by animateFloatAsState(
        targetValue = if (isEgg1Clicked) 360f else 0f,
        animationSpec = tween(durationMillis = 3000),
        label = "egg1 rotation animation"
    )

    val egg2OffsetAnimated by animateOffsetAsState(
        targetValue = if (isEgg2Clicked) Offset(0f, egg2FallingDistance) else Offset(0f, 0f),
        animationSpec = tween(
            durationMillis = appearingDuration,
            easing = LinearOutSlowInEasing
        ),
        finishedListener = {
            // Aquí pots gestionar qualsevol lògica després de l'animació
        },
        label = "egg fell down animation"
    )

    // Exemple per execució només d'inici sense premer botó (cal esperar en un fil nou)
    LaunchedEffect(!isBall3Started) {
        delay(500) // Retard per donar temps a la IU de renderitzar-se
        isBall3Started = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Animació del mosquit
        val mosquitoResourceId =
            if (isMosquitoClicked) R.drawable.animation_blood else R.drawable.animation_mosquito
        AnimatedImage(
            resourceId = mosquitoResourceId,
            contentDescription = "Mosquito",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(50.dp)
                .clickable {
                    isMosquitoClicked = !isMosquitoClicked
                }
        )

        // Animacions pilotes
        AnimatedImage(
            resourceId = R.drawable.pilota,
            contentDescription = "Ball 1",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(50.dp)
                .scale(scale)
                .alpha(ball1Alpha)
                .clickable {
                    isBall1Clicked = !isBall1Clicked
                }
        )

        AnimatedImage(
            resourceId = R.drawable.pilota,
            contentDescription = "Ball 2",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(50.dp)
                .scale(scale)
                .graphicsLayer(rotationZ = ball2Rotation, translationX = ball2TranslationX)
                .clickable {
                    // Animació de rotació
                    isBall2Clicked = !isBall2Clicked
                }
        )

        AnimatedImage(
            resourceId = R.drawable.pilota,
            contentDescription = "Ball 3",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(50.dp, 50.dp, 50.dp, 100.dp)
                .scale(scale)
                .graphicsLayer(
                    translationY = ball3BounceAnimation * 50,
                    alpha = 1f
                )
        )

        AnimatedImage(
            resourceId = R.drawable.pilota,
            contentDescription = "Ball 4",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(50.dp, 50.dp, 50.dp, 100.dp)
                .scale(scale)
                .clickable {
                    isBall4Clicked = !isBall4Clicked
                }
                .graphicsLayer(
                    rotationZ = ball4Rotation,
                    scaleX = ball4Scale,
                    scaleY = ball4Scale
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = appearingDuration,
                        easing = LinearOutSlowInEasing
                    )
                )
        )

        // Animacions ous
        AnimatedImage(
            resourceId = if (isEgg1Clicked) R.drawable.chicken else R.drawable.ou,
            contentDescription = if (isEgg1Clicked) "Chicken" else "Egg 1",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(50.dp, 100.dp, 50.dp, 50.dp)
                .scale(scale)
                .graphicsLayer(rotationZ = egg1Rotation)
                .clickable {
                    isEgg1Clicked = !isEgg1Clicked
                })



        AnimatedImage(
            resourceId = R.drawable.ou,
            contentDescription = "Egg 2",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(50.dp, 100.dp, 50.dp, 50.dp)
                .scale(scale)
                .offset(y = -egg2OffsetAnimated.y.dp)
                .clickable {
                    if (!isEgg2Clicked) {
                        isEgg2Clicked = true
                    }
                }
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = appearingDuration,
                        easing = LinearOutSlowInEasing // Aparició inicial
                    )
                )
        )
    }
}

@Composable
fun AnimatedImage(resourceId: Int, contentDescription: String, modifier: Modifier) {
    val painter = rememberAsyncImagePainter(model = resourceId)

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.graphicsLayer()
    )
}