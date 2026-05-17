package com.android.purebilibili.feature.video.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import kotlinx.coroutines.launch

@Composable
fun AnimatedGesturePercentText(
    percent: Int,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    modifier: Modifier = Modifier,
    label: String = "gesture-percent-blur-fade"
) {
    val normalizedPercent = percent.coerceIn(0, 100)
    val blurAnim = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(1f) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(normalizedPercent) {
        if (!initialized) {
            initialized = true
            return@LaunchedEffect
        }
        blurAnim.snapTo(6f)
        alphaAnim.snapTo(0.55f)
        launch {
            blurAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 220)
            )
        }
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 180)
        )
    }

    AnimatedContent(
        targetState = normalizedPercent,
        transitionSpec = {
            (fadeIn(animationSpec = tween(180)) togetherWith
                fadeOut(animationSpec = tween(140))) using
                SizeTransform(clip = false)
        },
        label = label
    ) { targetPercent ->
        Text(
            text = "$targetPercent%",
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            modifier = modifier
                .alpha(alphaAnim.value)
                .blur(
                    radius = blurAnim.value.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
        )
    }
}
