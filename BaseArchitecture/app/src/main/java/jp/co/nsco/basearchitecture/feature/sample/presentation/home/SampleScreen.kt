package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jp.co.nsco.basearchitecture.ui.theme.BaseArchitectureTheme

/**
 * Sample Home画面の表示Composable。
 *
 * ScreenはRouteから渡された表示値だけを描画し、ViewModel、UseCase、Repository、
 * NavController、Scaffoldを直接知らない。
 */
@Composable
fun SampleScreen(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
    }
}

/** Sample Home画面のPreview。 */
@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview() {
    BaseArchitectureTheme {
        SampleScreen(message = "Sample Homeの仮画面です。")
    }
}
