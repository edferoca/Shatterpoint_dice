package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AttackDiceFace
import com.example.data.DefenseDiceFace
import com.example.ui.theme.*

@Composable
fun ShatterpointCriticalIcon(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black,
    starColor: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val size = this.size
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = size.minDimension / 2f
        
        // Solid background circle
        drawCircle(
            color = backgroundColor,
            radius = radius
        )
        
        // Scale and lengths based on actual size
        val rMax = radius * 0.72f // Cardinal arms length
        val rMid = radius * 0.38f // Diagonal arms length
        val rInner = radius * 0.12f // Center size
        
        // Star cardinal arms
        val starPath = Path().apply {
            moveTo(cx, cy - rMax)
            quadraticTo(cx + rInner, cy - rInner, cx + rMax, cy) // to Right
            quadraticTo(cx + rInner, cy + rInner, cx, cy + rMax) // to Bottom
            quadraticTo(cx - rInner, cy + rInner, cx - rMax, cy) // to Left
            quadraticTo(cx - rInner, cy - rInner, cx, cy - rMax) // back to Top
            close()
        }
        
        // Star diagonal arms
        val diagonalPath = Path().apply {
            val cos45 = 0.7071f
            val dMax = rMid
            val dInner = rInner * 0.8f
            
            val trX = cx + dMax * cos45
            val trY = cy - dMax * cos45
            
            val brX = cx + dMax * cos45
            val brY = cy + dMax * cos45
            
            val blX = cx - dMax * cos45
            val blY = cy + dMax * cos45
            
            val tlX = cx - dMax * cos45
            val tlY = cy - dMax * cos45
            
            moveTo(trX, trY)
            quadraticTo(cx + dInner, cy, brX, brY)
            quadraticTo(cx, cy + dInner, blX, blY)
            quadraticTo(cx - dInner, cy, tlX, tlY)
            quadraticTo(cx, cy - dInner, trX, trY)
            close()
        }
        
        drawPath(path = starPath, color = starColor)
        drawPath(path = diagonalPath, color = starColor)
        
        // Small center circle
        drawCircle(color = starColor, radius = rInner * 0.8f)
    }
}

@Composable
fun ShatterpointStrikeIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black
) {
    Canvas(modifier = modifier) {
        val size = this.size
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = size.minDimension / 2f
        
        val rMax = radius * 0.85f // Cardinal arms length
        val rMid = radius * 0.45f // Diagonal arms length
        val rInner = radius * 0.15f // Center size
        
        // Star cardinal arms
        val starPath = Path().apply {
            moveTo(cx, cy - rMax)
            quadraticTo(cx + rInner, cy - rInner, cx + rMax, cy)
            quadraticTo(cx + rInner, cy + rInner, cx, cy + rMax)
            quadraticTo(cx - rInner, cy + rInner, cx - rMax, cy)
            quadraticTo(cx - rInner, cy - rInner, cx, cy - rMax)
            close()
        }
        
        // Star diagonal arms
        val diagonalPath = Path().apply {
            val cos45 = 0.7071f
            val dMax = rMid
            val dInner = rInner * 0.8f
            
            val trX = cx + dMax * cos45
            val trY = cy - dMax * cos45
            
            val brX = cx + dMax * cos45
            val brY = cy + dMax * cos45
            
            val blX = cx - dMax * cos45
            val blY = cy + dMax * cos45
            
            val tlX = cx - dMax * cos45
            val tlY = cy - dMax * cos45
            
            moveTo(trX, trY)
            quadraticTo(cx + dInner, cy, brX, brY)
            quadraticTo(cx, cy + dInner, blX, blY)
            quadraticTo(cx - dInner, cy, tlX, tlY)
            quadraticTo(cx, cy - dInner, trX, trY)
            close()
        }
        
        drawPath(path = starPath, color = color)
        drawPath(path = diagonalPath, color = color)
        drawCircle(color = color, radius = rInner * 0.8f)
    }
}

@Composable
fun ShatterpointExpertiseIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    innerColor: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val size = this.size
        val cx = size.width / 2f
        val cy = size.height / 2f
        val w = size.width
        val h = size.height
        
        // Outer shape
        val path = Path().apply {
            moveTo(cx, h * 0.12f)
            lineTo(cx + w * 0.35f, cy - h * 0.12f)
            lineTo(cx + w * 0.35f, cy + h * 0.05f)
            lineTo(cx, h * 0.90f)
            lineTo(cx - w * 0.35f, cy + h * 0.05f)
            lineTo(cx - w * 0.35f, cy - h * 0.12f)
            close()
        }
        drawPath(path = path, color = color)
        
        // Inner triangle / hollow chevron pointing down
        val innerPath = Path().apply {
            moveTo(cx, cy - h * 0.06f)
            lineTo(cx + w * 0.18f, cy - h * 0.14f)
            lineTo(cx, cy + h * 0.32f)
            lineTo(cx - w * 0.18f, cy - h * 0.14f)
            close()
        }
        drawPath(path = innerPath, color = innerColor)
    }
}

@Composable
fun ShatterpointFailureIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    cutoutColor: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val size = this.size
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        
        // Base concave square of the failure symbol
        val path = Path().apply {
            moveTo(w * 0.18f, h * 0.18f) // Top-Left
            quadraticTo(cx, h * 0.30f, w * 0.82f, h * 0.18f) // TR
            quadraticTo(w * 0.70f, cy, w * 0.82f, h * 0.82f) // BR
            quadraticTo(cx, h * 0.70f, w * 0.18f, h * 0.82f) // BL
            quadraticTo(w * 0.30f, cy, w * 0.18f, h * 0.18f) // TL
            close()
        }
        drawPath(path = path, color = color)
        
        // Organic crack line down the middle
        val crackPath = Path().apply {
            moveTo(cx + w * 0.02f, h * 0.22f)
            cubicTo(
                cx - w * 0.05f, h * 0.32f,
                cx - w * 0.22f, h * 0.40f,
                cx - w * 0.15f, h * 0.49f
            )
            cubicTo(
                cx - w * 0.03f, h * 0.55f,
                cx + w * 0.04f, h * 0.45f,
                cx + w * 0.10f, h * 0.60f
            )
            quadraticTo(cx + w * 0.01f, h * 0.68f, cx - w * 0.02f, h * 0.76f)
        }
        
        drawPath(
            path = crackPath,
            color = cutoutColor,
            style = Stroke(
                width = 4.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        
        // Circular crack cutout that makes it look like a customized broken/shattered eye or hourglass center
        drawCircle(
            color = cutoutColor,
            radius = w * 0.09f,
            center = androidx.compose.ui.geometry.Offset(cx - w * 0.08f, cy + h * 0.02f)
        )
        
        // Tiny core dot matching the background color to leave an organic ring shape
        drawCircle(
            color = color,
            radius = w * 0.04f,
            center = androidx.compose.ui.geometry.Offset(cx - w * 0.08f, cy + h * 0.02f)
        )
    }
}

@Composable
fun ShatterpointShieldIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val size = this.size
        val w = size.width
        val h = size.height
        val cx = w / 2f
        
        val path = Path().apply {
            moveTo(cx, h * 0.12f)
            lineTo(w * 0.82f, h * 0.12f)
            lineTo(w * 0.82f, h * 0.5f)
            quadraticTo(w * 0.82f, h * 0.82f, cx, h * 0.92f)
            quadraticTo(w * 0.18f, h * 0.82f, w * 0.18f, h * 0.5f)
            lineTo(w * 0.18f, h * 0.12f)
            close()
        }
        drawPath(path = path, color = color)
        
        // Inner line highlight
        val innerPath = Path().apply {
            moveTo(cx, h * 0.22f)
            lineTo(w * 0.72f, h * 0.22f)
            lineTo(w * 0.72f, h * 0.5f)
            quadraticTo(w * 0.72f, h * 0.76f, cx, h * 0.83f)
            quadraticTo(w * 0.28f, h * 0.76f, w * 0.28f, h * 0.5f)
            lineTo(w * 0.28f, h * 0.22f)
            close()
        }
        drawPath(path = innerPath, color = Color(0xFF0D47A1), style = Stroke(width = 3.dp.toPx()))
    }
}

@Composable
fun AttackDieWidget(
    face: AttackDiceFace,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val innerDieBg = Color(0xFFEFEFEF)
    Box(
        modifier = modifier
            .size(72.dp)
            .testTag("attack_die_${face.name.lowercase()}")
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0xFFFCFCFC), Color(0xFFD6D6D6))
                )
            )
            .border(2.dp, Color.Black.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Subtle inner highlight ring to simulate depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
                .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(11.dp))
        )
        Box(
            modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            when (face) {
                AttackDiceFace.CRITICAL -> {
                    ShatterpointCriticalIcon(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                AttackDiceFace.STRIKE -> {
                    ShatterpointStrikeIcon(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black
                    )
                }
                AttackDiceFace.EXPERTISE -> {
                    ShatterpointExpertiseIcon(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black,
                        innerColor = innerDieBg
                    )
                }
                AttackDiceFace.FAILURE -> {
                    ShatterpointFailureIcon(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black,
                        cutoutColor = innerDieBg
                    )
                }
            }
        }
    }
}

@Composable
fun DefenseDieWidget(
    face: DefenseDiceFace,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val defenseDarkBlue = Color(0xFF0D47A1)
    Box(
        modifier = modifier
            .size(72.dp)
            .testTag("defense_die_${face.name.lowercase()}")
            .clip(CircleShape)
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFF0D47A1))
                )
            )
            .border(2.dp, Color.White.copy(alpha = 0.45f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Subtle inner highlight ring to simulate depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
                .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
        )
        Box(
            modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            when (face) {
                DefenseDiceFace.BLOCK -> {
                    ShatterpointShieldIcon(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White
                    )
                }
                DefenseDiceFace.EXPERTISE -> {
                    ShatterpointExpertiseIcon(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White,
                        innerColor = defenseDarkBlue
                    )
                }
                DefenseDiceFace.FAILURE -> {
                    ShatterpointFailureIcon(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White,
                        cutoutColor = defenseDarkBlue
                    )
                }
            }
        }
    }
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DeepCharcoal,
    borderStroke: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = borderStroke,
        content = content
    )
}

@Composable
fun DiceSelectorRow(
    title: String,
    count: Int,
    color: Color,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { if (count > 0) onValueChange(count - 1) },
                colors = IconButtonDefaults.iconButtonColors(containerColor = LightCharcoal)
            ) {
                Text("-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(36.dp)
                    .background(LightCharcoal, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = { if (count < 20) onValueChange(count + 1) },
                colors = IconButtonDefaults.iconButtonColors(containerColor = LightCharcoal)
            ) {
                Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}
