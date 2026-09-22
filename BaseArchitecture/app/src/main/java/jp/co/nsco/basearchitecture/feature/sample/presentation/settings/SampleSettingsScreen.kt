package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

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

/** Sample Settings画面の表示Composable。 */
@Composable
fun SampleSettingsScreen(
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

/** Sample Settings画面のPreview。 */
@Preview(showBackground = true)
@Composable
private fun SampleSettingsScreenPreview() {
    BaseArchitectureTheme {
        SampleSettingsScreen(message = "Sample Settingsの仮画面です。")
    }
}
