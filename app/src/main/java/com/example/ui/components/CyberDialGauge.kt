package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ElectricAmber
import com.example.ui.theme.LaserLime
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun CyberDialGauge(
    percentage: Int,
    label: String,
    subLabel: String,
    modifier: Modifier = Modifier,
    size: Dp = 190.dp,
    primaryColor: Color = CyanNeon,
    accentColor: Color = ElectricAmber
) {
    val animatedPercent by animateFloatAsState(
        targetValue = percentage.coerceIn(0, 100).toFloat(),
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "dial_anim"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = 14.dp.toPx()
            val canvasSize = this.size.minDimension
            val radius = (canvasSize - strokeWidth) / 2
            val center = Offset(this.size.width / 2, this.size.height / 2)

            val startAngle = 140f
            val sweepAngle = 260f

            // Background track
            drawArc(
                color = ObsidianCard,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Dynamic Gradient Arc
            val progressSweep = (animatedPercent / 100f) * sweepAngle
            val arcGradient = Brush.sweepGradient(
                colors = listOf(primaryColor, accentColor, LaserLime),
                center = center
            )

            drawArc(
                brush = arcGradient,
                startAngle = startAngle,
                sweepAngle = progressSweep,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Inner subtle kinetic ring
            drawCircle(
                color = primaryColor.copy(alpha = 0.08f),
                radius = radius - strokeWidth - 6.dp.toPx(),
                center = center
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${animatedPercent.toInt()}%",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = TextPrimary
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
            Text(
                text = subLabel,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}
