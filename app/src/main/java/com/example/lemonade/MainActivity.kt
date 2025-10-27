package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

private enum class LemonadeStep { SELECT, SQUEEZE, DRINK, RESTART }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LemonadeApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LemonadeApp() {
    var step by rememberSaveable { mutableStateOf(LemonadeStep.SELECT) }
    var tapsRemaining by rememberSaveable { mutableStateOf(0) }

    val titleText = when (step) {
        LemonadeStep.SELECT -> R.string.tap_lemon_tree
        LemonadeStep.SQUEEZE -> R.string.keep_tapping
        LemonadeStep.DRINK -> R.string.tap_lemonade
        LemonadeStep.RESTART -> R.string.tap_empty_glass
    }

    val imageRes = when (step) {
        LemonadeStep.SELECT -> R.drawable.lemon_tree
        LemonadeStep.SQUEEZE -> R.drawable.lemon_squeeze
        LemonadeStep.DRINK -> R.drawable.lemon_drink
        LemonadeStep.RESTART -> R.drawable.lemon_restart
    }

    val contentDesc = when (step) {
        LemonadeStep.SELECT -> R.string.lemon_tree_content_description
        LemonadeStep.SQUEEZE -> R.string.lemon_content_description
        LemonadeStep.DRINK -> R.string.glass_of_lemonade_content_description
        LemonadeStep.RESTART -> R.string.empty_glass_content_description
    }

    fun onTap() {
        when (step) {
            LemonadeStep.SELECT -> {
                tapsRemaining = Random.nextInt(2, 5) // 2..4
                step = LemonadeStep.SQUEEZE
            }
            LemonadeStep.SQUEEZE -> {
                tapsRemaining -= 1
                if (tapsRemaining <= 0) step = LemonadeStep.DRINK
            }
            LemonadeStep.DRINK -> step = LemonadeStep.RESTART
            LemonadeStep.RESTART -> step = LemonadeStep.SELECT
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lemonade") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = stringResource(titleText),
                    fontSize = 18.sp,                 // крупнее стандарта
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))       // отступ между текстом и изображением

                Button(
                    onClick = { onTap() },
                            shape = RoundedCornerShape(16.dp), // слегка скругленные углы
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    contentPadding = PaddingValues(24.dp)
                ) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = stringResource(contentDesc),
                        modifier = Modifier.size(240.dp)
                    )
                }

                Text("step = ${step.name}, taps = $tapsRemaining") // временно

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LemonadePreview() {
    LemonadeApp()
}
