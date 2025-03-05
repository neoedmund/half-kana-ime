package com.example.composeime

import android.graphics.Paint.Style
import android.text.style.StyleSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import splitties.systemservices.inputMethodManager

@Composable
fun KeyboardScreen() {
    val keysMatrix = arrayOf(
        arrayOf("_sel", "_back"),
        arrayOf("ｱ", "ｲ", "ｳ", "ｴ", "ｵ", "ｶ", "ｷ", "ｸ", "ｹ", "ｺ", "ｻ", "ｼ", "ｽ", "ｾ", "ｿ"),
        arrayOf("ﾀ", "ﾁ", "ﾂ", "ﾃ", "ﾄ", "ﾅ", "ﾆ", "ﾇ", "ﾈ", "ﾉ", "ﾊ", "ﾋ", "ﾌ", "ﾍ", "ﾎ"),
        arrayOf("ﾏ", "ﾐ", "ﾑ", "ﾒ", "ﾓ", "ﾔ", "ﾕ", "ﾖ", "ﾗ", "ﾘ", "ﾙ", "ﾚ", "ﾛ", "ﾜ", "ﾝ"),
        arrayOf("ﾞ", "ﾟ", "･", "ｦ", "ｧ", "ｨ", "ｩ", "ｪ", "ｫ", "ｬ", "ｭ", "ｮ", "ｯ", "ｰ"),
    )
    Column(
        modifier = Modifier
            .background(Color(0xFF9575CD))
            .fillMaxWidth()
    ) {
        keysMatrix.forEach { row ->
            FixedHeightBox(modifier = Modifier.fillMaxWidth(), height = 32.dp) {
                Row(Modifier) {
                    row.forEach { key ->
                        KeyboardKey(keyboardKey = key, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun FixedHeightBox(modifier: Modifier, height: Dp, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val h = height.roundToPx()
        layout(constraints.maxWidth, h) {
            placeables.forEach { placeable ->
                placeable.place(x = 0, y = kotlin.math.min(0, h - placeable.height))
            }
        }
    }
}

@Composable
fun KeyboardKey(
    keyboardKey: String,
    modifier: Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState()
    val ctx = LocalContext.current
    Box(modifier = modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
        Text(
            text = keyName(keyboardKey),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .border(1.dp, Color.Black)
                .clickable(interactionSource = interactionSource, indication = null) {
                    if (keyboardKey == "_back") {
                        (ctx as IMEService).currentInputConnection.deleteSurroundingText(1, 0)
                    } else if (keyboardKey == "_sel") {
                        inputMethodManager.showInputMethodPicker()
                    } else {
                        (ctx as IMEService).currentInputConnection.commitText(
                            keyboardKey,
                            keyboardKey
                                .length
                        )
                    }
                }
                .background(Color.White)
                .padding(4.dp),
            style = TextStyle(fontSize = 20.sp),
//            fontSize               = 30.dp,
//            fontWeight = FontWeight.Bold,
        )
        if (pressed.value) {
            Text(
                keyName(keyboardKey),
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
                    .background(Color.White)
                    .padding(4.dp),
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
//                fontSize =  30.dp,
//                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun keyName(a: String): String {
    if (a == "_back") {
        return "⇦Backspace"
    } else if (a == "_sel") {
        return "↗Select IME"
    }
    return a


}
