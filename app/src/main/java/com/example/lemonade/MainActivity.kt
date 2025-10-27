package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LemonadeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LemonadeApp(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding))
                }
            }
        }
    }
}

private enum class Step { SELECT, SQUEEZE, DRINK, RESTART }

@Composable
fun LemonadeApp(modifier: Modifier = Modifier) {
    var step by remember { mutableStateOf(Step.SELECT) }
    var squeezesLeft by remember { mutableStateOf(0) }

    val textId = when (step) {
        Step.SELECT -> R.string.tap_lemon_tree
        Step.SQUEEZE -> R.string.keep_tapping
        Step.DRINK -> R.string.tap_to_drink
        Step.RESTART -> R.string.tap_to_restart
    }
    val imageId = when (step) {
        Step.SELECT -> R.drawable.lemon_tree
        Step.SQUEEZE -> R.drawable.lemon_squeeze
        Step.DRINK -> R.drawable.lemon_drink
        Step.RESTART -> R.drawable.lemon_restart
    }
    val contentDescId = when (step) {
        Step.SELECT -> R.string.lemon_tree_content_description
        Step.SQUEEZE -> R.string.lemon_content_description
        Step.DRINK -> R.string.lemonade_content_description
        Step.RESTART -> R.string.empty_glass_content_description
    }

    fun onTap() {
        when (step) {
            Step.SELECT -> {
                // 2..4 тапа без Range.random - чтобы не ловить compareTo
                squeezesLeft = Random.nextInt(from = 2, until = 5)
                step = Step.SQUEEZE
            }
            Step.SQUEEZE -> {
                squeezesLeft -= 1
                if (squeezesLeft <= 0) step = Step.DRINK
            }
            Step.DRINK -> step = Step.RESTART
            Step.RESTART -> step = Step.SELECT
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(textId), fontSize = 20.sp)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = ::onTap,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Image(
                painter = painterResource(imageId),
                contentDescription = stringResource(contentDescId)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LemonadePreview() {
    LemonadeTheme { LemonadeApp() }
}
